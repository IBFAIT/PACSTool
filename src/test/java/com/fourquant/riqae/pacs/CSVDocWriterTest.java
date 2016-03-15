package com.fourquant.riqae.pacs;

import org.junit.Test;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileReader;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import static com.fourquant.riqae.pacs.TestConstants.nameAshlee;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class CSVDocWriterTest {

  @Test
  public void testWriteToBuffer() throws Exception {
    final CSVDocWriter writer = new CSVDocWriter();
    final List<DataRow> dataRows = new ArrayList<>();
    final DataRow dataRow = new DataRow();
    dataRow.setPatientName(nameAshlee);
    final StringBuffer buffer = new StringBuffer();

    dataRows.add(dataRow);

    writer.write(dataRows, buffer);

    assertTrue(buffer.toString().contains(nameAshlee));
  }

  @Test
  public void testWrite() throws Exception {
    final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    System.setOut(new PrintStream(outContent));

    final CSVDocWriter writer = new CSVDocWriter();
    final List<DataRow> dataRows = new ArrayList<>();
    writer.write(dataRows);

    final String header =
          "Patient Name;Patient ID;Study Date;Study Description;Series Description;Study Instance UID;Series Instance UID;Result";
    assertEquals(
          header,
          outContent.toString().trim());
  }

  @Test
  public void testWriteToFile() throws Exception {
    final DataRowFactory factory = new DataRowFactory();
    final List<DataRow> dataRows =
          factory.create(
                getClass().
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
    System.out.println("file = " + file);
    writer.write(dataRows, file);

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
    writer.write(new ArrayList<>(), "/does/not/exist");
  }
}