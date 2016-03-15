package com.fourquant.riqae.pacs.csv;


import org.junit.Assert;
import org.junit.Test;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static java.nio.file.FileSystems.getDefault;

public class XML2CSVConverterTest {

  @Test
  public final void testXml2CSVConversion()
        throws IOException, ParserConfigurationException, SAXException {

    final XML2CSVConverter xml2CSVConverter = new XML2CSVConverter();
    final CSVDocWriter csvDocWriter = new CSVDocWriter();

    final String xml = readContent(getPath("/XML2CSVTest.xml"));

    final List<DataRow> dataRows = xml2CSVConverter.convert(xml);

    final StringBuffer buffer = new StringBuffer();

    csvDocWriter.write(dataRows, buffer);

    final String actual =
          buffer.toString()
                .replace("\n", "").replace("\r", "");

    final String expected =
          readContent(getPath("/XML2CSVTest.csv"))
                .replace("\n", "").replace("\r", "");

    Assert.assertEquals(expected, actual);
  }

  private Path getPath(final String fileName) {

    return getDefault().getPath(getClass().
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
