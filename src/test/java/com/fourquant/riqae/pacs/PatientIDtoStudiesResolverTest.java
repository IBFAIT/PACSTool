package com.fourquant.riqae.pacs;

import org.junit.Test;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Created by tomjre on 3/13/16.
 */
public class PatientIDtoStudiesResolverTest {


  static final String PATIENTID = "USB000123456";
  static final String EXAMDATE = "20000101-20200101";
  static final String AET = "DO_NOT_EXECUTE@127.1.1.1:11112";

  static final String[] FINDSCU_TESTS_EXPECTED =
        {
              "findscu -L STUDY  -r PatientName -r PatientID -r StudyDate -r StudyDescription -r StudyInstanceUID"
                    + " -m PatientID=USB000123456 -m StudyDate=20000101-20200101 -c DO_NOT_EXECUTE@127.1.1.1:11112"
        };
  private static final String[] MOCRESULTS = {
        "<?xml version=\"1.0\" encoding=\"UTF-8\"?><NativeDicomModel xml:space=\"preserve\"><DicomAttribute keyword=\"SpecificCharacterSet\" tag=\"00080005\" vr=\"CS\"><Value number=\"1\">ISO_IR 100</Value></DicomAttribute><DicomAttribute keyword=\"StudyDate\" tag=\"00080020\" vr=\"DA\"><Value number=\"1\">20141125</Value></DicomAttribute><DicomAttribute keyword=\"QueryRetrieveLevel\" tag=\"00080052\" vr=\"CS\"><Value number=\"1\">STUDY</Value></DicomAttribute><DicomAttribute keyword=\"RetrieveAETitle\" tag=\"00080054\" vr=\"AE\"><Value number=\"1\">OSIRIX</Value></DicomAttribute><DicomAttribute keyword=\"StudyDescription\" tag=\"00081030\" vr=\"LO\"><Value number=\"1\">Rm Colonna Cervicale</Value></DicomAttribute><DicomAttribute keyword=\"PatientName\" tag=\"00100010\" vr=\"PN\"><PersonName number=\"1\"><Alphabetic><FamilyName>ROSSI</FamilyName><GivenName>URBANO</GivenName></Alphabetic></PersonName></DicomAttribute><DicomAttribute keyword=\"PatientID\" tag=\"00100020\" vr=\"LO\"><Value number=\"1\">USB000123456</Value></DicomAttribute><DicomAttribute keyword=\"StudyInstanceUID\" tag=\"0020000D\" vr=\"UI\"><Value number=\"1\">1.2.826.0.1.3680043.2.97.2008.2008.2260375007.1411250805249840</Value></DicomAttribute></NativeDicomModel>",
        "<?xml version=\"1.0\" encoding=\"UTF-8\"?><NativeDicomModel xml:space=\"preserve\"><DicomAttribute keyword=\"SpecificCharacterSet\" tag=\"00080005\" vr=\"CS\"><Value number=\"1\">ISO_IR 100</Value></DicomAttribute><DicomAttribute keyword=\"StudyDate\" tag=\"00080020\" vr=\"DA\"><Value number=\"1\">20141125</Value></DicomAttribute><DicomAttribute keyword=\"QueryRetrieveLevel\" tag=\"00080052\" vr=\"CS\"><Value number=\"1\">STUDY</Value></DicomAttribute><DicomAttribute keyword=\"RetrieveAETitle\" tag=\"00080054\" vr=\"AE\"><Value number=\"1\">OSIRIX</Value></DicomAttribute><DicomAttribute keyword=\"StudyDescription\" tag=\"00081030\" vr=\"LO\"><Value number=\"1\">Rm Colonna Dorsale</Value></DicomAttribute><DicomAttribute keyword=\"PatientName\" tag=\"00100010\" vr=\"PN\"><PersonName number=\"1\"><Alphabetic><FamilyName>ROSSI</FamilyName><GivenName>URBANO</GivenName></Alphabetic></PersonName></DicomAttribute><DicomAttribute keyword=\"PatientID\" tag=\"00100020\" vr=\"LO\"><Value number=\"1\">USB000123456</Value></DicomAttribute><DicomAttribute keyword=\"StudyInstanceUID\" tag=\"0020000D\" vr=\"UI\"><Value number=\"1\">1.2.826.0.1.3680043.2.97.2008.2008.3727469665.1411250805260930</Value></DicomAttribute></NativeDicomModel>",
        "<?xml version=\"1.0\" encoding=\"UTF-8\"?><NativeDicomModel xml:space=\"preserve\"><DicomAttribute keyword=\"SpecificCharacterSet\" tag=\"00080005\" vr=\"CS\"><Value number=\"1\">ISO_IR 100</Value></DicomAttribute><DicomAttribute keyword=\"StudyDate\" tag=\"00080020\" vr=\"DA\"><Value number=\"1\">20141125</Value></DicomAttribute><DicomAttribute keyword=\"QueryRetrieveLevel\" tag=\"00080052\" vr=\"CS\"><Value number=\"1\">STUDY</Value></DicomAttribute><DicomAttribute keyword=\"RetrieveAETitle\" tag=\"00080054\" vr=\"AE\"><Value number=\"1\">OSIRIX</Value></DicomAttribute><DicomAttribute keyword=\"StudyDescription\" tag=\"00081030\" vr=\"LO\"><Value number=\"1\">Rm Colonna Lombosacrale</Value></DicomAttribute><DicomAttribute keyword=\"PatientName\" tag=\"00100010\" vr=\"PN\"><PersonName number=\"1\"><Alphabetic><FamilyName>ROSSI</FamilyName><GivenName>URBANO</GivenName></Alphabetic></PersonName></DicomAttribute><DicomAttribute keyword=\"PatientID\" tag=\"00100020\" vr=\"LO\"><Value number=\"1\">USB000123456</Value></DicomAttribute><DicomAttribute keyword=\"StudyInstanceUID\" tag=\"0020000D\" vr=\"UI\"><Value number=\"1\">1.2.826.0.1.3680043.2.97.2008.2008.1968233823.1411250805255780</Value></DicomAttribute></NativeDicomModel>"
  };  // taken from actual anonymized data

  /**
   * perform actual test on OSIRIX
   */
  public static void main(String[] args) {

  }

  @Test
  public void testCommandGeneratio() {

    try {
      PatientIDtoStudiesResolver psresolver = new PatientIDtoStudiesResolver(AET);
      assertEquals(FINDSCU_TESTS_EXPECTED[0], psresolver.generateCommand(PATIENTID, EXAMDATE));

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Test
  public void testMOCExecute() {
    try {
      PatientIDtoStudiesResolver psresolver = new PatientIDtoStudiesResolver(AET);
      generateMocResults(FindscuExecuter.DO_NOT_EXECUTE_DIR);
      String[] outArray = psresolver.resolveXML(PATIENTID, EXAMDATE);
      int i = 0;
      for (String out : outArray) {
        // System.out.println(out);
        assertEquals(MOCRESULTS[i].trim(), out.trim());
        i++;
      }
    } catch (Exception e) {
      e.printStackTrace();
    }

  }

  @Test
  public void testMOCExecuteWithXMLConverstionToCSV() throws IOException {

    final XML2CSVConverter xml2CSVConverter = new XML2CSVConverter();
    final CSVDocWriter csvDocWriter = new CSVDocWriter();

    final String xml = MOCRESULTS[0];

    final List<DataRow> dataRows = xml2CSVConverter.convert(xml);

    final StringBuffer buffy = new StringBuffer();

    csvDocWriter.write(dataRows, buffy);

  }

  // TODO

  public void generateMocResults(String tempPath) {
    try {
      int i = 1;
      for (String moc : MOCRESULTS) {
        PrintWriter writer = new PrintWriter(tempPath + "r00" + String.valueOf(i) + ".dcm", "UTF-8");
        writer.println(moc);
        writer.close();
        i++;
      }


    } catch (Exception e) {
      e.printStackTrace();
    }

  }

}
