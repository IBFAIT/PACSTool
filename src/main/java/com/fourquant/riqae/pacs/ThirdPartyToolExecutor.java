package com.fourquant.riqae.pacs;

import java.io.IOException;

public interface ThirdPartyToolExecutor {

  /**
   * Executes a 3rd party tool
   * Returns the output of the 3rd party tool
   *
   * @param command The actual command
   *                (e.g. findscu -c DCMQRSCP@localhost:11112 -r PatientID -m PatientName=John^Doe)
   */
  String[] execute(String command) throws IOException;
}