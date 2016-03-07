package com.fourquant.riqae.pacs;

import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;

import static org.w3c.dom.Node.ELEMENT_NODE;

public class DomTest {
  @Test
  public void testParseDom()
        throws ParserConfigurationException, IOException, SAXException {

    final File xmlFile =
          new File(
                getClass().getResource("/data.xml").getFile());

    final DocumentBuilderFactory factory =
          DocumentBuilderFactory.newInstance();

    final DocumentBuilder builder = factory.newDocumentBuilder();

    final Document doc = builder.parse(xmlFile);

    final Element root = doc.getDocumentElement();
    root.normalize();

    final NodeList nativeDicomModels =
          doc.getElementsByTagName("NativeDicomModel");

    for (int i = 0; i < nativeDicomModels.getLength(); i++) {

      if (nativeDicomModels.item(i).getNodeType() == ELEMENT_NODE) {

        final NodeList dicomAttributes =
              ((Element) nativeDicomModels.item(i))
                    .getElementsByTagName("DicomAttribute");

        for (int j = 0; j < dicomAttributes.getLength(); j++) {
          Node item = dicomAttributes.item(j);

          Element item1 = (Element) item;
          String keyword = item1.getAttribute("keyword");
          System.out.println(keyword + " -> " + dicomAttributes.item(j).getTextContent().trim());
        }
      }
    }
  }
}