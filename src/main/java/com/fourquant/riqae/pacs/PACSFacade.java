package com.fourquant.riqae.pacs;

import com.fourquant.riqae.pacs.CSVDoc.Row;

import java.util.logging.Logger;

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
      findscu -c DCMQRSCP@localhost:11112 -m PatientName=John^Doe -m
        StudyDate=20110510- -m ModalitiesInStudy=CT

        Query Query/Retrieve Service Class Provider DCMQRSCP listening on local
        port 11112 for CT Studies for Patient John Doe since 2011-05-10
     */
  public final CSVDoc process(final CSVDoc request) {

    final CSVDoc response = new CSVDoc();
    final Executor executor = new Executor(getUser(), getServer(), getPort());

    //for every patient...
    for (final Row requestRow : request) {

      //create findscu call
      final String patientName = requestRow.getPatientName();
      final FindScuCommandCreator findScuCommandCreator =
            new FindScuCommandCreator();

      final String findSCUCall =
            findScuCommandCreator.createFindScuStatement(
                  patientName, user, server, Integer.toString(port));

      final FindScuCommandExecutorDummy findScuCommandExecutorDummy =
            new FindScuCommandExecutorDummy();

      //todo
      final String resulttodo = findScuCommandExecutorDummy.execute(findSCUCall);

      System.out.println("result = " + resulttodo);

      /* execute findscucalls

      */
      final CSVDoc result = executor.execute(findSCUCall);
      for (final Row row : result) {
        response.add(row);
      }
    }

    return response;
  }
}