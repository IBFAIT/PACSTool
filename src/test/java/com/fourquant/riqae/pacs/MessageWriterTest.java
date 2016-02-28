package com.fourquant.riqae.pacs;

import com.fourquant.riqae.pacs.Message.Row;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileReader;
import java.io.PrintStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class MessageWriterTest {

  @Test
  public void testWriteToBuffer() throws Exception {
    final MessageWriter writer = new MessageWriter();
    final Message message = new Message();
    final Row row = new Row();
    row.setPatientName("John Doe");
    final StringBuffer buffer = new StringBuffer();

    message.add(row);

    writer.write(message, buffer);

    assertTrue(buffer.toString().contains("John Doe"));
  }

  @Test
  public void testWrite() throws Exception {
    final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    PrintStream out = System.out;
    System.setOut(new PrintStream(outContent));

    final MessageWriter writer = new MessageWriter();
    final Message message = new Message();
    writer.write(message);

    assertEquals(
          "Patient Name,Patient ID,Exam ID,Series ID,Technique,Type",
          outContent.toString().trim());

    System.setOut(out);
  }

  @Test
  public void testWriteToFile() throws Exception {
    final MessageFactory factory = new MessageFactory();
    final Message message =
          factory.create(
                getClass().
                      getResource("/testRequest.csv").getFile());

    try (BufferedReader br =
               new BufferedReader(
                     new FileReader(
                           getClass().
                                 getResource("/testRequest.csv").getFile()))) {
      String line;
      while ((line = br.readLine()) != null) {
        System.out.println(line);
      }
    }

    final MessageWriter writer = new MessageWriter();

    final String file = getClass().getResource("/dummy.csv").getFile();
    System.out.println("file = " + file);
    writer.write(message, file);

    try (BufferedReader br = new BufferedReader(new FileReader(file))) {
      String line;
      while ((line = br.readLine()) != null) {
        System.out.println(line);
      }
    }

  }


  @Test(expected = IllegalStateException.class)
  public void testWriteWithException() throws Exception {
    final MessageWriter writer = new MessageWriter();
    writer.write(new Message(), "/does/not/existx");
  }
}