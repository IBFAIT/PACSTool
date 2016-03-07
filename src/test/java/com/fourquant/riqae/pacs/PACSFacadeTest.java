package com.fourquant.riqae.pacs;

import com.fourquant.riqae.pacs.CSVDoc.Row;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.*;

public class PACSFacadeTest {
  private final static String server = "localhost";
  private final static int port = 8924;
  private final static String user = "admin";
  private final static String victimsName = "John^Doe";

  private final PACSFacade pacsFacade = new PACSFacade(server, port, user);

  @Test
  public void testResponseNotNull() {
    final CSVDoc pacsMapObject = new CSVDoc();
    final CSVDoc CSVDoc = pacsFacade.process(pacsMapObject);
    assertNotNull(CSVDoc);
  }

  @Test
  public void testProcess() {
    CSVDocFactory CSVDocFactory = new CSVDocFactory();
    final List<String> patientNames = new ArrayList<>();
    patientNames.add("Test^Process");
    final CSVDoc CSVDoc = CSVDocFactory.create(patientNames);

    final CSVDoc response = pacsFacade.process(CSVDoc);
    final Set<String> returnedPatientNames = response.getPatientNames();
    assertTrue(returnedPatientNames.contains("Test^Process"));
  }

  @Test
  public void testQuery() {

    CSVDoc pacsRequest = new CSVDoc();
    final Row john = new Row();
    john.setPatientName(victimsName);
    pacsRequest.add(john);

    CSVDoc CSVDoc = pacsFacade.process(pacsRequest);

    Row row = CSVDoc.iterator().next();
    assertNotNull(row);

    assertEquals(victimsName, row.getPatientName());
  }

  @Test
  public void testGetServer() throws Exception {
    assertEquals(server, pacsFacade.getServer());
  }

  @Test
  public void testGetPort() throws Exception {
    assertEquals(port, pacsFacade.getPort());
  }
}
