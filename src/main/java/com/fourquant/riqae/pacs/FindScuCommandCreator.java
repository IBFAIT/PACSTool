package com.fourquant.riqae.pacs;

import org.apache.commons.cli.*;

import static java.lang.Integer.parseInt;

public class FindScuCommandCreator {

  public static final int PACS_SERVER_PORT_DEFAULT = 9090;
  public static final String PACS_SERVER_ADDRESS_DEFAULT = "localhost";
  public static final String PACS_SERVER_USER_DEFAULT = "john";

  public static final String optPort = "p";
  public static final String longOptPort = "port";

  public static final String optServer = "s";
  public static final String longOptServer = "server";

  public static final String optUser = "u";
  public static final String longOptUser = "user";

  public static final String optPatientName = "pn";
  public static final String longOptPatientName = "patient-name";

  public static final String optBinaryPath = "bp";
  public static final String longOptBinaryPath = "binaryPath";

  public static final String optHelp = "h";
  public static final String longOptHelp = "help";

  public static void main(final String[] args) throws ParseException {
    final CommandLineProcessor clp = new CommandLineProcessor(args);
    if (!clp.callIsValid()) {
      clp.printHelp();
      return;
    }

    final String server = clp.getServer();
    final int port = clp.getPort();
    final String user = clp.getUser();
    final String[] patientNames = clp.getPatientNames();
    final String binaryPath = clp.getBinaryPath();

    final String[] findScuStatements =
          new FindScuCommandCreator().createFindScuStatements(
                patientNames, user, server, Integer.toString(port), binaryPath);

    for (final String findScuStatement : findScuStatements) {
      System.out.println(findScuStatement);
    }
  }

  public final String[] createFindScuStatements(
        final String[] patientNames, final String user,
        final String server, final String port, final String binaryPath) {

    final String[] findScuCommands = new String[patientNames.length];

    for (int i = 0; i < patientNames.length; i++) {
      findScuCommands[i] = createFindScuStatement(
            patientNames[i], user, server, port, binaryPath);
    }
    return findScuCommands;
  }

  public final String createFindScuStatement(
        final String patientName, final String user,
        final String server, final String port, final String binaryPath) {
    return binaryPath + " -r PatientID -c " + user + "@" + server + ":" + port +
          " -m PatientName=" + patientName + "";
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

    public String getBinaryPath() {
      final String binaryPath;
      if (has(optBinaryPath))
        binaryPath = line.getOptionValue(optBinaryPath);
      else
        throw new IllegalStateException();

      return binaryPath;
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

    private boolean has(final String parameter) {
      return line.hasOption(parameter);
    }

    public boolean callIsValid() {
      return !isHelp() && hasPatientNames();
    }

    public void printHelp() {
      new HelpFormatter().
            printHelp("FindScuCommandCreator", options);
    }

    private boolean isHelp() {
      return line.hasOption(optHelp);
    }

    private boolean hasPatientNames() {
      return line.hasOption(optPatientName);
    }

    public String[] getPatientNames() {
      return line.getOptionValues(optPatientName);
    }
  }

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

      options.addOption(Option.builder(optBinaryPath)
            .hasArg()
            .argName("binaryPath")
            .required(true)
            .longOpt(longOptBinaryPath)
            .desc("Binary Path")
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
