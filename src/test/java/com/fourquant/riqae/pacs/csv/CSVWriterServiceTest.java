package com.fourquant.riqae.pacs.csv;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static com.fourquant.riqae.pacs.TestConstants.nameAshlee;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class CSVWriterServiceTest {

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
  public void testWrite() throws Exception {
    final String namesFile =
          getClass().
                getResource("/namesIdsStudyInstanceUIDsAndSerieInstanceUIDs.csv").
                getFile();

    final CSVReaderService csvReaderService = new CSVReaderService();
    final CSVWriterService csvWriterService = new CSVWriterService();

    final Set<CSVDataRow> CSVDataRows = csvReaderService.createDataRows(namesFile);
    csvWriterService.writeDataRows(CSVDataRows);
    assertTrue(
          outContent.toString().
                contains("Kate Moss;USB000123471;591048351241.9457933;20111231;CT Thorax;591048351241.9457933.5;Series Description;"));
  }

  /*
  @Test
  public void testResponseWriter() throws ParseException, IOException,
        InterruptedException, ParserConfigurationException, SAXException {

    final CommandLineParser parser = new DefaultParser();
    final Options options = OptionsFactory.createOptions();

    String[] args = new String[]{"-" + optPatientName, nameKate};

    final CommandLine line;
    line = parser.parse(options, args);

    final CSVReaderService csvDocReader = new CSVReaderService();

    final List<CSVDataRow> request =
          csvDocReader.createDataRows(line.getOptionValues(optPatientName));

    final DefaultPACSFacade pacsFacade =
          new DefaultPACSFacade("localhost", 2133, "admin");

    pacsFacade.setThirdPartyToolExecutor(
          new DummyThirdPartyToolExecutor(
                new String[]{"kate.xml"}));

    final List<CSVDataRow> response = pacsFacade.process(request);

    final CSVWriterService csvDocWriter = new CSVWriterService();
    csvDocWriter.writeDataRows(response);

    final String out = outContent.toString();
    assertTrue(out.contains(nameKate));
  }

*/

  @Test
  public void testWriteToBuffer() throws Exception {
    final CSVWriterService writer = new CSVWriterService();
    final List<CSVDataRow> CSVDataRows = new ArrayList<>();
    final CSVDataRow CSVDataRow = new CSVDataRow();
    CSVDataRow.setPatientName(nameAshlee);
    final StringBuffer buffer = new StringBuffer();

    CSVDataRows.add(CSVDataRow);

    writer.writeDataRows(CSVDataRows, buffer);

    assertTrue(buffer.toString().contains(nameAshlee));
  }

  @Test
  public void testWriteDataRows() throws Exception {
    final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    System.setOut(new PrintStream(outContent));

    final CSVWriterService writer = new CSVWriterService();
    final List<CSVDataRow> CSVDataRows = new ArrayList<>();
    writer.writeDataRows(CSVDataRows);

    final String header =
          "Patient Name;Patient ID;Study Instance UID;Study Date;Study Description;Series Instance UID;Series Description;Result";
    assertEquals(
          header,
          outContent.toString().trim());
  }

  @Test
  public void testWriteToFile() throws Exception {
    final CSVReaderService csvReaderService = new CSVReaderService();

    final Set<CSVDataRow> csvDataRows =
          csvReaderService.createDataRows(getClass().
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

    final CSVWriterService writer = new CSVWriterService();

    final String file = getClass().getResource("/dummy.csv").getFile();

    writer.writeDataRows(csvDataRows, file);

    try (BufferedReader br = new BufferedReader(new FileReader(file))) {
      String line;
      while ((line = br.readLine()) != null) {
        System.out.println(line);
      }
    }
  }

  @Test(expected = FileNotFoundException.class)
  public void testWriteWithException() throws Exception {
    final CSVWriterService writer = new CSVWriterService();
    writer.writeDataRows(new ArrayList<>(), "/does/not/exist");
  }
}