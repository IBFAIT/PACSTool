package com.fourquant.riqae.pacs;

import org.junit.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.fourquant.riqae.pacs.TestConstants.*;
import static org.junit.Assert.*;

public class CSVDocFactoryTest {

  @Test
  public void testCreateFromCommandLineArgs() throws Exception {
    final CSVDocFactory factory = new CSVDocFactory();
    final ArrayList<String> patientNames = new ArrayList<String>() {{
      add(TestConstants.nameDonatella);
      add("John^Doe");
      add("Lucky^Luke");
    }};

    final List<DataRow> CSVDoc = factory.create(patientNames);
    final DataRow dataRow = CSVDoc.iterator().next();
    assertEquals(TestConstants.nameDonatella, dataRow.getPatientName());
  }

  @Test
  public void testCreateFromCSVFile() throws Exception {
    final CSVDocFactory factory = new CSVDocFactory();
    final List<DataRow> CSVDoc =
          factory.create(
                getClass().getResource("/names.csv").getFile());

    final Set<String> patientNames = new HashSet<>();

    for (DataRow dataRow : CSVDoc) {
      patientNames.add(dataRow.getPatientName());
    }

    assertTrue(patientNames.contains(nameAshlee));
    assertTrue(patientNames.contains(nameDonatella));
    assertTrue(patientNames.contains(nameKate));
    assertFalse(patientNames.contains("Patient Name"));
  }
}