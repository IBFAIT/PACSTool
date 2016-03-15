package com.fourquant.riqae.pacs.csv;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import static com.fourquant.riqae.pacs.TestConstants.*;
import static java.util.stream.Collectors.toSet;
import static org.junit.Assert.*;

public class DataRowFactoryTest {

  @Test
  public void testCreateFromCommandLineArgs() throws Exception {
    final DataRowFactory factory = new DataRowFactory();
    final ArrayList<String> patientNames = new ArrayList<String>() {{
      add(nameDonatella);
      add(nameKate);
      add("Lucky^Luke");
    }};

    final List<DataRow> dataRows = factory.create(patientNames);
    final Iterator<DataRow> iterator = dataRows.iterator();
    final DataRow dataRow = iterator.next();

    assertEquals(nameDonatella, dataRow.getPatientName());
    assertEquals(nameKate, iterator.next().getPatientName());
  }

  @Test
  public void testCreateFromCSVFile() throws Exception {
    final DataRowFactory factory = new DataRowFactory();
    final List<DataRow> dataRows =
          factory.create(
                getClass().getResource("/names.csv").getFile());

    final Set<String> patientNames =
          dataRows.stream().map(DataRow::getPatientName).collect(toSet());

    assertTrue(patientNames.contains(nameAshlee));
    assertTrue(patientNames.contains(nameDonatella));
    assertTrue(patientNames.contains(nameKate));
    assertFalse(patientNames.contains("Patient Name"));
  }
}