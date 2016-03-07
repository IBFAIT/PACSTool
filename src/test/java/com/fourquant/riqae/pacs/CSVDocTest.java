package com.fourquant.riqae.pacs;

import com.fourquant.riqae.pacs.CSVDoc.Row;
import org.junit.Test;

import java.util.Iterator;

import static org.junit.Assert.*;

public class CSVDocTest {

  @Test
  public void testAdd() throws Exception {
    CSVDoc CSVDoc = new CSVDoc();

    CSVDoc.add(new Row() {
      @Override
      public String getPatientName() {
        return "John Doe";
      }
    });

    CSVDoc.add(new Row() {
      @Override
      public String getPatientName() {
        return "Donatella Versace";
      }
    });

    Iterator<Row> iterator = CSVDoc.iterator();
    assertTrue(iterator.hasNext());
    assertEquals("John Doe", iterator.next().getPatientName());
    assertTrue(iterator.hasNext());
    assertEquals("Donatella Versace", iterator.next().getPatientName());
    assertFalse(iterator.hasNext());
    assertTrue(CSVDoc.getPatientNames().contains("Donatella Versace"));
  }
}
