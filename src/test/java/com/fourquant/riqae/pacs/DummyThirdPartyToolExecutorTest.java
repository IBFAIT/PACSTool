package com.fourquant.riqae.pacs;

import org.junit.Test;

import java.io.IOException;

public class DummyThirdPartyToolExecutorTest {
  @Test
  public void testFoo() {

    final String[] dataFiles
          = new String[]{"donatella.xml", "kate.xml", "ashlee.xml"};

    final DummyThirdPartyToolExecutor executor =
          new DummyThirdPartyToolExecutor(dataFiles);

    try {
      final String[] result = executor.execute("find xy");
      System.out.println("result = " + result);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
