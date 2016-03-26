package com.fourquant.riqae.pacs.tools;

import com.fourquant.riqae.pacs.LoggingFunctionException;
import org.apache.commons.cli.ParseException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;

import static com.fourquant.riqae.pacs.TestConstants.*;
import static com.fourquant.riqae.pacs.tools.Operation.*;
import static java.nio.file.Files.readAllBytes;
import static org.junit.Assert.assertEquals;
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
  public void testHelp() throws ParserConfigurationException,
        SAXException, ParseException, IOException, InterruptedException,
        LoggingFunctionException {

    final String[] args = new String[]{};
    PACSTool.main(args);
    assertTrue(outContent.toString().contains("help"));
  }

  @Test
  public void testResolvePatientIDsByCmdParams()
        throws ParserConfigurationException, SAXException, ParseException,
        IOException, InterruptedException, LoggingFunctionException {

    final Path outputFile = Files.createTempFile("tmp", ".csv");

    final String[] args = new String[]{
          "-patient-name", nameAnna,
          "-patient-name", nameMario,
          "-s", server,
          "-u", userName,
          "-p", port,
          "-c", RESOLVE_PATIENT_IDS.toString(),
          "-o", outputFile.toAbsolutePath().toString()};

    PACSTool.main(args);

    assertTrue(
          new String(
                readAllBytes(outputFile)).
                contains(getCSVDataRow(nameAnna).getPatientID()));

    assertTrue(
          new String(
                readAllBytes(outputFile)).
                contains(getCSVDataRow(nameMario).getPatientID()));

  }

  @Test
  public void testResolvePatientIDsByFile() throws ParserConfigurationException,
        SAXException, ParseException, IOException, InterruptedException,
        LoggingFunctionException {

    final String[] args = new String[]{
          "-input-file",
          getClass().getResource("/osirixNames.csv").getFile(),
          "-s", server,
          "-u", userName,
          "-p", port,
          "-c", RESOLVE_PATIENT_IDS.toString()};

    PACSTool.main(args);

    assertTrue(outContent.toString().contains(nameAnna));
    assertTrue(outContent.toString().contains(getCSVDataRow(nameAnna).getPatientID()));
  }

  @Test
  public void testResolveStudyInstanceUIDs() throws ParserConfigurationException,
        SAXException, ParseException, IOException, InterruptedException,
        LoggingFunctionException {

    final String[] args = new String[]{
          "-input-file",
          getClass().getResource("/osirixNamesAndIds.csv").getFile(),
          "-s", server,
          "-u", userName,
          "-p", port,
          "-c", RESOLVE_STUDY_INSTANCE_UIDS.toString()};

    PACSTool.main(args);

    assertTrue(
          outContent.toString().contains(nameAnna));

    assertTrue(
          outContent.toString().contains(
                getCSVDataRow(nameAnna).getPatientID()));

    assertTrue(
          outContent.toString().contains(
                getCSVDataRow(nameAnna).getStudyInstanceUID()));
  }

  @Test
  public void testResolveSeriesInstanceUIDs() throws ParserConfigurationException,
        SAXException, ParseException, IOException, InterruptedException,
        LoggingFunctionException {

    final String[] args = new String[]{
          "-input-file",
          getClass().getResource("/osirixNamesIdsAndStudyInstanceUIDs.csv").getFile(),
          "-s", server,
          "-u", userName,
          "-p", port,
          "-c", RESOLVE_SERIES_INSTANCE_UIDS.toString()};

    PACSTool.main(args);

    assertTrue(
          outContent.toString().contains(nameAnna));

    assertTrue(
          outContent.toString().contains(
                getCSVDataRow(nameAnna).getPatientID()));

    assertTrue(
          outContent.toString().contains(
                getCSVDataRow(nameAnna).getStudyInstanceUID()));

    assertTrue(
          outContent.toString().contains(
                getCSVDataRow(nameAnna).getSeriesInstanceUID()));
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