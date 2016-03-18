package com.fourquant.riqae.pacs.tools;


import com.fourquant.riqae.pacs.SCUOperationWrapper;
import com.fourquant.riqae.pacs.csv.CSVDataRow;
import com.fourquant.riqae.pacs.csv.CSVReaderService;
import com.fourquant.riqae.pacs.csv.CSVWriterService;
import org.apache.commons.cli.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.Set;

import static java.lang.Integer.parseInt;

public final class PACSTool {
  public static final int PACS_SERVER_PORT_DEFAULT = 9090;
  public static final String PACS_SERVER_ADDRESS_DEFAULT = "localhost";
  public static final String PACS_SERVER_USER_DEFAULT = "john";

  public static void main(final String[] args)
        throws ParseException, ParserConfigurationException,
        SAXException, IOException, InterruptedException {

    final CommandLineProcessor clp = new CommandLineProcessor(args);
    if (!clp.callIsValid()) {
      clp.printHelp();
      return;
    }

    final String server = clp.getServer();
    final int port = clp.getPort();
    final String user = clp.getUser();
    final String outputFile = clp.getOutputFile();
    final String patientNamesFile = clp.getPatientnamesfile();
    final String[] patientNames = clp.getPatientNames();

    final Set<CSVDataRow> csvDataRowsInput;
    final CSVReaderService csvReaderService = new CSVReaderService();

    if (clp.patientNamesFileSet()) {
      csvDataRowsInput = csvReaderService.createDataRows(patientNamesFile);
    } else if (clp.patientNamesSet()) {
      csvDataRowsInput = csvReaderService.createDataRowsWithNames(patientNames);
    } else {
      throw new IllegalStateException();
    }

    final SCUOperationWrapper scuOperationWrapper = new SCUOperationWrapper(null, null, null);


//    final Set<CSVDataRow> CSVDataRowsOutput = pacsFacade.process(csvDataRowsInput);

    final CSVWriterService csvWriterService = new CSVWriterService();

    if (clp.outputFileSet()) {
//      csvWriterService.writeDataRows(CSVDataRowsOutput, outputFile);
    } else {
//      csvWriterService.writeDataRows(CSVDataRowsOutput);
    }
  }

  static final class CommandLineProcessor {
    final CommandLine line;
    final Options options;

    public CommandLineProcessor(final String[] args) throws ParseException {
      options = OptionsFactory.createOptions();
      final CommandLineParser parser = new DefaultParser();
      line = parser.parse(options, args);
    }

    public String getServer() {

      final String server;
      if (has(OptionsFactory.optServer))
        server = line.getOptionValue(OptionsFactory.optServer);
      else
        server = PACS_SERVER_ADDRESS_DEFAULT;

      return server;
    }

    public int getPort() {
      final int port;
      if (has(OptionsFactory.optPort))
        port = parseInt(line.getOptionValue(OptionsFactory.optPort));
      else
        port = PACS_SERVER_PORT_DEFAULT;

      return port;
    }

    public String getUser() {

      final String user;
      if (has(OptionsFactory.optUser))
        user = line.getOptionValue(OptionsFactory.optUser);
      else
        user = PACS_SERVER_USER_DEFAULT;

      return user;
    }

    private boolean outputFileSet() {
      return line.hasOption(OptionsFactory.optOutputFile);
    }

    private boolean isHelp() {
      return line.hasOption(OptionsFactory.optHelp);
    }

    private boolean hasPatientNames() {
      return line.hasOption(OptionsFactory.optPatientName) || line.hasOption(OptionsFactory.optPatientNamesFile);
    }

    public String[] getPatientNames() {
      return line.getOptionValues(OptionsFactory.optPatientName);
    }

    public String getPatientnamesfile() {
      return line.getOptionValue(OptionsFactory.optPatientNamesFile);
    }

    public String getOutputFile() {
      return line.getOptionValue(OptionsFactory.optOutputFile);
    }

    private boolean has(final String parameter) {
      return line.hasOption(parameter);
    }

    public boolean callIsValid() {
      return !isHelp() && hasPatientNames();
    }

    public void printHelp() {
      new HelpFormatter().
            printHelp("PACSTool", options);
    }

    public boolean patientNamesFileSet() {
      return line.hasOption(OptionsFactory.optPatientNamesFile);
    }

    public boolean patientNamesSet() {
      return line.hasOption(OptionsFactory.optPatientName);
    }
  }

}