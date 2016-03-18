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
    final String command = commandLineProcessor.getCommand();


    switch (command) {
      case "a":
        System.out.println("-> A");
        break;
      case "b":
        System.out.println("-> B");
        break;
      case "c":
        System.out.println("-> C");
        break;
    }

    final CSVReaderService csvReaderService = new CSVReaderService();

    final SCUOperationWrapper scuOperationWrapper =
          new SCUOperationWrapper(user, server, Integer.toString(port));

    final CSVWriterService csvWriterService = new CSVWriterService();


    final Set<CSVDataRow> csvDataRowsInput;

    if (commandLineProcessor.patientNamesFileSet()) {
      csvDataRowsInput = csvReaderService.createDataRows(patientNamesFile);
    } else if (commandLineProcessor.patientNamesSet()) {
      csvDataRowsInput = csvReaderService.createDataRowsWithNames(patientNames);
    } else {
      throw new IllegalStateException();
    }


  }


}