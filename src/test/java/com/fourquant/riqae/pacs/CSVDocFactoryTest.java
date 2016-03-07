package com.fourquant.riqae.pacs;

import com.fourquant.riqae.pacs.CSVDoc.Row;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Set;

import static org.junit.Assert.*;

public class CSVDocFactoryTest {

  @Test
  public void testCreateFromCommandLineArgs() throws Exception {
    final CSVDocFactory factory = new CSVDocFactory();
    final ArrayList<String> patientNames = new ArrayList<String>() {{
      add("Donatella^Versace");
      add("John^Doe");
      add("Lucky^Luke");
    }};

    final CSVDoc CSVDoc = factory.create(patientNames);
    final Row row = CSVDoc.iterator().next();
    assertEquals("Donatella^Versace", row.getPatientName());
  }

  @Test
  public void testCreateFromCSVFile() throws Exception {
    final CSVDocFactory factory = new CSVDocFactory();
    final CSVDoc CSVDoc =
          factory.create(
                getClass().getResource("/names.csv").getFile());

    final Set<String> patientNames = CSVDoc.getPatientNames();
    assertTrue(patientNames.contains("Ashlee^Simpson"));
    assertTrue(patientNames.contains("Kate^Moss"));
    assertTrue(patientNames.contains("Donatella^Versace"));
    assertFalse(patientNames.contains("Patient Name"));
  }
}