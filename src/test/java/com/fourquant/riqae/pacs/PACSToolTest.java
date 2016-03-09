package com.fourquant.riqae.pacs;

import com.fourquant.riqae.pacs.PACSTool.ResponseWriter;
import org.apache.commons.cli.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.List;

import static com.fourquant.riqae.pacs.PACSTool.RequestFactory.createRequest;
import static com.fourquant.riqae.pacs.PACSTool.optPatientNamesFile;
import static com.fourquant.riqae.pacs.TestConstants.*;
import static java.nio.file.Files.readAllLines;
import static org.junit.Assert.*;

public class PACSToolTest {

  private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
  private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();

  @Before
  public void setUpStreams() {
    System.setOut(new PrintStream(outContent));
    System.setErr(new PrintStream(errContent));
  }

  @After
  public void cleanUpStreams() {
    System.setOut(null);
    System.setErr(null);
  }

  @Test
  public void testWithNoParameters() {
    final String[] args = new String[]{};
    try {
      PACSTool.main(args);
      assertTrue(outContent.toString().contains("help"));
    } catch (ParseException | SAXException | IOException | ParserConfigurationException e) {
      e.printStackTrace();
    }
  }

  @Test
  public void testFoo() {
    final String[] args = new String[]{
          "-patient-name", "Flavio Trolese",
          "-patient-name", "Kevin Mader",
          "-s", "localhost", "foo"};

    try {
      PACSTool.main(args);

    } catch (ParseException | SAXException | IOException | ParserConfigurationException e) {
      e.printStackTrace();
    }
  }

  @Test
  public void testWithPatientNames() {
    final String[] args = new String[]{
          "-patient-name", nameKate,
          "-patient-name", nameAshlee,
          "-s", "localhost"};
    try {
      PACSTool.main(args);
      assertTrue(outContent.toString().contains(nameKate));
      assertTrue(outContent.toString().contains(nameAshlee));
    } catch (ParseException | SAXException | IOException | ParserConfigurationException e) {
      e.printStackTrace();
    }
  }

  @Test
  public void testWithPatientNameFile() {
    final String[] args = new String[]{
          "-patient-names-file", getClass().getResource("/names.csv").getFile()};
    try {
      PACSTool.main(args);
      assertTrue(outContent.toString().contains(nameAshlee));
      assertTrue(outContent.toString().contains(nameKate));
      assertTrue(outContent.toString().contains(nameDonatella));
    } catch (ParseException | SAXException | IOException | ParserConfigurationException e) {
      e.printStackTrace();
    }
  }

  @Test
  public void testWithPatientNameFileAndOutputFile() {
    final String[] args = new String[]{
          "-patient-names-file", getClass().getResource("/names.csv").getFile(),
          "-output-file", getClass().getResource("/out.csv").getFile()};
    try {
      PACSTool.main(args);
      assertFalse(outContent.toString().contains(nameAshlee));
      assertFalse(outContent.toString().contains(nameKate));
      assertFalse(outContent.toString().contains(nameDonatella));

      final Path path =
            FileSystems.getDefault().getPath(getClass().getResource("/out.csv").getPath());
      final List<String> outputLines = readAllLines(path);
      String output = "";
      for (String line : outputLines) {
        output += line;
      }

      assertTrue(output.contains(nameAshlee));
      assertTrue(output.contains(nameKate));
      assertTrue(output.contains(nameDonatella));
    } catch (ParseException | IOException | ParserConfigurationException | SAXException e) {
      e.printStackTrace();
    }
  }

  @Test
  public void testPatientNamesFile() {
    final CommandLineParser parser = new DefaultParser();
    final Options options = PACSTool.OptionsFactory.createOptions();

    final String[] args = new String[]{
          "-patient-names-file",
          getClass().getResource("/names.csv").getFile()};

    final CommandLine line;
    try {
      line = parser.parse(options, args);

      String pnf = line.getOptionValue(optPatientNamesFile);
      final List<DataRow> request = createRequest(pnf);

      final DefaultPACSFacade pacsFacade =
            new DefaultPACSFacade("localhost", 2133, "admin");

      pacsFacade.setThirdPartyToolExecutor(
            new DummyThirdPartyToolExecutor(
                  new String[]{"ashlee.xml"}));

      final List<DataRow> response = pacsFacade.process(request);

      ResponseWriter.write(response);

      final String out = outContent.toString();
      assertTrue(out.contains("Ashlee Simpson"));
    } catch (ParseException | IOException e) {
      e.printStackTrace();
    }
  }

  @Test
  public void testOut() {
    System.out.print("hello");
    assertEquals("hello", outContent.toString());
  }

  @Test
  public void testErr() {
    System.err.print("hello again");
    assertEquals("hello again", errContent.toString());
  }
}