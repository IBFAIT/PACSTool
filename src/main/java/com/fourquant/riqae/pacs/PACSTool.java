package com.fourquant.riqae.pacs;


import org.apache.commons.cli.*;

import java.util.logging.Logger;

public final class PACSTool {
  public static final int PACS_SERVER_PORT_DEFAULT = 9090;
  public static final String PACS_SERVER_ADDRESS_DEFAULT = "localhost";
  public static final String PACS_SERVER_USER_DEFAULT = "john";

  private static final Logger log =
        Logger.getLogger(PACSTool.class.getName());

  private final PACSFacade pacsFacade;

  public PACSTool(final PACSFacade pacsFacade) {
    this.pacsFacade = pacsFacade;
  }

  public static void main(final String[] args) throws ParseException {

    final Options options = OptionsFactory.createOptions();
    final CommandLineParser parser = new DefaultParser();
    final CommandLine line = parser.parse(options, args);

    if (!hasPatientNames(line) || isHelp(line)) {
      new HelpFormatter().
            printHelp("PACSTool", options);
      return;
    }

    final String server;
    if (has("s", line))
      server = line.getOptionValue("s");
    else
      server = PACS_SERVER_ADDRESS_DEFAULT;

    final int port;
    if (has("p", line))
      port = Integer.parseInt(line.getOptionValue("p"));
    else
      port = PACS_SERVER_PORT_DEFAULT;

    final String user;
    if (has("u", line))
      user = line.getOptionValue("u");
    else
      user = PACS_SERVER_USER_DEFAULT;

    final PACSTool pacsTool =
          new PACSTool(PACSFacadeFactory.createFactory(server, port, user));

    final Message pacsRequest;
    if (line.hasOption("pnf")) {
      pacsRequest = RequestFactory.createRequest(line.getOptionValue("pnf"));
    } else if (line.hasOption("pn")) {
      pacsRequest = RequestFactory.createRequest(line.getOptionValues("pn"));

    } else {
      new HelpFormatter().
            printHelp("PACSTool", options);
      return;
    }

    final Message pacsResponse = pacsTool.process(pacsRequest);
    if (outputFileSet(line))
      ResponseWriter.write(pacsResponse, line.getOptionValue("o"));
    else
      ResponseWriter.write(pacsResponse);
  }

  private static boolean outputFileSet(final CommandLine line) {
    return line.hasOption("o");
  }

  private static boolean isHelp(final CommandLine line) {
    return line.hasOption("h");
  }

  private static boolean hasPatientNames(final CommandLine line) {
    return line.hasOption("pn") || line.hasOption("pnf");
  }

  private static boolean has(final String parameter, final CommandLine line) {
    return line.hasOption(parameter);
  }

  private Message process(final Message pacsRequest) {
    return pacsFacade.process(pacsRequest);
  }

  static final class ResponseWriter {
    static void write(final Message pacsResponse,
                      final String outputFileName) {

      final MessageWriter writer = new MessageWriter();
      writer.write(pacsResponse, outputFileName);
    }

    static void write(
          final Message pacsResponse) {

      final MessageWriter writer = new MessageWriter();
      writer.write(pacsResponse);
    }
  }

  static final class PACSFacadeFactory {

    static PACSFacade createFactory(
          final String server,
          final int port,
          final String user) {
      return new PACSFacade(server, port, user);
    }
  }

  static final class RequestFactory {
    final static MessageFactory factory = new MessageFactory();

    static Message createRequest(final String filePath) {
      return factory.create(filePath);
    }

    static Message createRequest(final String[] patientNames) {
      return factory.create(patientNames);
    }

  }

  //todo introduce optiongroups
  static final class OptionsFactory {
    static Options createOptions() {
      final Options options = new Options();

      options.addOption(Option.builder("s")
            .hasArg()
            .argName("ip")
            .required(false)
            .longOpt("server")
            .desc("PACS server IP address")
            .build());

      options.addOption(Option.builder("p")
            .hasArg()
            .argName("port")
            .required(false)
            .longOpt("port")
            .desc("PACS server port number")
            .build());

      options.addOption(Option.builder("u")
            .hasArg()
            .argName("username")
            .required(false)
            .longOpt("user")
            .desc("PACS server user")
            .build());

      options.addOption(Option.builder("pn")
            .hasArg()
            .argName("patientname")
            .required(false)
            .longOpt("patient-name")
            .desc("Patient name")
            .build());

      options.addOption(Option.builder("pnf")
            .hasArg()
            .argName("patientnamesfile")
            .required(false)
            .longOpt("patient-names-file")
            .desc("Patient names file")
            .build());

      options.addOption(Option.builder("o")
            .hasArg()
            .argName("file")
            .required(false)
            .longOpt("output-file")
            .desc("Output file name")
            .build());

      options.addOption(Option.builder("h")
            .required(false)
            .longOpt("help")
            .desc("This help message")
            .build());
      return options;
    }
  }
}