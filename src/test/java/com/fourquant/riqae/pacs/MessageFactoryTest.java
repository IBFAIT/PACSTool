package com.fourquant.riqae.pacs;

import com.fourquant.riqae.pacs.Message.Row;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Set;

import static org.junit.Assert.*;

public class MessageFactoryTest {

  @Test
  public void testCreateFromCommandLineArgs() throws Exception {
    final MessageFactory factory = new MessageFactory();
    final ArrayList<String> patientNames = new ArrayList<String>() {{
      add("Donatella Versace");
      add("John Doe");
      add("Lucky Luke");
    }};

    final Message message = factory.create(patientNames);
    final Row row = message.iterator().next();
    assertEquals("Donatella Versace", row.getPatientName());
  }

  @Test
  public void testCreateFromCSVFile() throws Exception {
    final MessageFactory factory = new MessageFactory();
    final Message message =
          factory.create(
                getClass().getResource("/testRequest.csv").getFile());

    final Set<String> patientNames = message.getPatientNames();
    assertTrue(patientNames.contains("Thomas Re"));
    assertTrue(patientNames.contains("Kevin Mader"));
    assertTrue(patientNames.contains("Flavio Trolese"));
    assertFalse(patientNames.contains("Patient Name"));
  }
}