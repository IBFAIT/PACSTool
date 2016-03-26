package com.fourquant.riqae.pacs.tools;

import org.apache.commons.cli.Options;

import static com.fourquant.riqae.pacs.tools.Operation.RESOLVE_PATIENT_IDS;
import static org.apache.commons.cli.Option.builder;

public class OptionsFactory {

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

  public static final String optInputFile = "i";
  public static final String longOptInputFile = "input-file";

  public static final String optCommand = "c";
  public static final String longOptCommand = "command";

  public static final String optHelp = "h";
  public static final String longOptHelp = "help";

  public static final String commandResolvePatientIDs =
        RESOLVE_PATIENT_IDS.toString();

  public static Options createOptions() {
    final Options options = new Options();

    options.addOption(builder(optServer)
          .hasArg()
          .argName("ip")
          .required(false)
          .longOpt(longOptServer)
          .desc("PACS server IP address")
          .build());

    options.addOption(builder(optPort)
          .hasArg()
          .argName("port")
          .required(false)
          .longOpt(longOptPort)
          .desc("PACS server port number")
          .build());

    options.addOption(builder(optUser)
          .hasArg()
          .argName("username")
          .required(false)
          .longOpt(longOptUser)
          .desc("PACS server user")
          .build());

    options.addOption(builder(optPatientName)
          .hasArg()
          .argName("patientname")
          .required(false)
          .longOpt(longOptPatientName)
          .desc("Patient name")
          .build());

    options.addOption(builder(optPatientNamesFile)
          .hasArg()
          .argName("patientnamesfile")
          .required(false)
          .longOpt(longOptPatientNamesFile)
          .desc("Patient names file")
          .build());

    options.addOption(builder(optOutputFile)
          .hasArg()
          .argName("file")
          .required(false)
          .longOpt(longOptOutputFile)
          .desc("Output file name")
          .build());

    options.addOption(builder(optInputFile)
          .hasArg()
          .argName("file")
          .required(false)
          .longOpt(longOptInputFile)
          .desc("Input file name")
          .build());

    options.addOption(builder(optCommand)
          .hasArg()
          .argName("command")
          .required(false)
          .longOpt(longOptCommand)
          .desc("Command")
          .build());

    options.addOption(builder(optHelp)
          .required(false)
          .longOpt(longOptHelp)
          .desc("This help message")
          .build());
    return options;
  }
}
