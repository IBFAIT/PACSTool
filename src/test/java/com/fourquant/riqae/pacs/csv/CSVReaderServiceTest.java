package com.fourquant.riqae.pacs.csv;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import static com.fourquant.riqae.pacs.TestConstants.*;
import static java.util.stream.Collectors.toSet;
import static org.junit.Assert.*;

public class CSVReaderServiceTest {

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
  public void testCreateFromCommandLineArgs() throws Exception {
    final CSVReaderService csvReaderService = new CSVReaderService();

    final Set<String> patientNames = new HashSet<String>() {{
      add(nameDonatella);
      add(nameKate);
    }};

    final Set<CSVDataRow> csvDataRows =
          csvReaderService.createDataRowsWithNames(patientNames);

    final Iterator<CSVDataRow> iterator = csvDataRows.iterator();
    final CSVDataRow csvDataRow = iterator.next();

    assertEquals(nameDonatella, csvDataRow.getPatientName());
    assertEquals(nameKate, iterator.next().getPatientName());
  }

  @Test
  public void testCreateFromCSVFile() throws Exception {
    final CSVReaderService csvReaderService = new CSVReaderService();
    final Set<CSVDataRow> csvDataRows =
          csvReaderService.createDataRows(
                getClass().getResource("/names.csv").getFile());

    final Set<String> patientNames =
          csvDataRows.stream().map(CSVDataRow::getPatientName).collect(toSet());

    assertTrue(patientNames.contains(nameAshlee));
    assertTrue(patientNames.contains(nameDonatella));
    assertTrue(patientNames.contains(nameKate));
    assertFalse(patientNames.contains("Patient Name"));
  }
}