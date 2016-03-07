package com.fourquant.riqae.pacs;

import org.apache.commons.cli.ParseException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static com.fourquant.riqae.pacs.FindScuCommandCreator.main;
import static org.junit.Assert.assertTrue;

public class FindScuCommandCreatorTest {

  private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
  private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();

  @Before
  public void setUpStreams() {
    System.setOut(new PrintStream(outContent));
    System.setErr(new PrintStream(errContent));
  }

  @After
  public void cleanUpStreams() {
    System.setOut(null);
    System.setErr(null);
  }

  @Test
  public void testFindScuCommandCreatorBasic() {

    final String[] args = {"-pn", "Ashlee^Simpson",
          "-pn", "Kate^Moss",
          "-pn", "Donatella^Versace"};
    try {
      main(args);
      final String expected =
            "findscu -c john@localhost:9090 -m PatientName=Ashlee^Simpson";

      assertTrue(outContent.toString().contains(expected));
    } catch (ParseException e) {
      e.printStackTrace();
    }
  }

  @Test
  public void testFindScuCommandCreatorParams() {

    final String[] args = {"-pn", "Ashlee^Simpson",
          "-pn", "Kate^Moss",
          "-pn", "Donatella^Versace",
          "-s", "10.10.22.22",
          "-p", "666",
          "-u", "foo"};
    try {
      main(args);
      final String expected =
            "findscu -c foo@10.10.22.22:666 -m PatientName=Ashlee^Simpson";

      assertTrue(outContent.toString().contains(expected));
    } catch (ParseException e) {
      e.printStackTrace();
    }
  }

}
