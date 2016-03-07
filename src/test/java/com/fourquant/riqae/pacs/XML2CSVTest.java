package com.fourquant.riqae.pacs;


import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class XML2CSVTest {

  @Test
  public final void testXml2CSVConversion() throws IOException {
    final XML2CSV xml2CSV = new XML2CSV();
    final CSVDocWriter csvDocWriter = new CSVDocWriter();

    final String xml = readContent(getPath("/XML2CSVTest.xml"));

    final CSVDoc csvDoc = xml2CSV.convert(xml);

    final StringBuffer buffer = new StringBuffer();

    csvDocWriter.write(csvDoc, buffer);

    final String actual =
          buffer.toString()
                .replace("\n", "").replace("\r", "");

    final String expected =
          readContent(getPath("/XML2CSVTest.csv"))
                .replace("\n", "").replace("\r", "");

    Assert.assertEquals(expected, actual);
  }

  private Path getPath(final String fileName) {

    return FileSystems.getDefault().getPath(getClass().
          getResource(fileName).getPath());
  }

  private String readContent(final Path path) throws IOException {
    String content = "";
    final List<String> lines = Files.readAllLines(path);
    for (final String line : lines) {
      content += line;
      content += System.getProperty("line.separator");
    }
    return content;
  }
}
