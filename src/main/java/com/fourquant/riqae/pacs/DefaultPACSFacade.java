package com.fourquant.riqae.pacs;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public final class DefaultPACSFacade implements PACSFacade {

  private static final Logger log =
        Logger.getLogger(DefaultPACSFacade.class.getName());

  private final String server;

  private final int port;

  private final String user;

  public DefaultPACSFacade(
        final String server,
        final int port,
        final String user) {

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

  public List<DataRow> process(final List<DataRow> input) throws
        IOException {

    final List<DataRow> output = new ArrayList<>();
    for (final DataRow dataRow : input) {

      final String patientName = dataRow.getPatientName();
      final FindScuCommandCreator findScuCommandCreator =
            new FindScuCommandCreator();

      final String findSCUCall =
            findScuCommandCreator.createFindScuStatement(
                  patientName, user, server, Integer.toString(port));

      //replace by real implementation
      final ThirdPartyToolExecutor findScuExecutor =

            new ThirdPartyToolExecutor() {
              private String patientName;

              @Override
              public String execute(String command) {

                return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                      "<NativeDicomModel xml:space=\"preserve\">\n" +
                      "<DicomAttribute keyword=\"PatientID\" tag=\"00100020\" vr=\"LO\">" +
                      "<Value number=\"1\">USB0003138461</Value>" +
                      "</DicomAttribute>\n" +
                      "<DicomAttribute keyword=\"PatientName\" tag=\"00100010\" vr=\"DA\">" +
                      " <Value number=\"1\">" + dataRow.getPatientName() + "</Value>" +
                      "</DicomAttribute>\n" +
                      "</NativeDicomModel>";
              }
            };

      final String xmlResult = findScuExecutor.execute(findSCUCall);
      final XML2CSVConverter xml2CSVConverter = new XML2CSVConverter();
      final List<DataRow> rows = xml2CSVConverter.convert(xmlResult);

      for (final DataRow row : rows) {
        output.add(row);
      }
    }

    final CSVDocWriter csvDocWriter = new CSVDocWriter();
    final StringBuffer buffer = new StringBuffer();
    csvDocWriter.write(output, buffer);

    return output;
  }
}