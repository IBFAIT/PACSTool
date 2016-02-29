package com.fourquant.riqae.pacs;

import com.fourquant.riqae.pacs.Message.Row;

import java.util.logging.Logger;

import static java.lang.Math.random;

public final class PACSFacade {
  private static final Logger log =
        Logger.getLogger(PACSFacade.class.getName());

  private final String server;

  private final int port;

  private final String user;

  public PACSFacade(final String server, final int port, final String user) {
    this.server = server;
    this.port = port;
    this.user = user;
  }

  public final String getServer() {
    return server;
  }

  public final int getPort() {
    return port;
  }

  public final String getUser() {
    return user;
  }

  /*
      Processing faked
      Does not establish no connection to no PACS server at the moment
      Populates responseMessage with random values

      this is probably the place where we will have our real queries later...
      findscu -c DCMQRSCP@localhost:11112 -m PatientName=Doe^John -m
        StudyDate=20110510- -m ModalitiesInStudy=CT

        Query Query/Retrieve Service Class Provider DCMQRSCP listening on local
        port 11112 for CT Studies for Patient John Doe since 2011-05-10
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