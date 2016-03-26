package com.fourquant.riqae.pacs.csv;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;

import static com.fourquant.riqae.pacs.csv.CSVProtocol.*;
import static java.nio.charset.StandardCharsets.UTF_8;
import static javax.xml.parsers.DocumentBuilderFactory.newInstance;
import static org.w3c.dom.Node.ELEMENT_NODE;

public class XML2CSVConverterService {

  public static Set<CSVDataRow> convert(final String xmlContent)
        throws IOException, ParserConfigurationException, SAXException {

    final DocumentBuilderFactory factory = newInstance();

    final DocumentBuilder builder;

    final Set<CSVDataRow> csvDataRows = new HashSet<>();

    builder = factory.newDocumentBuilder();

    final InputStream stream =
          new ByteArrayInputStream(xmlContent.getBytes(UTF_8));

    final Document doc = builder.parse(stream);
    final Element root = doc.getDocumentElement();

    root.normalize();

    final NodeList nativeDicomModels =
          doc.getElementsByTagName("NativeDicomModel");

    for (int i = 0; i < nativeDicomModels.getLength(); i++) {

      if (nativeDicomModels.item(i).getNodeType() == ELEMENT_NODE) {

        final NodeList dicomAttributes =
              ((Element) nativeDicomModels.item(i))
                    .getElementsByTagName("DicomAttribute");

        final CSVDataRow row = new CSVDataRow();

        for (int j = 0; j < dicomAttributes.getLength(); j++) {

          final Node node = dicomAttributes.item(j);

          final Element element = (Element) node;

          final String keyword = element.getAttribute("keyword");

          final String value = dicomAttributes.item(j).getTextContent().trim();

          switch (keyword) {

            case PATIENT_NAME_KEYWORD:

              row.setPatientName(value);
              break;

            case PATIENT_ID_KEYWORD:

              row.setPatientID(value);
              break;

            case STUDY_INSTANCE_UI_KEYWORD:

              row.setStudyInstanceUID(value);
              break;

            case STUDY_DATE_KEYWORD:

              row.setStudyDate(value);
              break;

            case STUDY_DESCRIPTION_KEYWORD:

              row.setStudyDescription(value);
              break;

            case SERIES_INSTANCE_UID_KEYWORD:

              row.setSeriesInstanceUID(value);
              break;

            case SERIES_DESCRIPTION_KEYWORD:

              row.setSeriesDescription(value);
              break;
          }
        }
        csvDataRows.add(row);
      }
    }

    return csvDataRows;
  }

  public static Set<CSVDataRow> createCSVDataRows(final String[] xmlResults)
        throws IOException, ParserConfigurationException, SAXException {

    final Set<CSVDataRow> csvDataRows = new HashSet<>();

    for (final String xml : xmlResults) {

      final XML2CSVConverterService service = new XML2CSVConverterService();

      final Set<CSVDataRow> dataRows = convert(xml);

      csvDataRows.addAll(dataRows);
    }

    return csvDataRows;
  }
}