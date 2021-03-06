package com.fourquant.riqae.pacs;

import com.fourquant.riqae.pacs.csv.CSVDataRow;
import com.fourquant.riqae.pacs.tools.Operation;
import org.dcm4che3.tool.findscu.FindSCU;
import org.dcm4che3.tool.getscu.GetSCU;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.file.Path;
import java.text.DecimalFormat;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

import static com.fourquant.riqae.pacs.Dcm4CheWrapperHelper.*;
import static com.fourquant.riqae.pacs.csv.CSVProtocol.*;
import static com.fourquant.riqae.pacs.csv.XML2CSVConverterService.convert;
import static com.fourquant.riqae.pacs.tools.Operation.FETCH_SERIES;
import static java.math.RoundingMode.CEILING;

public final class Dcm4CheWrapper {

  private static final Logger log =
        Logger.getLogger(Dcm4CheWrapper.class.getName());

  private final String userName;
  private final String server;
  private final String port;
  private final String bind;

  public Dcm4CheWrapper(final String userName, final String server,
                        final String port, final String bind) {

    this.userName = userName;
    this.server = server;
    this.port = port;
    this.bind = bind;
  }

  public File[] fetchSeries(final String seriesInstanceUID)
        throws IOException, LoggingFunctionException {

    final File tempDirectory = createTempDirectory();

    final String[] scuArguments = createSCUArguments(
          seriesInstanceUID, tempDirectory, FETCH_SERIES);

    final File[] files;

    files = executeGetSCU(tempDirectory, scuArguments);

    float sizeOfDirectory = sizeOfDirectory(tempDirectory.toPath());

    final DecimalFormat decimalFormat = new DecimalFormat("###,###.###");
    decimalFormat.setRoundingMode(CEILING);

    log.info("size of " + tempDirectory.getAbsolutePath() + " is " +
          decimalFormat.format(sizeOfDirectory) + " bytes, " +
          decimalFormat.format(sizeOfDirectory / 1_024) + " KB, " +
          decimalFormat.format(sizeOfDirectory / 1_024 / 1_024) + " MB.");

    deleteOnExit(tempDirectory);

    return files;

  }

  public void execute(final Method method,
                      final String... command) throws LoggingFunctionException {

    final Object[] args = {command};

    final LoggingFunction loggingFunction = new LoggingFunction();

    final Float duration = loggingFunction.execute(method, args);

    final StringBuilder stringBuilder = new StringBuilder();
    for (final String cmd : command) {
      stringBuilder.append(cmd);
      stringBuilder.append(" ");
    }

    log.info(
          "executing " + method.getName() + " on " +
                method.getDeclaringClass().getName() + " with args " +
                stringBuilder.toString() + " took " + duration);

  }

  public String[] executeFindSCU(final Path tempDirectory,
                                 final String... command)
        throws IOException, LoggingFunctionException {

    final Method method;

    try {

      method = FindSCU.class.getMethod("main", String[].class);

      execute(method, command);

      return readFiles(tempDirectory, dcmFilter());

    } catch (NoSuchMethodException e) {
      throw new LoggingFunctionException(e);
    }
  }

  public File[] executeGetSCU(final File tempDirectory,
                              final String... command)
        throws LoggingFunctionException {

    final Method method;

    try {

      method = GetSCU.class.getMethod("main", String[].class);

      execute(method, command);

      final File[] outFileList = (
            tempDirectory.listFiles());

      assert outFileList != null;

      return outFileList;

    } catch (NoSuchMethodException e) {
      throw new LoggingFunctionException(e);
    }
  }

  public ExecutionWrapper execute(final Operation operation) {
    return new ExecutionWrapper(operation);
  }

  private String[] createSCUArguments(final String argument,
                                      final File tempDirectory,
                                      final Operation op) {

    return createSCUArguments(argument, tempDirectory.getAbsolutePath(), op);
  }

  private String[] createSCUArguments(final String argument,
                                      final String tempDirectory,
                                      final Operation operation) {

    switch (operation) {
      case RESOLVE_PATIENT_IDS:

        return new String[]{"-r", PATIENT_ID_KEYWORD,
              "-c", userName + "@" + server + ":" + port,
              "-b", bind,
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
              "-b", bind,
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
              "-b", bind,
              "-m", STUDY_INSTANCE_UI_KEYWORD + "=" + argument,
              "--out-dir", tempDirectory,
              "--xml"};

      case FETCH_SERIES:

        return new String[]{
              "-L", "SERIES",
              "-c", userName + "@" + server + ":" + port,
              "-b", bind,
              "-m", SERIES_INSTANCE_UID_KEYWORD + "=" + argument,
              "--store-tcs",
              getClass().getResource("/store-tcs.properties").toString(),
              "--directory", tempDirectory};

      default:
        throw new IllegalStateException();
    }
  }

  public class ExecutionWrapper {
    private final Operation operation;

    ExecutionWrapper(final Operation operation) {
      this.operation = operation;
    }

    String[] on(final String argument)
          throws LoggingFunctionException, IOException {

      final File tempDirectory = createTempDirectory();

      final String[] arguments = createSCUArguments(
            argument, tempDirectory, operation);

      final String[] xmlResults =
            executeFindSCU(tempDirectory.toPath(), arguments);

      deleteOnExit(tempDirectory);

      return xmlResults;

    }

    public Set<CSVDataRow> on(final Set<CSVDataRow> inputCSVDataRows)
          throws LoggingFunctionException, IOException {

      try {

        final Set<CSVDataRow> outputCSVDataRows = new HashSet<>();

        for (final CSVDataRow row : inputCSVDataRows) {

          final String id;

          switch (operation) {
            case RESOLVE_PATIENT_IDS:

              id = row.getPatientName();
              break;

            case RESOLVE_STUDY_INSTANCE_UIDS:

              id = row.getPatientID();
              break;

            case RESOLVE_SERIES_INSTANCE_UIDS:

              id = row.getStudyInstanceUID();
              break;

            default:
              id = null;
              assert false;
          }

          final String[] xmlResults =
                execute(operation).on(id);

          outputCSVDataRows.addAll(
                convert(xmlResults));

        }

        return outputCSVDataRows;

      } catch (ParserConfigurationException | SAXException e) {
        throw new IOException(e);
      }

    }
  }
}