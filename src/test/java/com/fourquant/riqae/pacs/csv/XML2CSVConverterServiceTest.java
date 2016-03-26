package com.fourquant.riqae.pacs.csv;


import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Set;

import static com.fourquant.riqae.pacs.csv.XML2CSVConverterService.convert;
import static java.nio.file.FileSystems.getDefault;

public class XML2CSVConverterServiceTest {

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
  public final void testXml2CSVConversion()
        throws IOException, ParserConfigurationException, SAXException {

    final CSVWriterService csvWriterService = new CSVWriterService();

    final String xml = readContent(getPath("/XML2CSVTest.xml"));

    final Set<CSVDataRow> CSVDataRows = convert(xml);

    final StringBuffer buffer = new StringBuffer();

    csvWriterService.writeDataRows(CSVDataRows, buffer);

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
