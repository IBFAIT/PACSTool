package com.fourquant.riqae.pacs;

import com.fourquant.riqae.pacs.CSVDoc.Row;
import org.junit.Test;

import java.util.Set;

import static org.junit.Assert.assertEquals;


public class ExecutorTest {

  @Test
  public void testExecute() {
    final Executor executor = new Executor("admin", "localhost", 23);
    final CSVDoc CSVDoc = executor.execute("findscu -c DCMQRSCP@localhost:11112 " +
          "-m PatientName=John^Doe -m\n" +
          "        StudyDate=20110510- -m ModalitiesInStudy=CT");
    final Set<String> patientNames = CSVDoc.getPatientNames();
    assertEquals(1, patientNames.size());
    final Row row = CSVDoc.iterator().next();
    assertEquals("John^Doe", row.getPatientName());

    assertEquals(3, CSVDoc.size());

    for (final Row r : CSVDoc) {
      System.out.println("row = " + r);
    }
  }
}