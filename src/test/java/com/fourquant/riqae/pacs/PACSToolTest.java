package com.fourquant.riqae.pacs;

import com.fourquant.riqae.pacs.PACSTool.PACSFacadeFactory;
import org.apache.commons.cli.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

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
  public void testCreatePACSFacadeFactoryWithParams() {
    final String serverExpected = "103.231.642.242";
    final int portExpected = 2423;
    final String userExpected = "admin";

    final PACSFacade factory =
          PACSFacadeFactory.createFactory(serverExpected, portExpected, userExpected);

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
    String[] args = new String[]{"-pn", "John Doe"};
    final CommandLine line;
    try {
      line = parser.parse(options, args);

      final Message request = PACSTool.RequestFactory.createRequest(line.getOptionValues("pn"));

      final PACSFacade pacsFacade = new PACSFacade("localhost", 2133, "admin");
      final Message response = pacsFacade.process(request);

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

    //todo set relative path
    final String[] args = new String[]{
          "-patient-names-file",
          "/Users/nonlo/IdeaProjects/ToolB/src/test/resources/testRequest.csv"};
    final CommandLine line;
    try {
      line = parser.parse(options, args);

      String pnf = line.getOptionValue("pnf");
      final Message request = PACSTool.RequestFactory.createRequest(pnf);

      final PACSFacade pacsFacade = new PACSFacade("localhost", 2133, "admin");
      final Message response = pacsFacade.process(request);

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
