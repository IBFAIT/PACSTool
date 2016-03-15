package com.fourquant.riqae.pacs.tools;

import org.apache.commons.cli.*;

import static java.lang.Integer.parseInt;

public class FindScuCommandCreator {

  public static final int PACS_SERVER_PORT_DEFAULT = 9090;
  public static final String PACS_SERVER_ADDRESS_DEFAULT = "localhost";
  public static final String PACS_SERVER_USER_DEFAULT = "john";

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
          " -m PatientName=\"" + patientName + "\"";
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

    public String getBinaryPath() {
      final String binaryPath;
      if (has(OptionsFactory.optBinaryPath))
        binaryPath = line.getOptionValue(OptionsFactory.optBinaryPath);
      else
        throw new IllegalStateException();

      return binaryPath;
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
      return line.hasOption(OptionsFactory.optHelp);
    }

    private boolean hasPatientNames() {
      return line.hasOption(OptionsFactory.optPatientName);
    }

    public String[] getPatientNames() {
      return line.getOptionValues(OptionsFactory.optPatientName);
    }
  }

}
