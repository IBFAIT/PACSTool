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
import java.util.List;

import static com.fourquant.riqae.pacs.PACSTool.RequestFactory.createRequest;
import static com.fourquant.riqae.pacs.PACSTool.optPatientNamesFile;
import static com.fourquant.riqae.pacs.TestConstants.binaryPath;
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
          "-patient-name", "Brebix",
          "-patient-name", "Wrix",
          "-s", "localhost",
          "-u", "OSIRIX",
          "-p", "11112",
          "-bp", binaryPath};

    PACSTool.main(args);
    assertTrue(outContent.toString().contains("XsaDYa"));
    assertTrue(outContent.toString().contains("7rAgWJ"));
  }

  /*
  @Test
  public void testWithPatientNameFile() throws ParserConfigurationException, SAXException, ParseException, IOException, InterruptedException {
    final String[] args = new String[]{
          "-patient-names-file", getClass().getResource("/names.csv").getFile(), "-bp", "xxx"};
    PACSTool.main(args);
    assertTrue(outContent.toString().contains(nameAshlee));
    assertTrue(outContent.toString().contains(nameKate));
    assertTrue(outContent.toString().contains(nameDonatella));
  }

*/

  /*

  @Test
  public void testWithPatientNameFileAndOutputFile() throws ParserConfigurationException, SAXException, ParseException, IOException, InterruptedException {
    final String[] args = new String[]{
          "-patient-names-file", getClass().getResource("/names.csv").getFile(),
          "-output-file", getClass().getResource("/out.csv").getFile(), "-bp", "xxx"};

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
  }

  */

  @Test
  public void testPatientNamesFile() throws ParseException, IOException, InterruptedException {
    final CommandLineParser parser = new DefaultParser();
    final Options options = PACSTool.OptionsFactory.createOptions();

    final String[] args = new String[]{
          "-patient-names-file",
          getClass().getResource("/names.csv").getFile(), "-bp", "xxx"};

    final CommandLine line;
    line = parser.parse(options, args);

    String pnf = line.getOptionValue(optPatientNamesFile);
    final List<DataRow> request = createRequest(pnf);

    final DefaultPACSFacade pacsFacade =
          new DefaultPACSFacade("localhost", 2133, "admin", "xxx");


    pacsFacade.setThirdPartyToolExecutor(
          new DummyThirdPartyToolExecutor(
                new String[]{"ashlee.xml"}));

    final List<DataRow> response = pacsFacade.process(request);

    ResponseWriter.write(response);

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