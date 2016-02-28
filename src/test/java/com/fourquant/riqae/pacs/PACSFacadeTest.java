package com.fourquant.riqae.pacs;

import com.fourquant.riqae.pacs.Message.Row;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class PACSFacadeTest {

  private PACSFacade pacsFacade = new PACSFacade("localhost", 8924);

  @Test
  public void testResponseNotNull() {
    final Message pacsMapObject = new Message();
    final Message message = pacsFacade.process(pacsMapObject);
    assertNotNull(message);
  }

  @Test
  public void testQuery() {

    Message pacsRequest = new Message();
    final Row john = new Row();
    john.setPatientName("John Doe");
    pacsRequest.add(john);

    Message message = pacsFacade.process(pacsRequest);

    Row row = message.iterator().next();
    assertNotNull(row);

    assertEquals("John Doe", row.getPatientName());
  }

  @Test
  public void testGetServer() throws Exception {
    assertEquals("localhost", pacsFacade.getServer());
  }

  @Test
  public void testGetPort() throws Exception {
    assertEquals(8924, pacsFacade.getPort());
  }
}
