package com.fourquant.riqae.pacs;


import org.apache.commons.cli.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.List;

import static com.fourquant.riqae.pacs.PACSTool.RequestFactory.createRequest;
import static java.lang.Integer.parseInt;

public final class PACSTool {

  public static final int PACS_SERVER_PORT_DEFAULT = 9090;
  public static final String PACS_SERVER_ADDRESS_DEFAULT = "localhost";
  public static final String PACS_SERVER_USER_DEFAULT = "john";

  public static final String optPort = "p";
  public static final String longOptPort = "port";

  public static final String optServer = "s";
  public static final String longOptServer = "server";

  public static final String optUser = "u";
  public static final String longOptUser = "user";

  public static final String optOutputFile = "o";
  public static final String longOptOutputFile = "output-file";

  public static final String optPatientName = "pn";
  public static final String longOptPatientName = "patient-name";

  public static final String optPatientNamesFile = "pnf";
  public static final String longOptPatientNamesFile = "patient-names-file";

  public static final String optHelp = "h";
  public static final String longOptHelp = "help";

  public static void main(final String[] args)
        throws ParseException, ParserConfigurationException, SAXException, IOException {

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

    final List<DataRow> pacsRequest;

    if (clp.patientNamesFileSet()) {
      pacsRequest = createRequest(patientNamesFile);
    } else if (clp.patientNamesSet()) {
      pacsRequest = createRequest(patientNames);
    } else {
      throw new IllegalStateException();
    }

    final DefaultPACSFacade pacsFacade = new DefaultPACSFacade(server, port, user);

    pacsFacade.setThirdPartyToolExecutor(
          new DummyThirdPartyToolExecutor(
                new String[]{"donatella.xml", "kate.xml", "ashlee.xml"}));

    final List<DataRow> pacsResponse = pacsFacade.process(pacsRequest);

    if (clp.outputFileSet()) {
      ResponseWriter.write(pacsResponse, outputFile);
    } else
      ResponseWriter.write(pacsResponse);
  }

  static final class ResponseWriter {
    static void write(final List<DataRow> dataRows,
                      final String outputFileName) {

      final CSVDocWriter writer = new CSVDocWriter();
      writer.write(dataRows, outputFileName);
    }

    static void write(final List<DataRow> dataRows) throws IOException {

      final CSVDocWriter writer = new CSVDocWriter();
      writer.write(dataRows);
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
      if (has(optServer))
        server = line.getOptionValue(optServer);
      else
        server = PACS_SERVER_ADDRESS_DEFAULT;

      return server;
    }

    public int getPort() {
      final int port;
      if (has(optPort))
        port = parseInt(line.getOptionValue(optPort));
      else
        port = PACS_SERVER_PORT_DEFAULT;

      return port;
    }

    public String getUser() {

      final String user;
      if (has(optUser))
        user = line.getOptionValue(optUser);
      else
        user = PACS_SERVER_USER_DEFAULT;

      return user;
    }

    private boolean outputFileSet() {
      return line.hasOption(optOutputFile);
    }

    private boolean isHelp() {
      return line.hasOption(optHelp);
    }

    private boolean hasPatientNames() {
      return line.hasOption(optPatientName) || line.hasOption(optPatientNamesFile);
    }

    public String[] getPatientNames() {
      return line.getOptionValues(optPatientName);
    }

    public String getPatientnamesfile() {
      return line.getOptionValue(optPatientNamesFile);
    }

    public String getOutputFile() {
      return line.getOptionValue(optOutputFile);
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
      return line.hasOption(optPatientNamesFile);
    }

    public boolean patientNamesSet() {
      return line.hasOption(optPatientName);
    }
  }

  static final class RequestFactory {
    final static DataRowFactory factory = new DataRowFactory();

    static List<DataRow> createRequest(final String filePath) {
      return factory.create(filePath);
    }

    static List<DataRow> createRequest(final String[] patientNames) {
      return factory.create(patientNames);
    }
  }

  //todo introduce optiongroups
  static final class OptionsFactory {
    static Options createOptions() {
      final Options options = new Options();

      options.addOption(Option.builder(optServer)
            .hasArg()
            .argName("ip")
            .required(false)
            .longOpt(longOptServer)
            .desc("PACS server IP address")
            .build());

      options.addOption(Option.builder(optPort)
            .hasArg()
            .argName("port")
            .required(false)
            .longOpt(longOptPort)
            .desc("PACS server port number")
            .build());

      options.addOption(Option.builder(optUser)
            .hasArg()
            .argName("username")
            .required(false)
            .longOpt(longOptUser)
            .desc("PACS server user")
            .build());

      options.addOption(Option.builder(optPatientName)
            .hasArg()
            .argName("patientname")
            .required(false)
            .longOpt(longOptPatientName)
            .desc("Patient name")
            .build());

      options.addOption(Option.builder(optPatientNamesFile)
            .hasArg()
            .argName("patientnamesfile")
            .required(false)
            .longOpt(longOptPatientNamesFile)
            .desc("Patient names file")
            .build());

      options.addOption(Option.builder(optOutputFile)
            .hasArg()
            .argName("file")
            .required(false)
            .longOpt(longOptOutputFile)
            .desc("Output file name")
            .build());

      options.addOption(Option.builder(optHelp)
            .required(false)
            .longOpt(longOptHelp)
            .desc("This help message")
            .build());
      return options;
    }
  }
}