package com.fourquant.riqae.pacs.csv;

import com.fourquant.riqae.pacs.DefaultPACSFacade;
import com.fourquant.riqae.pacs.executors.DummyThirdPartyToolExecutor;
import com.fourquant.riqae.pacs.tools.OptionsFactory;
import org.apache.commons.cli.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import static com.fourquant.riqae.pacs.TestConstants.*;
import static com.fourquant.riqae.pacs.tools.OptionsFactory.optPatientName;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class CSVDocWriterTest {

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
  public void testWrite() throws Exception {
    final String namesFile =
          getClass().
                getResource("/namesIdsStudyInstanceUIDsAndSeriesInstanceUIDs.csv").
                getFile();

    final CSVDocReader csvDocReader = new CSVDocReader();
    final CSVDocWriter csvDocWriter = new CSVDocWriter();

    final List<DataRow> dataRows = csvDocReader.createDataRows(namesFile);
    csvDocWriter.writeDataRows(dataRows);
    assertTrue(
          outContent.toString().
                contains("Kate Moss;USB000123471;591048351241.9457933;20111231;CT Thorax;591048351241.9457933.5;Series Description;"));
  }

  @Test
  public void testResponseWriter() throws ParseException, IOException,
        InterruptedException {

    final CommandLineParser parser = new DefaultParser();
    final Options options = OptionsFactory.createOptions();

    String[] args = new String[]{"-" + optPatientName, nameKate, "-" +
          OptionsFactory.optBinaryPath, binaryPath};

    final CommandLine line;
    line = parser.parse(options, args);

    final CSVDocReader csvDocReader = new CSVDocReader();

    final List<DataRow> request =
          csvDocReader.createDataRows(line.getOptionValues(optPatientName));

    final DefaultPACSFacade pacsFacade =
          new DefaultPACSFacade("localhost", 2133, "admin", binaryPath);

    pacsFacade.setThirdPartyToolExecutor(
          new DummyThirdPartyToolExecutor(
                new String[]{"kate.xml"}));

    final List<DataRow> response = pacsFacade.process(request);

    final CSVDocWriter csvDocWriter = new CSVDocWriter();
    csvDocWriter.writeDataRows(response);

    final String out = outContent.toString();
    assertTrue(out.contains(nameKate));
  }

  @Test
  public void testWriteToBuffer() throws Exception {
    final CSVDocWriter writer = new CSVDocWriter();
    final List<DataRow> dataRows = new ArrayList<>();
    final DataRow dataRow = new DataRow();
    dataRow.setPatientName(nameAshlee);
    final StringBuffer buffer = new StringBuffer();

    dataRows.add(dataRow);

    writer.writeDataRows(dataRows, buffer);

    assertTrue(buffer.toString().contains(nameAshlee));
  }

  @Test
  public void testWriteDataRows() throws Exception {
    final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    System.setOut(new PrintStream(outContent));

    final CSVDocWriter writer = new CSVDocWriter();
    final List<DataRow> dataRows = new ArrayList<>();
    writer.writeDataRows(dataRows);

    final String header =
          "Patient Name;Patient ID;Study Instance UID;Study Date;Study Description;Series Instance UID;Series Description;Result";
    assertEquals(
          header,
          outContent.toString().trim());
  }

  @Test
  public void testWriteToFile() throws Exception {
    final CSVDocReader csvDocReader = new CSVDocReader();

    final List<DataRow> dataRows =
          csvDocReader.createDataRows(getClass().
                getResource("/names.csv").getFile());

    try (BufferedReader br =
               new BufferedReader(
                     new FileReader(
                           getClass().
                                 getResource("/names.csv").getFile()))) {
      String line;
      while ((line = br.readLine()) != null) {
        System.out.println(line);
      }
    }

    final CSVDocWriter writer = new CSVDocWriter();

    final String file = getClass().getResource("/dummy.csv").getFile();

    writer.writeDataRows(dataRows, file);

    try (BufferedReader br = new BufferedReader(new FileReader(file))) {
      String line;
      while ((line = br.readLine()) != null) {
        System.out.println(line);
      }
    }
  }

  @Test(expected = IllegalStateException.class)
  public void testWriteWithException() throws Exception {
    final CSVDocWriter writer = new CSVDocWriter();
    writer.writeDataRows(new ArrayList<>(), "/does/not/exist");
  }
}