package com.fourquant.riqae.pacs.tools;

import org.apache.commons.cli.ParseException;
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

import static com.fourquant.riqae.pacs.TestConstants.*;
import static java.nio.file.Files.readAllLines;
import static org.junit.Assert.assertTrue;

public class PACSToolTest {

  private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
  private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();

  private PrintStream oldOut;
  private PrintStream oldErr;

  @Before
  public void setUpStreams() {
    oldOut = System.out;
    oldErr = System.err;
    System.setOut(new PrintStream(outContent));
    System.setErr(new PrintStream(errContent));
  }

  @After
  public void cleanUpStreams() {
    System.setOut(oldOut);
    System.setErr(oldErr);
  }

  @Test
  public void testWithBinaryPathOnly() throws ParserConfigurationException,
        SAXException, ParseException, IOException, InterruptedException {

    final String[] args = new String[]{};
    PACSTool.main(args);
    assertTrue(outContent.toString().contains("help"));
  }

  @Test
  public void testWithPatientNames() throws ParserConfigurationException,
        SAXException, ParseException, IOException, InterruptedException {

    final String[] args = new String[]{
          "-patient-name", patientNames[0],
          "-patient-name", patientNames[1],
          "-s", server,
          "-u", userName,
          "-p", port};

    PACSTool.main(args);
    assertTrue(outContent.toString().contains(patientIds[0]));
    assertTrue(outContent.toString().contains(patientIds[1]));
  }

  @Test
  public void testWithPatientNameFile() throws ParserConfigurationException,
        SAXException, ParseException, IOException, InterruptedException {

    final String[] args = new String[]{
          "-patient-names-file",
          getClass().getResource("/osirixNames.csv").getFile(),
          "-s", server,
          "-u", userName,
          "-p", port};

    PACSTool.main(args);
    assertTrue(outContent.toString().contains("Verdi Anna Fasula"));
  }

  @Test
  public void testWithPatientNameFileAndOutputFile()
        throws ParserConfigurationException, SAXException, ParseException,
        IOException, InterruptedException {

    final String[] args = new String[]{
          "-patient-names-file",
          getClass().getResource("/osirixNames.csv").getFile(),
          "-s", server,
          "-u", userName,
          "-p", port,
          "-output-file", getClass().getResource("/out.csv").getFile()};

    PACSTool.main(args);

    final Path path =
          FileSystems.getDefault().getPath(
                getClass().getResource("/out.csv").getPath());

    final List<String> outputLines = readAllLines(path);
    String output = "";
    for (String line : outputLines) {
      output += line;
    }

    assertTrue(output.contains("Verdi Anna Fasula"));
  }

/*
  @Test
  public void testPatientNamesFile() throws ParseException,
        IOException, InterruptedException, ParserConfigurationException, SAXException {

    final CommandLineParser parser = new DefaultParser();
    final Options options = OptionsFactory.createOptions();

    final String[] args = new String[]{
          "-patient-names-file",
          getClass().getResource("/names.csv").getFile()};

    final CommandLine line;
    line = parser.parse(options, args);

    String pnf = line.getOptionValue(OptionsFactory.optPatientNamesFile);

    final CSVReaderService csvDocReader = new CSVReaderService();

    final List<CSVDataRow> request = csvDocReader.createDataRows(pnf);

    final DefaultPACSFacade pacsFacade =
          new DefaultPACSFacade("localhost", 2133, "admin");


    pacsFacade.setThirdPartyToolExecutor(
          new DummyThirdPartyToolExecutor(
                new String[]{"ashlee.xml"}));

    final List<CSVDataRow> response = pacsFacade.process(request);

    final CSVWriterService csvDocWriter = new CSVWriterService();

    csvDocWriter.writeDataRows(response);

    final String out = outContent.toString();
    assertTrue(out.contains("Ashlee Simpson"));
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

*/
}