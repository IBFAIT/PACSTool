package com.fourquant.riqae.pacs;

import com.fourquant.riqae.pacs.Message.Row;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class PACSFacadeTest {
  private final static String server = "localhost";
  private final static int port = 8924;
  private final static String user = "admin";
  private final static String victimsName = "John Doe";

  private final PACSFacade pacsFacade = new PACSFacade(server, port, user);

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
    john.setPatientName(victimsName);
    pacsRequest.add(john);

    Message message = pacsFacade.process(pacsRequest);

    Row row = message.iterator().next();
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
