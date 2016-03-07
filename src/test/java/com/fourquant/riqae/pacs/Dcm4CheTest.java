package com.fourquant.riqae.pacs;


import org.dcm4che3.data.*;
import org.junit.Test;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

import static org.dcm4che3.data.Tag.PatientName;
import static org.junit.Assert.assertEquals;

public class Dcm4CheTest {

  private static final String REFERENCE_XML =
        "<?xml version=\"1.0\" encoding=\"UTF-8\"?><NativeDicomModel " +
              "xml:space=\"preserve\"><DicomAttribute " +
              "keyword=\"SpecificCharacterSet\" tag=\"00080005\" vr=\"CS\">" +
              "<Value number=\"1\">ISO 2022 IR 87</Value></DicomAttribute>" +
              "<DicomAttribute keyword=\"ImageType\" tag=\"00080008\" vr=\"CS\">" +
              "<Value number=\"1\">DERIVED</Value><Value number=\"2\"/>" +
              "<Value number=\"3\">PRIMARY</Value><Value number=\"4\"/>" +
              "<Value number=\"5\">TEST</Value></DicomAttribute><DicomAttribute " +
              "keyword=\"AccessionNumber\" tag=\"00080050\" vr=\"SH\"/>" +
              "<DicomAttribute keyword=\"SourceImageSequence\" tag=\"00082112\" " +
              "vr=\"SQ\"><Item number=\"1\"><DicomAttribute " +
              "keyword=\"ReferencedSOPClassUID\" tag=\"00081150\" vr=\"UI\">" +
              "<Value number=\"1\">1.2.840.10008.5.1.4.1.1.2</Value>" +
              "</DicomAttribute><DicomAttribute " +
              "keyword=\"ReferencedSOPInstanceUID\" tag=\"00081155\" vr=\"UI\">" +
              "<Value number=\"1\">1.2.3.4</Value></DicomAttribute></Item>" +
              "</DicomAttribute><DicomAttribute tag=\"00090002\" " +
              "privateCreator=\"PRIVATE\" vr=\"OB\"><InlineBinary>AAE=" +
              "</InlineBinary></DicomAttribute><DicomAttribute " +
              "keyword=\"PatientName\" tag=\"00100010\" vr=\"PN\"><PersonName " +
              "number=\"1\"><Alphabetic><FamilyName>af</FamilyName><GivenName>" +
              "ag</GivenName></Alphabetic><Ideographic><FamilyName>if</FamilyName>" +
              "<GivenName>ig</GivenName></Ideographic><Phonetic><FamilyName>pf" +
              "</FamilyName><GivenName>pg</GivenName></Phonetic></PersonName>" +
              "</DicomAttribute><DicomAttribute keyword=\"FrameTime\" " +
              "tag=\"00181063\" vr=\"DS\"><Value number=\"1\">33</Value>" +
              "</DicomAttribute><DicomAttribute keyword=\"SamplesPerPixel\" " +
              "tag=\"00280002\" vr=\"US\"><Value number=\"1\">1</Value>" +
              "</DicomAttribute><DicomAttribute keyword=\"NumberOfFrames\" " +
              "tag=\"00280008\" vr=\"IS\"><Value number=\"1\">1</Value>" +
              "</DicomAttribute><DicomAttribute keyword=\"FrameIncrementPointer\" " +
              "tag=\"00280009\" vr=\"AT\"><Value number=\"1\">00181063</Value>" +
              "</DicomAttribute><DicomAttribute keyword=\"OverlayData\" " +
              "tag=\"60003000\" vr=\"OW\"><BulkData uuid=\"someuuid\"/>" +
              "</DicomAttribute><DicomAttribute keyword=\"PixelData\" " +
              "tag=\"7FE00010\" vr=\"OB\"><BulkData " +
              "uri=\"file:/PixelData?offsets=0,1234&amp;lengths=0,5678\"/>" +
              "</DicomAttribute></NativeDicomModel>";

  @Test
  public void testXml2Dcm() throws IOException, SAXException,
        ParserConfigurationException {
    final Converter converter = new Converter();
    final Attributes dataset = converter.xml2dcm(REFERENCE_XML);

    Attributes referenceDataset = createTestDataset();
    // the null value will be an empty string after converting to XML,
    // therefore we have to adapt the reference
    referenceDataset.setString(Tag.ImageType, VR.CS, "DERIVED", "",
          "PRIMARY", "", "TEST");

    assertEquals(referenceDataset, dataset);
  }

  @Test
  public void testParseXml() throws IOException, SAXException,
        ParserConfigurationException {
    final Converter converter = new Converter();
    Attributes dataset = converter.xml2dcm("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
          "<NativeDicomModel xml:space=\"preserve\">\n" +
          "<DicomAttribute keyword=\"SpecificCharacterSet\" tag=\"00080005\" vr=\"CS\"><Value number=\"1\">ISO_IR 100</Value></DicomAttribute>\n" +
          "<DicomAttribute keyword=\"PatientName\" tag=\"00100010\" vr=\"DA\"><Value number=\"1\">Donatella Versace</Value></DicomAttribute><DicomAttribute keyword=\"StudyDate\" tag=\"00080020\" vr=\"DA\"><Value number=\"1\">20110308</Value></DicomAttribute><DicomAttribute keyword=\"QueryRetrieveLevel\" tag=\"00080052\" vr=\"CS\"><Value number=\"1\">STUDY</Value></DicomAttribute>\n" +
          "<DicomAttribute keyword=\"RetrieveAETitle\" tag=\"00080054\" vr=\"AE\"><Value number=\"1\">OSIRIX</Value></DicomAttribute>\n" +
          "<DicomAttribute keyword=\"StudyDescription\" tag=\"00081030\" vr=\"LO\"><Value number=\"1\">Ct Thorax</Value></DicomAttribute>\n" +
          "<DicomAttribute keyword=\"PatientID\" tag=\"00100020\" vr=\"LO\"><Value number=\"1\">USB0003138461</Value></DicomAttribute>\n" +
          "<DicomAttribute keyword=\"StudyInstanceUID\" tag=\"0020000D\" vr=\"UI\"><Value number=\"1\">1.2.840.113619.6.95.31.0.3.4.1.4285.13.23320240</Value></DicomAttribute>\n" +
          "</NativeDicomModel>");
    assertEquals("Donatella Versace", dataset.getValue(PatientName));
  }

  private Attributes createTestDataset() {
    Attributes dataset = new Attributes();
    dataset.setString(Tag.SpecificCharacterSet, VR.CS, "ISO 2022 IR 87");
    dataset.setString(Tag.ImageType, VR.CS, "DERIVED", null,
          "PRIMARY", "", "TEST");
    dataset.setNull(Tag.AccessionNumber, VR.SH);
    Attributes item = new Attributes(2);
    dataset.newSequence(Tag.SourceImageSequence, 1).add(item);
    item.setString(Tag.ReferencedSOPClassUID, VR.UI, UID.CTImageStorage);
    item.setString(Tag.ReferencedSOPInstanceUID, VR.UI, "1.2.3.4");
    dataset.setString(PatientName, VR.PN, "af^ag=if^ig=pf^pg");
    dataset.setBytes("PRIVATE", 0x00090002, VR.OB, new byte[]{0, 1});
    dataset.setDouble(Tag.FrameTime, VR.DS, 33.0);
    dataset.setInt(Tag.SamplesPerPixel, VR.US, 1);
    dataset.setInt(Tag.NumberOfFrames, VR.IS, 1);
    dataset.setInt(Tag.FrameIncrementPointer, VR.AT, Tag.FrameTime);
    dataset.setValue(Tag.OverlayData, VR.OW, new BulkData("someuuid", null,
          false));
    Fragments frags = dataset.newFragments(Tag.PixelData, VR.OB, 2);
    frags.add(null);
    frags.add(new BulkData(null, "file:/PixelData?offset=1234&length=5678",
          false));

    return dataset;
  }
}