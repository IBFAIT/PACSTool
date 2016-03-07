package com.fourquant.riqae.pacs;

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

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.w3c.dom.Node.ELEMENT_NODE;

public class XML2CSV {

  public final CSVDoc convert(final String xml) {

    final DocumentBuilderFactory factory =
          DocumentBuilderFactory.newInstance();

    final DocumentBuilder builder;
    final CSVDoc csvDoc = new CSVDoc();
    try {


      builder = factory.newDocumentBuilder();
      InputStream stream = new ByteArrayInputStream(xml.getBytes(UTF_8));

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

          final CSVDoc.Row row = new CSVDoc.Row();

          for (int j = 0; j < dicomAttributes.getLength(); j++) {
            Node item = dicomAttributes.item(j);

            Element item1 = (Element) item;
            String keyword = item1.getAttribute("keyword");
            String value = dicomAttributes.item(j).getTextContent().trim();

            switch (keyword) {
              case "PatientName":
                row.setPatientName(value);
                break;

              case "PatientID":
                row.setPatientId(value);
                break;

              case "StudyDate":
                row.setStudyDate(value);
                break;

              case "StudyDescription":
                row.setStudyDescription(value);
                break;

              case "StudyInstanceUID":
                row.setStudyInstanceUid(value);
                break;
            }

          }
          csvDoc.add(row);
        }
      }


      return csvDoc;


    } catch (ParserConfigurationException | SAXException | IOException e) {
      e.printStackTrace();
    }


    return null;


  }
}
