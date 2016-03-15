package com.fourquant.riqae.pacs;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.List;

import static com.fourquant.riqae.pacs.TestConstants.binaryPath;

/**
 * Created by tomjre on 3/10/16.
 */
public class FindScuExecutorTest {

  static final String TEMP_PATH = "/tmp/";

  static final String[] FINDSCU_TESTS_IN =
        {"findscu  -r PatientID -r StudyDate -r StudyDescription -r StudyInstanceUID -L STUDY -c DO_NOT_EXECUTE@localhost:11112  -m PatientID=USB03570011 -m StudyDate=20100101-20170101",
              "findscu  --do-not-execute -r PatientID -r StudyDate -r StudyDescription -r StudyInstanceUID -L STUDY -c OSIRIX@localhost:11112  -m PatientID=USB03570011 -m StudyDate=20100101-20170101 --out-dir /Users/tomjre/Desktop/USB_Projects/out"
        };
  static final String[] FINDSCU_TESTS_EXPECTED =
        {"findscu  -r PatientID -r StudyDate -r StudyDescription -r StudyInstanceUID -L STUDY -c DO_NOT_EXECUTE@localhost:11112  -m PatientID=USB03570011 -m StudyDate=20100101-20170101 --out-dir /tmp/ --xml ",
              "findscu  --do-not-execute -r PatientID -r StudyDate -r StudyDescription -r StudyInstanceUID -L STUDY -c OSIRIX@localhost:11112  -m PatientID=USB03570011 -m StudyDate=20100101-20170101  --out-dir /tmp/ --xml --out-dir /Users/tomjre/Desktop/USB_Projects/out"
        };
  private static final String[] MOCRESULTS = {
        "<?xml version=\"1.0\" encoding=\"UTF-8\"?><NativeDicomModel xml:space=\"preserve\"><DicomAttribute keyword=\"SpecificCharacterSet\" tag=\"00080005\" vr=\"CS\"><Value number=\"1\">ISO_IR 100</Value></DicomAttribute><DicomAttribute keyword=\"StudyDate\" tag=\"00080020\" vr=\"DA\"><Value number=\"1\">20110308</Value></DicomAttribute><DicomAttribute keyword=\"QueryRetrieveLevel\" tag=\"00080052\" vr=\"CS\"><Value number=\"1\">STUDY</Value></DicomAttribute><DicomAttribute keyword=\"RetrieveAETitle\" tag=\"00080054\" vr=\"AE\"><Value number=\"1\">OSIRIX</Value></DicomAttribute><DicomAttribute keyword=\"StudyDescription\" tag=\"00081030\" vr=\"LO\"><Value number=\"1\">Ct Thorax</Value></DicomAttribute><DicomAttribute keyword=\"PatientID\" tag=\"00100020\" vr=\"LO\"><Value number=\"1\">USB0001</Value></DicomAttribute><DicomAttribute keyword=\"PatientName\" tag=\"00100010\" vr=\"LO\"><Value number=\"1\">VerdiJoe</Value></DicomAttribute><DicomAttribute keyword=\"StudyInstanceUID\" tag=\"0020000D\" vr=\"UI\"><Value number=\"1\">23320240</Value></DicomAttribute></NativeDicomModel>",
        "<?xml version=\"1.0\" encoding=\"UTF-8\"?><NativeDicomModel xml:space=\"preserve\"><DicomAttribute keyword=\"SpecificCharacterSet\" tag=\"00080005\" vr=\"CS\"><Value number=\"1\">ISO_IR 100</Value></DicomAttribute><DicomAttribute keyword=\"StudyDate\" tag=\"00080020\" vr=\"DA\"><Value number=\"1\">20110308</Value></DicomAttribute><DicomAttribute keyword=\"QueryRetrieveLevel\" tag=\"00080052\" vr=\"CS\"><Value number=\"1\">STUDY</Value></DicomAttribute><DicomAttribute keyword=\"RetrieveAETitle\" tag=\"00080054\" vr=\"AE\"><Value number=\"1\">OSIRIX</Value></DicomAttribute><DicomAttribute keyword=\"StudyDescription\" tag=\"00081030\" vr=\"LO\"><Value number=\"1\">Ct Thorax</Value></DicomAttribute><DicomAttribute keyword=\"PatientID\" tag=\"00100020\" vr=\"LO\"><Value number=\"1\">USB0002</Value></DicomAttribute><DicomAttribute keyword=\"PatientName\" tag=\"00100010\" vr=\"LO\"><Value number=\"1\">GiallaMaria</Value></DicomAttribute><DicomAttribute keyword=\"StudyInstanceUID\" tag=\"0020000D\" vr=\"UI\"><Value number=\"1\">233202123</Value></DicomAttribute></NativeDicomModel>",
        "<?xml version=\"1.0\" encoding=\"UTF-8\"?><NativeDicomModel xml:space=\"preserve\"><DicomAttribute keyword=\"SpecificCharacterSet\" tag=\"00080005\" vr=\"CS\"><Value number=\"1\">ISO_IR 100</Value></DicomAttribute><DicomAttribute keyword=\"StudyDate\" tag=\"00080020\" vr=\"DA\"><Value number=\"1\">20100202</Value></DicomAttribute><DicomAttribute keyword=\"QueryRetrieveLevel\" tag=\"00080052\" vr=\"CS\"><Value number=\"1\">STUDY</Value></DicomAttribute><DicomAttribute keyword=\"RetrieveAETitle\" tag=\"00080054\" vr=\"AE\"><Value number=\"1\">OSIRIX</Value></DicomAttribute><DicomAttribute keyword=\"StudyDescription\" tag=\"00081030\" vr=\"LO\"><Value number=\"1\">Ct Thorax</Value></DicomAttribute><DicomAttribute keyword=\"PatientID\" tag=\"00100020\" vr=\"LO\"><Value number=\"1\">USB0003</Value></DicomAttribute><DicomAttribute keyword=\"PatientName\" tag=\"00100010\" vr=\"LO\"><Value number=\"1\">RossiMario</Value></DicomAttribute><DicomAttribute keyword=\"StudyInstanceUID\" tag=\"0020000D\" vr=\"UI\"><Value number=\"1\">23320323</Value></DicomAttribute></NativeDicomModel>"
  };

  @Before
  public void setUpStreams() {

  }

  @After
  public void cleanUpStreams() {
    System.setOut(null);
    System.setErr(null);
  }


  public void generateMocResults(String tempPath) throws FileNotFoundException, UnsupportedEncodingException {
    int i = 1;
    for (String moc : MOCRESULTS) {
      PrintWriter writer = new PrintWriter(tempPath + "r00" + String.valueOf(i) + ".dcm", "UTF-8");
      writer.println(moc);
      writer.close();
      i++;
    }
  }

  @Test
  public void testOnFlaviosMacBook() throws IOException, InterruptedException {
    final FindScuCommandCreator findScuCommandCreator = new FindScuCommandCreator();
    final String findScuStatement = findScuCommandCreator.createFindScuStatement(
          "BREBIX", "OSIRIX", "localhost", "11112", binaryPath);
//    final FindscuExecuter findscuExe = new FindscuExecuter();

    final FindScuExecutor findscuExe = new FindScuExecutor();
    final String[] execute = findscuExe.execute(findScuStatement);
    for (final String s : execute) {

      final XML2CSVConverter xml2CSVConverter = new XML2CSVConverter();
      List<DataRow> convert = xml2CSVConverter.convert(s);
      for (DataRow dataRow : convert) {

        Assert.assertEquals("XsaDYa", dataRow.getPatientId());
      }

    }
  }
}