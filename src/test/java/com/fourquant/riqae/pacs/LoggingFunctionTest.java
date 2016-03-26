package com.fourquant.riqae.pacs;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class LoggingFunctionTest {

  private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
  private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();

  private PrintStream oldOut;
  private PrintStream oldErr;

  @Before
  public void setUpStreams() {
    oldOut = System.out;
    oldErr = System.err;
    System.setOut(new PrintStream(outContent));
    System.setErr(new PrintStream(errContent));
  }

  @After
  public void cleanUpStreams() {
    System.setOut(oldOut);
    System.setErr(oldErr);
  }

  @Test
  public void testExecute() throws NoSuchMethodException,
        IllegalAccessException, InvocationTargetException,
        InstantiationException, LoggingFunctionException {

    final LoggingFunction loggingFunction = new LoggingFunction();

    final Method method =
          LoggingFunctionTest.class.getMethod("calculate", int[].class);

    final Object[] args = {new int[]{2, 3, 5}};

    final Float duration = loggingFunction.execute(method, args);
    Assert.assertTrue(duration >= 1F);

    Assert.assertTrue(outContent.toString().contains("result = 10"));
  }

  public void calculate(int[] args) throws InterruptedException {

    int result = 0;

    for (int arg : args) {
      result += arg;
    }

    System.out.println("result = " + result);

    Thread.sleep(2000);
  }

}
