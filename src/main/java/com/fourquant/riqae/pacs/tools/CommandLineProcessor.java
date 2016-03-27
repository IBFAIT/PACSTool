package com.fourquant.riqae.pacs.tools;

import org.apache.commons.cli.*;

import static com.fourquant.riqae.pacs.tools.OptionsFactory.*;
import static java.lang.Integer.parseInt;

final class CommandLineProcessor {

  private static final int PACS_SERVER_PORT_DEFAULT = 9090;
  private static final String PACS_SERVER_ADDRESS_DEFAULT = "localhost";
  private static final String PACS_SERVER_USER_DEFAULT = "john";

  private final CommandLine line;
  private final Options options;

  public CommandLineProcessor(final String[] args) throws ParseException {

    options = createOptions();

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

  public boolean inputFileSet() {
    return line.hasOption(optInputFile);
  }

  public boolean outputFileSet() {
    return line.hasOption(optOutputFile);
  }

  public boolean isHelp() {
    return line.hasOption(optHelp);
  }

  public boolean hasCommand() {
    return line.hasOption(optCommand);
  }

  public String[] getPatientNames() {
    return line.getOptionValues(optPatientName);
  }

  public String getInputFile() {
    return line.getOptionValue(optInputFile);
  }

  public String getOutputFile() {
    return line.getOptionValue(optOutputFile);
  }

  public String getCommand() {
    return line.getOptionValue(optCommand);
  }

  public boolean has(final String parameter) {
    return line.hasOption(parameter);
  }

  public boolean callIsValid() {
    return !isHelp() && hasCommand();
  }

  public void printHelp() {
    new HelpFormatter().
          printHelp("PACSTool", options);
  }

  public boolean patientNamesSet() {
    return line.hasOption(optPatientName);
  }
}