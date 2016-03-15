package com.fourquant.riqae.pacs.tools;

import com.fourquant.riqae.pacs.DefaultPACSFacade;
import com.fourquant.riqae.pacs.csv.CSVDocReader;
import com.fourquant.riqae.pacs.csv.CSVDocWriter;
import com.fourquant.riqae.pacs.csv.DataRow;
import com.fourquant.riqae.pacs.executors.DummyThirdPartyToolExecutor;
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
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static com.fourquant.riqae.pacs.TestConstants.*;
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
  public void testWithBinaryPathOnly() throws ParserConfigurationException,
        SAXException, ParseException, IOException, InterruptedException {

    final String[] args = new String[]{"-bp", "xxx"};
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
          "-p", port,
          "-bp", binaryPath};

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
          "-p", port,
          "-bp", binaryPath};

    PACSTool.main(args);
    assertTrue(outContent.toString().contains(patientIds[0]));
    assertTrue(outContent.toString().contains(patientIds[1]));
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
          "-bp", binaryPath,
          "-output-file", getClass().getResource("/out.csv").getFile()};

    PACSTool.main(args);

    final Path path =
          FileSystems.getDefault().getPath(getClass().getResource("/out.csv").getPath());
    final List<String> outputLines = Files.readAllLines(path);
    String output = "";
    for (String line : outputLines) {
      output += line;
    }

    assertTrue(output.contains(patientIds[0]));
    assertTrue(output.contains(patientIds[1]));
  }


  @Test
  public void testPatientNamesFile() throws ParseException,
        IOException, InterruptedException {

    final CommandLineParser parser = new DefaultParser();
    final Options options = OptionsFactory.createOptions();

    final String[] args = new String[]{
          "-patient-names-file",
          getClass().getResource("/names.csv").getFile(), "-bp", "xxx"};

    final CommandLine line;
    line = parser.parse(options, args);

    String pnf = line.getOptionValue(OptionsFactory.optPatientNamesFile);

    final CSVDocReader csvDocReader = new CSVDocReader();

    final List<DataRow> request = csvDocReader.createDataRows(pnf);

    final DefaultPACSFacade pacsFacade =
          new DefaultPACSFacade("localhost", 2133, "admin", "xxx");


    pacsFacade.setThirdPartyToolExecutor(
          new DummyThirdPartyToolExecutor(
                new String[]{"ashlee.xml"}));

    final List<DataRow> response = pacsFacade.process(request);

    final CSVDocWriter csvDocWriter = new CSVDocWriter();

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


}