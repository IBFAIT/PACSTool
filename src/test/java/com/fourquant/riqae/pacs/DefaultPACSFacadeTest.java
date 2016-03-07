package com.fourquant.riqae.pacs;

import org.apache.commons.cli.*;
import org.junit.Test;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.fourquant.riqae.pacs.TestConstants.nameAshlee;
import static org.junit.Assert.*;

public class DefaultPACSFacadeTest {
  private final static String server = "localhost";
  private final static int port = 8924;
  private final static String user = "admin";

  private final DefaultPACSFacade pacsFacade =
        new DefaultPACSFacade(server, port, user);

  @Test
  public void testCreatePACSFacadeFactoryWithParams() {
    final String serverExpected = "103.231.642.242";
    final int portExpected = 2423;
    final String userExpected = "admin";

    final DefaultPACSFacade factory =
          PACSTool.PACSFacadeFactory.createFactory(
                serverExpected, portExpected, userExpected);

    final String serverActual = factory.getServer();
    final int portActual = factory.getPort();
    final String userActual = factory.getUser();

    assertEquals(serverExpected, serverActual);
    assertEquals(portExpected, portActual);
    assertEquals(userExpected, userActual);
  }


  @Test
  public void testCreatePACSFacadeFactoryWithCMD() {
    final String serverExpected = "103.231.642.242";
    final int portExpected = 2423;
    final String userExpected = "admin";

    final CommandLineParser parser = new DefaultParser();
    final Options options = PACSTool.OptionsFactory.createOptions();

    String[] args = new String[]{
          "-s", serverExpected,
          "-p", Integer.toString(portExpected),
          "-u", userExpected};
    final CommandLine line;

    try {
      line = parser.parse(options, args);

      final DefaultPACSFacade facade =
            PACSTool.PACSFacadeFactory.createFactory(
                  line.getOptionValue("s"),
                  Integer.parseInt(line.getOptionValue("p")),
                  line.getOptionValue("u"));

      final String serverActual = facade.getServer();
      final int portActual = facade.getPort();
      final String userActual = facade.getUser();

      assertEquals(serverExpected, serverActual);
      assertEquals(portExpected, portActual);
      assertEquals(userExpected, userActual);
    } catch (final ParseException e) {

      e.printStackTrace();
    }
  }


  @Test
  public void testResponseNotNull() throws ParserConfigurationException, SAXException, IOException {
    final List<DataRow> pacsMapObject = new ArrayList<>();
    final List<DataRow> dataRows = pacsFacade.process(pacsMapObject);
    assertNotNull(dataRows);
  }

  @Test
  public void testProcess() throws ParserConfigurationException, SAXException, IOException {
    DataRowFactory DataRowFactory = new DataRowFactory();
    final List<String> patientNames = new ArrayList<>();
    patientNames.add(nameAshlee);
    final List<DataRow> dataRows = DataRowFactory.create(patientNames);

    final List<DataRow> response = pacsFacade.process(dataRows);
    final Set<String> returnedPatientNames = new HashSet<>();
    for (final DataRow dataRow : response) {
      final String patientName = dataRow.getPatientName();
      returnedPatientNames.add(patientName);
    }
    assertTrue(returnedPatientNames.contains(nameAshlee));
  }

  @Test
  public void testQuery() throws ParserConfigurationException, SAXException, IOException {

    List<DataRow> pacsRequest = new ArrayList<>();
    final DataRow ashley = new DataRow();
    ashley.setPatientName(nameAshlee);
    pacsRequest.add(ashley);

    final List<DataRow> dataRows = pacsFacade.process(pacsRequest);

    final DataRow dataRow = dataRows.iterator().next();
    assertNotNull(dataRow);

    assertEquals(nameAshlee, dataRow.getPatientName());
  }

  @Test
  public void testGetServer() throws Exception {
    assertEquals(server, pacsFacade.getServer());
  }

  @Test
  public void testGetPort() throws Exception {
    assertEquals(port, pacsFacade.getPort());
  }
}
