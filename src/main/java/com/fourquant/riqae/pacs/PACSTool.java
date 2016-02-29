package com.fourquant.riqae.pacs;


import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;

import java.util.List;
import java.util.logging.Logger;

public final class PACSTool {
  public static final int PACS_SERVER_PORT_DEFAULT = 9090;
  public static final String PACS_SERVER_ADDRESS_DEFAULT = "localhost";
  public static final String PACS_SERVER_USER_DEFAULT = "admin";

  private static final Logger log =
        Logger.getLogger(PACSTool.class.getName());

  @Parameter(
        names = {"-server", "-s"},
        description = "PACS server IP address",
        required = false)
  private String server = PACS_SERVER_ADDRESS_DEFAULT;

  @Parameter(
        names = {"-port", "-p"},
        description = "PACS server port number",
        required = false)
  private int port = PACS_SERVER_PORT_DEFAULT;

  @Parameter(
        names = {"-user", "-u"},
        description = "PACS server user",
        required = false)
  private String user = PACS_SERVER_USER_DEFAULT;


  @Parameter(
        names = {"-patient-name", "-pn"},
        description = "Patient name",
        required = false)
  private List<String> patientNames;

  @Parameter(
        names = {"-patient-names-file", "-pnf"},
        description = "Patient names file",
        required = false)
  private String patientNamesFile;

  @Parameter(
        names = {"-output-file", "-o"},
        description = "Output file name",
        required = false)
  private String outputFileName;

  @Parameter(
        names = {"-h", "--help"},
        description = "This usage summary",
        help = true,
        required = false)
  private boolean help;

  private JCommander jCommander;
  private PACSFacade pacsFacade;

  public PACSTool(String[] args) {
    jCommander = new JCommander(this, args);
    pacsFacade = new PACSFacade(server, port, user);
  }

  private PACSTool() {
  }

  public static void main(String[] args) {
    final PACSTool pacsTool = new PACSTool(args);
    pacsTool.run();
  }

  private void run() {
    if (help) {
      jCommander.usage();
      return;
    }

    final MessageFactory factory = new MessageFactory();

    final Message pacsRequest;

    if (patientNamesFileSet()) {
      pacsRequest = factory.create(patientNamesFile);
    } else {
      pacsRequest = factory.create(patientNames);
    }

    final Message pacsResponse = process(pacsRequest);

    final MessageWriter writer = new MessageWriter();
    if (outputFileName != null)
      writer.write(pacsResponse, outputFileName);
    else
      writer.write(pacsResponse);
  }

  private boolean patientNamesFileSet() {
    return patientNamesFile != null;
  }

  private Message process(final Message pacsRequest) {
    return pacsFacade.process(pacsRequest);
  }

}