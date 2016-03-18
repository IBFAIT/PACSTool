package com.fourquant.riqae.pacs.tools;

import org.apache.commons.cli.*;

import static com.fourquant.riqae.pacs.tools.OptionsFactory.*;
import static java.lang.Integer.parseInt;

public final class CommandLineProcessor {

  public static final int PACS_SERVER_PORT_DEFAULT = 9090;
  public static final String PACS_SERVER_ADDRESS_DEFAULT = "localhost";
  public static final String PACS_SERVER_USER_DEFAULT = "john";

  final CommandLine line;
  final Options options;

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

  private boolean inputFileSet() {
    return line.hasOption(optInputFile);
  }

  private boolean outputFileSet() {
    return line.hasOption(optOutputFile);
  }

  private boolean isHelp() {
    return line.hasOption(optHelp);
  }

  private boolean hasPatientNames() {
    return line.hasOption(
          optPatientName) || line.hasOption(optPatientNamesFile);
  }

  private boolean hasCommand() {
    return line.hasOption(optCommand);
  }

  public String[] getPatientNames() {
    return line.getOptionValues(optPatientName);
  }

  public String getPatientnamesfile() {
    return line.getOptionValue(optPatientNamesFile);
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

  private boolean has(final String parameter) {
    return line.hasOption(parameter);
  }

  public boolean callIsValid() {
    return !isHelp() && hasPatientNames() && hasCommand();
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