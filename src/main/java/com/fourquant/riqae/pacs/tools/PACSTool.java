package com.fourquant.riqae.pacs.tools;


import com.fourquant.riqae.pacs.SCUOperationWrapper;
import com.fourquant.riqae.pacs.csv.CSVDataRow;
import com.fourquant.riqae.pacs.csv.CSVReaderService;
import com.fourquant.riqae.pacs.csv.CSVWriterService;
import org.apache.commons.cli.ParseException;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.Set;

import static com.fourquant.riqae.pacs.tools.Operation.valueOf;

public final class PACSTool {

  public static void main(final String[] args)
        throws ParseException, ParserConfigurationException,
        SAXException, IOException, InterruptedException {

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
    final String patientNamesFile = commandLineProcessor.getPatientnamesfile();
    final String[] patientNames = commandLineProcessor.getPatientNames();
    final String inputFile = commandLineProcessor.getInputFile();
    final String commandString = commandLineProcessor.getCommand();

    final Operation command = valueOf(commandString);

    final SCUOperationWrapper scuOperationWrapper =
          new SCUOperationWrapper(user, server, Integer.toString(port));


    switch (command) {
      case RESOLVE_PATIENT_IDS:

        System.out.println("-> A");

        final CSVReaderService csvReaderService = new CSVReaderService();

        final CSVWriterService csvWriterService = new CSVWriterService();


        final Set<CSVDataRow> csvDataRowsInput;

        if (commandLineProcessor.patientNamesFileSet()) {
          csvDataRowsInput = csvReaderService.createDataRows(patientNamesFile);
        } else if (commandLineProcessor.patientNamesSet()) {
          csvDataRowsInput = csvReaderService.createDataRowsWithNames(patientNames);
        } else {
          throw new IllegalStateException();
        }


        break;

      case RESOLVE_STUDY_INSTANCE_UIDS:

        System.out.println("-> B");
        break;

      case RESOLVE_SERIES_INSTANCE_UIDS:

        System.out.println("-> C");
        break;

      case FETCH_SERIES:

        System.out.println("-> D");
        break;

      default:
        throw new IllegalStateException();
    }
  }
}