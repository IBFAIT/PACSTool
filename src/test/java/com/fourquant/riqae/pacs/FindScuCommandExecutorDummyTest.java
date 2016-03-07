package com.fourquant.riqae.pacs;


import org.dcm4che3.data.*;
import org.dcm4che3.io.SAXWriter;
import org.dcm4che3.util.StringUtils;
import org.dcm4che3.util.TagUtils;
import org.junit.Test;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.sax.TransformerHandler;
import javax.xml.transform.stream.StreamResult;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.dcm4che3.data.Tag.PatientName;
import static org.junit.Assert.*;

public class FindScuCommandExecutorDummyTest {

  @Test
  public void testExecute() throws IOException, SAXException, ParserConfigurationException {

    final FindScuCommandExecutorDummy executor =
          new FindScuCommandExecutorDummy();

    final String result =
          executor.execute("findscu -c DCMQRSCP@localhost:11112 -r PatientID -m " +
                "PatientName=Donatella^Versace");
    System.out.println("result = " + result);

    assertTrue(
          result.contains(
                "<Value number=\"1\"> PatientName=Donatella^Versace</Value>"));

    final Converter converter = new Converter();
    final Attributes attributes = converter.xml2dcm(result);

    assertNotNull(attributes);

    //retrieve patient name from attributes
    final String patientName = (String) attributes.getValue(PatientName);
    System.out.println("attributes.getString(PatientName) = " + attributes.getString(PatientName));
    System.out.println("attributes.getProperties() = " + attributes.getProperties());

    SAXTransformerFactory tf = (SAXTransformerFactory) TransformerFactory.newInstance();
    try {
      TransformerHandler handler = tf.newTransformerHandler();
      ByteArrayOutputStream xmlOutput = new ByteArrayOutputStream();
      handler.setResult(new StreamResult(xmlOutput));
      SAXWriter writer = new SAXWriter(handler);

      String privateCreator = attributes.getPrivateCreator(PatientName);
      System.out.println("privateCreator = " + privateCreator);

      try {
        final SpecificCharacterSet cs = attributes.getSpecificCharacterSet();
        attributes.accept(new Attributes.Visitor() {
          @Override
          public boolean visit(Attributes attrs, int tag, VR vr, Object value) throws Exception {
            writeAttribute(tag, vr, value, cs, attrs);


            System.out.println("TagUtils.shortToHexString(i) = " + TagUtils.shortToHexString(tag));
            return false;
          }
        }, false);
        attributes.accept((attr, i, vr, o) -> {
          System.out.println("attributes = " + attr);
          System.out.println("i = " + i);
          System.out.println("o = " + o);
          System.out.println("o.getClass().getName() = " + o.getClass().getName());
          System.out.println("vr = " + vr);
          String keyword = ElementDictionary.keywordOf(i, privateCreator);
          System.out.println("keyword = " + keyword);
          char[] buffer = new char[256 * 4];
          char[] buf = buffer;
          String s = vr.toString(o, attr.bigEndian(), i, "");
          System.out.println("s = " + s);
          for (int off = 0, totlen = s.length(); off < totlen; ) {
            int len = Math.min(totlen - off, buf.length);
            s.getChars(off, off += len, buf, 0);
            System.out.println("s = " + s);
            System.out.println("buf = " + buf);
            System.out.println("len = " + len);

          }

          return true;
        }, true);
      } catch (Exception e) {
        e.printStackTrace();
      }

      System.out.println("writer.getClass().getName() = " + writer.getClass().getName());
      writer.write(attributes);
      String s = xmlOutput.toString(StandardCharsets.UTF_8.name());
      System.out.println("s = " + s);

    } catch (TransformerConfigurationException e) {
      e.printStackTrace();
    }

    assertEquals("Donatella^Versace", patientName);


    final CSVDoc CSVDoc = converter.xml2Message(result);
    assertNotNull(CSVDoc);
  }

  private void writeAttribute(int tag, VR vr, Object value,
                              SpecificCharacterSet cs, Attributes attrs) {
    if (TagUtils.isGroupLength(tag))
      return;
    System.out.println("TagUtils.toHexString(tag) = " + TagUtils.toHexString(tag));
//    gen.writeStartObject(TagUtils.toHexString(tag));
    System.out.println("vr.name() = " + vr.name());
    //gen.write("vr", vr.name());
    System.out.println("value instanceof Value = " + (value instanceof Value));
    System.out.println("value = " + value);

    String privateCreator = attrs.getPrivateCreator(PatientName);
    String keyword = ElementDictionary.keywordOf(tag, privateCreator);
    System.out.println("keyword = " + keyword);
    System.out.println("privateCreator = " + privateCreator);
    System.out.println("value = " + value);
    String v = (String) value;
    System.out.println("v.substring(keyword.length()) = " + v.substring(keyword.length() + 2));

    System.out.println("vr = " + vr);
    System.out.println("attrs.bigEndian() = " + attrs.bigEndian());
    System.out.println("attrs.getSpecificCharacterSet(vr) = " + attrs.getSpecificCharacterSet(vr));
    switch (vr) {
      case AE:
      case AS:
      case AT:
      case CS:
      case DA:
      case DS:
      case DT:
      case IS:
      case LO:
      case LT:
      case PN:
      case SH:
      case ST:
      case TM:
      case UC:
      case UI:
      case UR:
      case UT:
        System.out.println("        writeStringValues(vr, val, bigEndian, cs);");
        Object o = vr.toStrings(value, attrs.bigEndian(), cs);

        String[] ss = (o instanceof String[])
              ? (String[]) o
              : new String[]{(String) o};
        for (String s : ss) {
          if (s == null || s.isEmpty())
            System.out.println("null");
          else switch (vr) {
            case DS:
              try {
                System.out.println("StringUtils.parseDS(s) = " + StringUtils.parseDS(s));
              } catch (NumberFormatException e) {
                throw new IllegalStateException();
              }
              break;
            case IS:
              try {
                System.out.println("StringUtils.parseIS(s) = " + StringUtils.parseIS(s));
              } catch (NumberFormatException e) {
                throw new IllegalStateException();
              }
              break;
            case PN:
              throw new IllegalStateException();

            default:
              System.out.println("s = " + s);
          }
        }


//        writeStringValues(vr, val, bigEndian, cs);
        break;
      case FL:
      case FD:
        System.out.println("writeDoubleValues(vr, val, bigEndian);");
//        writeDoubleValues(vr, val, bigEndian);
        break;
      case SL:
      case SS:
      case UL:
      case US:
        System.out.println("writeIntValues(vr, val, bigEndian);");
//        writeIntValues(vr, val, bigEndian);
        break;
      case OB:
      case OD:
      case OF:
      case OL:
      case OW:
      case UN:
        System.out.println("writeInlineBinary(vr, (byte[]) val, bigEndian, preserve);");
//        writeInlineBinary(vr, (byte[]) val, bigEndian, preserve);
        break;
      case SQ:
        assert true;
    }

//    if (value instanceof Value)
//      writeValue((Value) value, attrs.bigEndian());
//    else
//      writeValue(vr, value, attrs.bigEndian(),
//            attrs.getSpecificCharacterSet(vr), true);
//    gen.writeEnd();
  }

}