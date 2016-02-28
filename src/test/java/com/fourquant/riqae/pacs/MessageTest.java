package com.fourquant.riqae.pacs;

import com.fourquant.riqae.pacs.Message.Row;
import org.junit.Test;

import java.util.Iterator;

import static org.junit.Assert.*;

public class MessageTest {

  @Test
  public void testAdd() throws Exception {
    Message message = new Message();

    message.add(new Row() {
      @Override
      public String getPatientName() {
        return "John Doe";
      }
    });

    message.add(new Row() {
      @Override
      public String getPatientName() {
        return "Donatella Versace";
      }
    });

    Iterator<Row> iterator = message.iterator();
    assertTrue(iterator.hasNext());
    assertEquals("John Doe", iterator.next().getPatientName());
    assertTrue(iterator.hasNext());
    assertEquals("Donatella Versace", iterator.next().getPatientName());
    assertFalse(iterator.hasNext());
    assertTrue(message.getPatientNames().contains("Donatella Versace"));
  }
}
