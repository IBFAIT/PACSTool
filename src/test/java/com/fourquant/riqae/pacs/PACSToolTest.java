package com.fourquant.riqae.pacs;

import com.fourquant.riqae.pacs.PACSTool.PACSFacadeFactory;
import org.apache.commons.cli.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.List;

import static com.fourquant.riqae.pacs.PACSTool.RequestFactory.createRequest;
import static com.fourquant.riqae.pacs.PACSTool.optPatientName;
import static com.fourquant.riqae.pacs.PACSTool.optPatientNamesFile;
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
    } catch (ParseException e) {
      e.printStackTrace();
    }
  }

  @Test
  public void testWithPatientNames() {
    final String[] args = new String[]{
          "-patient-name", "Flavio Trolese",
          "-patient-name", "Kevin Mader",
          "-s", "localhost"};
    try {
      PACSTool.main(args);
      assertTrue(outContent.toString().contains("Kevin Mader"));
      assertTrue(outContent.toString().contains("Flavio Trolese"));
    } catch (ParseException e) {
      e.printStackTrace();
    }
  }

  @Test
  public void testWithPatientNameFile() {
    final String[] args = new String[]{
          "-patient-names-file", getClass().getResource("/names.csv").getFile()};
    try {
      PACSTool.main(args);
      assertTrue(outContent.toString().contains("Ashlee Simpson"));
      assertTrue(outContent.toString().contains("Kate Moss"));
      assertTrue(outContent.toString().contains("Donatella Versace"));
    } catch (ParseException e) {
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
      assertFalse(outContent.toString().contains("Ashlee Simpson"));
      assertFalse(outContent.toString().contains("Kate Moss"));
      assertFalse(outContent.toString().contains("Donatella Versace"));

      final Path path =
            FileSystems.getDefault().getPath(getClass().getResource("/out.csv").getPath());
      final List<String> outputLines = readAllLines(path);
      String output = "";
      for (String line : outputLines) {
        output += line;
      }

      assertTrue(output.contains("Ashlee Simpson"));
      assertTrue(output.contains("Kate Moss"));
      assertTrue(output.contains("Donatella Versace"));
    } catch (ParseException | IOException e) {
      e.printStackTrace();
    }
  }

  @Test
  public void testCreatePACSFacadeFactoryWithParams() {
    final String serverExpected = "103.231.642.242";
    final int portExpected = 2423;
    final String userExpected = "admin";

    final PACSFacade factory =
          PACSFacadeFactory.createFactory(
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

      final PACSFacade factory =
            PACSFacadeFactory.createFactory(
                  line.getOptionValue("s"),
                  Integer.parseInt(line.getOptionValue("p")),
                  line.getOptionValue("u"));

      final String serverActual = factory.getServer();
      final int portActual = factory.getPort();
      final String userActual = factory.getUser();

      assertEquals(serverExpected, serverActual);
      assertEquals(portExpected, portActual);
      assertEquals(userExpected, userActual);
    } catch (final ParseException e) {

      e.printStackTrace();
    }
  }

  @Test
  public void testResponseWriter() {
    final CommandLineParser parser = new DefaultParser();
    final Options options = PACSTool.OptionsFactory.createOptions();
    String[] args = new String[]{"-" + optPatientName, "John Doe"};
    final CommandLine line;
    try {
      line = parser.parse(options, args);

      final CSVDoc request =
            createRequest(line.getOptionValues(optPatientName));

      final PACSFacade pacsFacade = new PACSFacade("localhost", 2133, "admin");
      final CSVDoc response = pacsFacade.process(request);

      PACSTool.ResponseWriter.write(response);

      final String out = outContent.toString();
      assertTrue(out.contains("John Doe"));
    } catch (ParseException e) {
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
      final CSVDoc request = createRequest(pnf);

      final PACSFacade pacsFacade = new PACSFacade("localhost", 2133, "admin");
      final CSVDoc response = pacsFacade.process(request);

      PACSTool.ResponseWriter.write(response);

      final String out = outContent.toString();
      assertTrue(out.contains("Ashlee Simpson"));
    } catch (ParseException e) {
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