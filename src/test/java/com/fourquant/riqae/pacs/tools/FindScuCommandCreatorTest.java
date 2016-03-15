package com.fourquant.riqae.pacs.tools;

import org.apache.commons.cli.ParseException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static com.fourquant.riqae.pacs.TestConstants.*;
import static com.fourquant.riqae.pacs.tools.FindScuCommandCreator.main;
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
  public void testFindScuCommandCreatorBasic() throws ParseException {

    final String[] args = {"-pn", nameAshlee,
          "-pn", nameKate,
          "-pn", nameDonatella,
          "-binaryPath", binaryPath};

    main(args);
    final String expected =
          binaryPath + " -r PatientID -c john@localhost:9090 -m PatientName=" +
                "\"" + nameAshlee + "\"";

    assertTrue(outContent.toString().contains(expected));
  }

  @Test
  public void testFindScuCommandCreatorParams() throws ParseException {

    final String[] args = {"-pn", nameAshlee,
          "-pn", nameKate,
          "-pn", nameDonatella,
          "-s", "10.10.22.22",
          "-p", "666",
          "-u", "foo",
          "-binaryPath", binaryPath};

    main(args);
    final String expected =
          binaryPath + " -r PatientID -c foo@10.10.22.22:666 -m PatientName=" + "\"" +
                nameAshlee + "\"";

    assertTrue(outContent.toString().contains(expected));
  }
}