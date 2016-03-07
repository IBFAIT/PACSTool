package com.fourquant.riqae.pacs;

import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.List;

public interface PACSFacade {
  List<DataRow> process(final List<DataRow> input) throws IOException, SAXException, ParserConfigurationException;
}
