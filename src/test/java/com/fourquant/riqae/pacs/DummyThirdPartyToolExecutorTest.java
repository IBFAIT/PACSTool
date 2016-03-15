package com.fourquant.riqae.pacs;

import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

public class DummyThirdPartyToolExecutorTest {
  @Test
  public void testFindDonatella() throws IOException {

    final String[] dataFiles
          = new String[]{"donatella.xml"};

    final DummyThirdPartyToolExecutor executor =
          new DummyThirdPartyToolExecutor(dataFiles);

      final String[] result = executor.execute("findscu -...");
      Assert.assertTrue(result[0].contains("Donatella"));
  }
}
