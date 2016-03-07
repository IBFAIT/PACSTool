package com.fourquant.riqae.pacs;

import com.fourquant.riqae.pacs.CSVDoc.Row;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileReader;
import java.io.PrintStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class CSVDocWriterTest {

  @Test
  public void testWriteToBuffer() throws Exception {
    final CSVDocWriter writer = new CSVDocWriter();
    final CSVDoc CSVDoc = new CSVDoc();
    final Row row = new Row();
    row.setPatientName("John Doe");
    final StringBuffer buffer = new StringBuffer();

    CSVDoc.add(row);

    writer.write(CSVDoc, buffer);

    assertTrue(buffer.toString().contains("John Doe"));
  }

  @Test
  public void testWrite() throws Exception {
    final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    System.setOut(new PrintStream(outContent));

    final CSVDocWriter writer = new CSVDocWriter();
    final CSVDoc CSVDoc = new CSVDoc();
    writer.write(CSVDoc);

    final String header =
          "Patient Name,Patient ID,Study Date,Study Description,Study Instance UID,Series Instance UID,Result";
    assertEquals(
          header,
          outContent.toString().trim());
  }

  @Test
  public void testWriteToFile() throws Exception {
    final CSVDocFactory factory = new CSVDocFactory();
    final CSVDoc CSVDoc =
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
    writer.write(CSVDoc, file);

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
    writer.write(new CSVDoc(), "/does/not/exist");
  }
}