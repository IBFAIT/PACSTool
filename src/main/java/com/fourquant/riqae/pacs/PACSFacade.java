package com.fourquant.riqae.pacs;

import com.fourquant.riqae.pacs.Message.Row;

import java.util.logging.Logger;

import static java.lang.Math.random;

public final class PACSFacade {
  private static final Logger log =
        Logger.getLogger(PACSFacade.class.getName());

  private String server;

  private int port;

  public PACSFacade(String server, int port) {
    this.server = server;
    this.port = port;
  }

  public final String getServer() {
    return server;
  }

  public final int getPort() {
    return port;
  }

  /*
    Processing faked
    Does not establish no connection to no PACS server at the moment
    Populates responseMessage with random values
   */
  public final Message process(final Message request) {

    log.fine("connecting to PACS (" + getServer() + ":" + getPort() + ")");

    final Message response = new Message();
    log.fine("response object created");

    log.fine("populating response object...");
    for (final Row requestRow : request) {
      final int patientId = randomInt();
      do {
        Row row = new Row();
        row.setPatientName(requestRow.getPatientName());
        row.setPatientId(patientId);
        row.setExamId(randomInt());
        row.setSeriesId(randomInt());
        row.setTechnique(randomTechnique());
        row.setType(randomType());

        response.add(row);
      } while (((int) (random() * 2)) > 0);
    }

    log.fine("population done");

    return response;
  }

  private String randomType() {
    return ((int) (random() * 2)) > 0 ? "Thorax" : "Breast";
  }

  private String randomTechnique() {
    return ((int) (random() * 2)) > 0 ? "CT" : "MRI";
  }

  private int randomInt() {
    return (int) (random() * 4141);
  }
}