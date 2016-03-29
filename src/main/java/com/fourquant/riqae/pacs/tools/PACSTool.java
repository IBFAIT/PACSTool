package com.fourquant.riqae.pacs.tools;


import com.fourquant.riqae.pacs.Dcm4CheWrapper;
import com.fourquant.riqae.pacs.LoggingFunctionException;
import com.fourquant.riqae.pacs.csv.CSVDataRow;
import com.fourquant.riqae.pacs.csv.CSVReaderService;
import com.fourquant.riqae.pacs.csv.CSVWriterService;
import org.apache.commons.cli.ParseException;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.Set;

import static com.fourquant.riqae.pacs.tools.Operation.*;
import static java.lang.System.out;

/**
 * The {@code PACSTool} class contains the {@code main} method and
 * represents the point of access to the logic from the outer world
 * <p>
 * <blockquote><pre>
 *   java -jar PACS-Tool.jar -pn Verdi -pn Neri -s localhost -u OSIRIX -p 11112 -c RESOLVE_PATIENT_IDS
 * </pre></blockquote>
 * <blockquote><pre>
 *   java -jar PACS-Tool.jar -input-file test-classes/osirixNames.csv -s localhost -u OSIRIX -p 11112 -c RESOLVE_PATIENT_IDS
 * </pre></blockquote>
 * <blockquote><pre>
 *   java -jar PACS-Tool.jar -input-file test-classes/osirixNames.csv -s localhost -u OSIRIX -p 11112 -c RESOLVE_PATIENT_IDS -o /tmp/out.csv
 * </pre></blockquote>
 * <blockquote><pre>
 *   java -jar PACS-Tool.jar -input-file test-classes/osirixNamesAndIds.csv -s localhost -u OSIRIX -p 11112 -c RESOLVE_STUDY_INSTANCE_UIDS -o /tmp/out.csv
 * </pre></blockquote>
 */

final class PACSTool {

  private final static CSVReaderService csvReaderService =
        new CSVReaderService();

  private final static CSVWriterService csvWriterService =
        new CSVWriterService();

  public static void main(final String[] args)
        throws ParseException, ParserConfigurationException,
        SAXException, IOException, InterruptedException,
        LoggingFunctionException {

    final CommandLineProcessor commandLineProcessor =
          new CommandLineProcessor(args);

    if (!commandLineProcessor.callIsValid()) {
      commandLineProcessor.printHelp();
      return;
    }

    final String server = commandLineProcessor.getServer();
    final int port = commandLineProcessor.getPort();
    final String user = commandLineProcessor.getUser();
    final String outputFile = commandLineProcessor.getOutputFile();
    final String[] patientNames = commandLineProcessor.getPatientNames();
    final String inputFile = commandLineProcessor.getInputFile();
    final String commandString = commandLineProcessor.getCommand();
    final String bind = commandLineProcessor.getBind();

    final Operation command = valueOf(commandString);

    final Dcm4CheWrapper dcm4CheWrapper =
          new Dcm4CheWrapper(user, server, Integer.toString(port), bind);

    switch (command) {
      case RESOLVE_PATIENT_IDS:

        final Set<CSVDataRow> csvDataRowsInput;

        if (commandLineProcessor.inputFileSet()) {

          csvDataRowsInput = csvReaderService.createDataRows(inputFile);

        } else if (commandLineProcessor.patientNamesSet()) {

          csvDataRowsInput = csvReaderService.createDataRowsWithNames(patientNames);

        } else {
          throw new IllegalStateException();
        }

        final Set<CSVDataRow> nameAndIdDataRows =
              dcm4CheWrapper.execute(RESOLVE_PATIENT_IDS).on(csvDataRowsInput);

        if (commandLineProcessor.outputFileSet())
          csvWriterService.writeCSVFile(nameAndIdDataRows, outputFile);
        else
          csvWriterService.writeDataRows(nameAndIdDataRows, out);

        break;

      case RESOLVE_STUDY_INSTANCE_UIDS:

        csvDataRowsInput = csvReaderService.createDataRows(inputFile);

        final Set<CSVDataRow> studyInstanceUIDDataRows =
              dcm4CheWrapper.execute(RESOLVE_STUDY_INSTANCE_UIDS).
                    on(csvDataRowsInput);

        if (commandLineProcessor.outputFileSet())
          csvWriterService.writeCSVFile(studyInstanceUIDDataRows, outputFile);
        else
          csvWriterService.writeDataRows(studyInstanceUIDDataRows, out);

        break;

      case RESOLVE_SERIES_INSTANCE_UIDS:

        csvDataRowsInput = csvReaderService.createDataRows(inputFile);

        final Set<CSVDataRow> seriesInstanceUIDDataRows =
              dcm4CheWrapper.execute(RESOLVE_SERIES_INSTANCE_UIDS).
                    on(csvDataRowsInput);

        if (commandLineProcessor.outputFileSet())
          csvWriterService.writeCSVFile(seriesInstanceUIDDataRows, outputFile);
        else
          csvWriterService.writeDataRows(seriesInstanceUIDDataRows, out);

        break;

      default:
        throw new IllegalStateException();
    }
  }
}