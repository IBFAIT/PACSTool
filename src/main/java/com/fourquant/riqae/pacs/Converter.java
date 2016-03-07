package com.fourquant.riqae.pacs;

import org.dcm4che3.data.Attributes;
import org.dcm4che3.io.ContentHandlerAdapter;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.ByteArrayInputStream;
import java.io.IOException;

import static java.nio.charset.StandardCharsets.UTF_8;

public class Converter {
  public final CSVDoc xml2Message(final String xml)
        throws IOException, SAXException, ParserConfigurationException {

    final Attributes attributes = xml2dcm(xml);
    final CSVDocFactory CSVDocFactory = new CSVDocFactory();

    final CSVDoc CSVDoc = CSVDocFactory.create(attributes);

    return CSVDoc;
  }

  public final Attributes xml2dcm(String xml) throws ParserConfigurationException,
        SAXException, IOException {

    final Attributes dataset = new Attributes();
    final ContentHandlerAdapter ch = new ContentHandlerAdapter(dataset);

    final SAXParserFactory f = SAXParserFactory.newInstance();
    final SAXParser p = f.newSAXParser();

    p.parse(new InputSource(
          new ByteArrayInputStream(xml.getBytes(UTF_8))), ch);

    return dataset;
  }
}
