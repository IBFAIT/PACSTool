package com.fourquant.riqae.pacs;

import com.fourquant.riqae.pacs.tools.Operation;
import org.dcm4che3.tool.findscu.FindSCU;
import org.dcm4che3.tool.getscu.GetSCU;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.function.Consumer;
import java.util.logging.Logger;

import static com.fourquant.riqae.pacs.csv.CSVProtocol.*;
import static com.fourquant.riqae.pacs.tools.Operation.*;
import static java.nio.file.Files.readAllBytes;

public final class SCUOperationWrapper {

  private static final Logger log =
        Logger.getLogger(SCUOperationWrapper.class.getName());

  final String userName;
  final String server;
  final String port;

  public SCUOperationWrapper(final String userName, final String server,
                             final String port) {

    this.userName = userName;
    this.server = server;
    this.port = port;
  }

  private static FileFilter dcmFilter() {
    return pathname -> pathname.getName().endsWith(".dcm");
  }

  public static <T> Consumer<T> measuringConsumer(final Consumer<T> block) {
    return t -> {
      float startTime = System.nanoTime();
      block.accept(t);
      float endTime = System.nanoTime();
      float duration = (endTime - startTime) / 1000000 / 1000;

      log.info("Duration: " + duration + " seconds");
    };
  }

  final public String[] resolve(final Operation operation,
                                final String argument)
        throws IOException, InterruptedException {

    final File tempDirectory = createTempDirectory();

    final String[] arguments = createSCUArguments(
          argument, tempDirectory, operation);

    final String[] xmlResults =
          executeFindSCU(tempDirectory, arguments);

    deleteOnExit(tempDirectory);

    return xmlResults;
  }

  final public String[] resolvePatientIDs(final String patientName)
        throws IOException, InterruptedException {

    return resolve(RESOLVE_PATIENT_IDS, patientName);
  }

  public String[] resolveStudyInstanceUIDs(final String patientID)
        throws IOException, InterruptedException {

    return resolve(RESOLVE_STUDY_INSTANCE_UIDS, patientID);
  }

  public String[] resolveSeriesInstanceUIDs(final String studyInstanceUID)
        throws IOException, InterruptedException {

    return resolve(RESOLVE_SERIES_INSTANCE_UIDS, studyInstanceUID);
  }

  public File[] fetchSeries(final String seriesInstanceUID)
        throws IOException, InterruptedException {

    final File tempDirectory = createTempDirectory();

    final String[] scuArguments = createSCUArguments(
          seriesInstanceUID, tempDirectory, FETCH_SERIES);

    final File[] files = executeFetchSCU(tempDirectory, scuArguments);

    deleteOnExit(tempDirectory);

    return files;
  }

  private String[] createSCUArguments(
        final String argument, final File tempDirectory, final Operation op) {

    return createSCUArguments(argument, tempDirectory.getAbsolutePath(), op);
  }

  private String[] createSCUArguments(
        final String argument, final Path tempDirectory,
        final Operation op) {

    return createSCUArguments(argument, tempDirectory.toString(), op);
  }

  private String[] createSCUArguments(final String argument,
                                      final String tempDirectory,
                                      final Operation op) {

    switch (op) {
      case RESOLVE_PATIENT_IDS:

        return new String[]{"-r", PATIENT_ID_KEYWORD,
              "-c", userName + "@" + server + ":" + port,
              "-m", PATIENT_NAME_KEYWORD + "=" + argument.replaceAll(" ", "^"),
              "--out-dir", tempDirectory,
              "--xml"};

      case RESOLVE_STUDY_INSTANCE_UIDS:

        return new String[]{
              "-r", PATIENT_ID_KEYWORD,
              "-r", PATIENT_NAME_KEYWORD,
              "-r", STUDY_INSTANCE_UI_KEYWORD,
              "-r", STUDY_DATE_KEYWORD,
              "-r", STUDY_DESCRIPTION_KEYWORD,
              "-L", "STUDY",
              "-c", userName + "@" + server + ":" + port,
              "-m", "PatientID=" + argument,
              "--out-dir", tempDirectory,
              "--xml"};

      case RESOLVE_SERIES_INSTANCE_UIDS:

        return new String[]{
              "-r", PATIENT_ID_KEYWORD,
              "-r", PATIENT_NAME_KEYWORD,
              "-r", STUDY_INSTANCE_UI_KEYWORD,
              "-r", STUDY_DATE_KEYWORD,
              "-r", STUDY_DESCRIPTION_KEYWORD,
              "-r", SERIES_DESCRIPTION_KEYWORD,
              "-r", SERIES_INSTANCE_UID_KEYWORD,
              "-L", "SERIES",
              "-c", userName + "@" + server + ":" + port,
              "-m", STUDY_INSTANCE_UI_KEYWORD + "=" + argument,
              "--out-dir", tempDirectory,
              "--xml"};

      case FETCH_SERIES:

        return new String[]{
              "-L", "SERIES",
              "-c", userName + "@" + server + ":" + port,
              "-m", SERIES_INSTANCE_UID_KEYWORD + "=" + argument,
              "--store-tcs",
              getClass().getResource("/store-tcs.properties").toString(),
              "--directory", tempDirectory};

      default:
        throw new IllegalStateException();
    }
  }

  private String[] executeFindSCU(final File tempDirectory,
                                  final String... command)
        throws IOException, InterruptedException {

    return executeFindSCU(tempDirectory.toPath(), command);
  }

  private String[] executeFindSCU(final Path tempDirectory,
                                  final String... command)
        throws IOException, InterruptedException {

    final Consumer<Void> wrap =
          measuringConsumer(Void -> FindSCU.main(command));

    wrap.accept(null);

    return readFiles(tempDirectory);
  }

  private File[] executeFetchSCU(final File tempDirectory,
                                 final String... command)
        throws IOException, InterruptedException {

    final Consumer<Void> wrap =
          measuringConsumer(Void -> GetSCU.main(command));

    wrap.accept(null);

    final File[] outFileList = (
          tempDirectory.listFiles());

    assert outFileList != null;

    return outFileList;
  }

  private String[] readFiles(final Path directory) throws IOException {
    final File directoryFile = directory.toFile();

    final File[] dcmFiles = (
          directoryFile.listFiles(
                dcmFilter()));

    final String[] xmlContents = new String[dcmFiles.length];

    if (dcmFiles.length > 0) {

      int i = 0;

      for (final File dcmFile : dcmFiles) {

        xmlContents[i++] =
              new String(
                    readAllBytes(
                          dcmFile.toPath()));

      }
    }

    return xmlContents;
  }

  public final File createTempDirectory() throws IOException {

    final Path tempDirectory =
          java.nio.file.Files.createTempDirectory("integrationTest-temp-");

    return tempDirectory.toFile();

  }

  private void deleteOnExit(final File file) {

    file.deleteOnExit();
  }
}
