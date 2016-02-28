package com.fourquant.riqae.pacs;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.Assert.assertEquals;

public class PACSToolTest {


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
  public void testOut() {
    System.out.print("hello");
    assertEquals("hello", outContent.toString());
  }


  @Test
  public void testErr() {
    System.err.print("hello again");
    assertEquals("hello again", errContent.toString());
  }

}
