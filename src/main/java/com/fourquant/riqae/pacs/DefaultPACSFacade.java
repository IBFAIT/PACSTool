package com.fourquant.riqae.pacs;

import com.fourquant.riqae.pacs.csv.CSVDocWriter;
import com.fourquant.riqae.pacs.csv.DataRow;
import com.fourquant.riqae.pacs.csv.XML2CSVConverter;
import com.fourquant.riqae.pacs.executors.ThirdPartyToolExecutor;
import com.fourquant.riqae.pacs.tools.FindScuCommandCreator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import static com.fourquant.riqae.pacs.PACSFacade.Operation.resolvePatientIds;
import static java.util.stream.Collectors.toList;

public final class DefaultPACSFacade implements PACSFacade {

  private static final Logger log =
        Logger.getLogger(DefaultPACSFacade.class.getName());

  private final String server;

  private final int port;

  private final String user;

  private final String binaryPath;

  private ThirdPartyToolExecutor thirdPartyToolExecutor;

  public DefaultPACSFacade(
        final String server,
        final int port,
        final String user,
        final String binaryPath) {

    this.server = server;
    this.port = port;
    this.user = user;
    this.binaryPath = binaryPath;
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

  public List<DataRow> process(
        final List<DataRow> input) throws IOException, InterruptedException {
    return process(input, resolvePatientIds);
  }

  public List<DataRow> process(
        final List<DataRow> input,
        Operation operation) throws IOException, InterruptedException {

//todo use operation

    final List<DataRow> output = new ArrayList<>();
    for (final DataRow inputRow : input) {

      final String patientName = inputRow.getPatientName();

      final FindScuCommandCreator findScuCommandCreator =
            new FindScuCommandCreator();

      final String findSCUCall =
            findScuCommandCreator.createFindScuStatement(
                  patientName, user, server, Integer.toString(port), binaryPath);

      final String[] xmlResult =
            thirdPartyToolExecutor.execute(findSCUCall);

      final XML2CSVConverter xml2CSVConverter = new XML2CSVConverter();

      for (final String anXmlResult : xmlResult) {
        final List<DataRow> dataRows = xml2CSVConverter.convert(anXmlResult);
        output.addAll(
              dataRows.stream().collect(toList()));
      }

      /*

       todo: add actual logic processing logic

       cases are:
       * resolve patientIds from names
       * resolve resolve study descriptions...
       * resolve bla
       * resolve bli
       * do calculate results
       *
       */

    }

    final CSVDocWriter csvDocWriter = new CSVDocWriter();
    final StringBuffer buffer = new StringBuffer();
    csvDocWriter.writeDataRows(output, buffer);

    return output;
  }

  public final void setThirdPartyToolExecutor(
        final ThirdPartyToolExecutor thirdPartyToolExecutor) {
    this.thirdPartyToolExecutor = thirdPartyToolExecutor;
  }
}