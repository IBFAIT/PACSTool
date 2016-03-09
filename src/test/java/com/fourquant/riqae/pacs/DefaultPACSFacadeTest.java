package com.fourquant.riqae.pacs;

import org.apache.commons.cli.*;
import org.junit.Test;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.fourquant.riqae.pacs.TestConstants.nameAshlee;
import static java.lang.Integer.parseInt;
import static java.nio.file.FileSystems.getDefault;
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

    final DefaultPACSFacade pacsFacade = new DefaultPACSFacade(
          serverExpected,
          portExpected,
          userExpected);

    final String serverActual = pacsFacade.getServer();
    final int portActual = pacsFacade.getPort();
    final String userActual = pacsFacade.getUser();

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
            new DefaultPACSFacade(
                  line.getOptionValue("s"),
                  parseInt(line.getOptionValue("p")),
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
  public void testProcessWithPatientNames() throws ParserConfigurationException, SAXException, IOException {

    final DataRowFactory DataRowFactory = new DataRowFactory();
    final List<String> patientNames = new ArrayList<>();
    patientNames.add(nameAshlee);
    final List<DataRow> dataRows = DataRowFactory.create(patientNames);

    pacsFacade.setThirdPartyToolExecutor(
          new DummyThirdPartyToolExecutor(
                new String[]{"ashlee.xml"}));

    final List<DataRow> response = pacsFacade.process(dataRows);
    final Set<String> returnedPatientNames = new HashSet<>();
    for (final DataRow dataRow : response) {
      final String patientName = dataRow.getPatientName();
      returnedPatientNames.add(patientName);
    }
    assertTrue(returnedPatientNames.contains(nameAshlee));
  }

  @Test
  public void testProcessWithPatientIds() throws ParserConfigurationException, SAXException, IOException {

    final DataRowFactory DataRowFactory = new DataRowFactory();
    final List<String> patientNames = new ArrayList<>();
    patientNames.add(nameAshlee);
    final List<DataRow> dataRows = DataRowFactory.create(patientNames);

    pacsFacade.setThirdPartyToolExecutor(new ThirdPartyToolExecutor() {
      @Override
      public String[] execute(String command) {
        try {
          final String[] xml = new String[1];
          xml[0] = readContent(getPath("/" + "ashlee.xml"));
          return xml;
        } catch (IOException e) {
          e.printStackTrace();
        }
        return null;
      }

      private Path getPath(final String fileName) {

        return getDefault().getPath(getClass().
              getResource(fileName).getPath());
      }

      private String readContent(final Path path) throws IOException {
        String content = "";
        final List<String> lines = Files.readAllLines(path);
        for (final String line : lines) {
          content += line;
          content += System.getProperty("line.separator");
        }
        return content;
      }

    });

    final List<DataRow> response = pacsFacade.process(dataRows);

    final Set<String> returnedPatientIds = new HashSet<>();
    for (final DataRow dataRow : response) {
      final String patientName = dataRow.getPatientId();
      returnedPatientIds.add(patientName);
    }
    assertTrue(returnedPatientIds.contains("USB000429321"));
  }

  @Test
  public void testResolveSeriesInstanceUids() throws Exception {
    //read complex csv file with multiple patient names
    //process this file
    //get a complex csv list back
    //todo
    pacsFacade.setThirdPartyToolExecutor(
          new DummyThirdPartyToolExecutor(
                new String[]{"ashlee.xml", "donatella.xml", "kate.xml"}));

    final DataRowFactory factory = new DataRowFactory();

    final List<DataRow> input =
          factory.create(
                getClass().
                      getResource("/namesIdsAndStudyInstanceUIDs.csv").getFile());

    final List<DataRow> output = pacsFacade.process(input);
    // TODO
//    Assert.assertTrue(false);
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