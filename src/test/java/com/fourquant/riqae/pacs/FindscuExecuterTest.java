package com.fourquant.riqae.pacs;

import org.apache.commons.cli.ParseException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Created by tomjre on 3/10/16.
 */
public class FindscuExecuterTest {

    static final String TEMP_PATH= "/tmp/";

    static final String[] FINDSCU_TESTS_IN =
            {"findscu  -r PatientID -r StudyDate -r StudyDescription -r StudyInstanceUID -L STUDY -c DO_NOT_EXECUTE@localhost:11112  -m PatientID=USB03570011 -m StudyDate=20100101-20170101",
                    "findscu  --do-not-execute -r PatientID -r StudyDate -r StudyDescription -r StudyInstanceUID -L STUDY -c OSIRIX@localhost:11112  -m PatientID=USB03570011 -m StudyDate=20100101-20170101 --out-dir /Users/tomjre/Desktop/USB_Projects/out"
            };
    static final String[] FINDSCU_TESTS_EXPECTED =
            {"findscu  -r PatientID -r StudyDate -r StudyDescription -r StudyInstanceUID -L STUDY -c DO_NOT_EXECUTE@localhost:11112  -m PatientID=USB03570011 -m StudyDate=20100101-20170101 --out-dir /tmp/ --xml ",
                    "findscu  --do-not-execute -r PatientID -r StudyDate -r StudyDescription -r StudyInstanceUID -L STUDY -c OSIRIX@localhost:11112  -m PatientID=USB03570011 -m StudyDate=20100101-20170101  --out-dir /tmp/ --xml --out-dir /Users/tomjre/Desktop/USB_Projects/out"
            };

    @Before
    public void setUpStreams() {

    }

    @After
    public void cleanUpStreams() {
        System.setOut(null);
        System.setErr(null);
    }





    @Test
    public void testCommandAppendingOutDir() {

        try {
            FindscuExecuter findscuExe = new FindscuExecuter(TEMP_PATH);
            assertEquals(FINDSCU_TESTS_EXPECTED[0], findscuExe.getFinalCommand(FINDSCU_TESTS_IN[0]));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testCommandInsertingOutDir() {

        try {
            FindscuExecuter findscuExe = new FindscuExecuter(TEMP_PATH);
            assertEquals(FINDSCU_TESTS_EXPECTED[1], findscuExe.getFinalCommand(FINDSCU_TESTS_IN[1]));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Test
    public void testExecute() {
        try {
            FindscuExecuter findscuExe = new FindscuExecuter();
            generateMocResults(findscuExe.DO_NOT_EXECUTE_DIR);
            String[] outArray= findscuExe.execute(FINDSCU_TESTS_IN[0]);
            int i=0;
            for (String out : outArray) {
               // last char may be an extra linefeed.
                assertEquals(MOCRESULTS[i].substring(0,MOCRESULTS[i].length()-1),out.substring(0,out.length()-1));
                i++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private static final String[] MOCRESULTS = {
            "<?xml version=\"1.0\" encoding=\"UTF-8\"?><NativeDicomModel xml:space=\"preserve\"><DicomAttribute keyword=\"SpecificCharacterSet\" tag=\"00080005\" vr=\"CS\"><Value number=\"1\">ISO_IR 100</Value></DicomAttribute><DicomAttribute keyword=\"StudyDate\" tag=\"00080020\" vr=\"DA\"><Value number=\"1\">20110308</Value></DicomAttribute><DicomAttribute keyword=\"QueryRetrieveLevel\" tag=\"00080052\" vr=\"CS\"><Value number=\"1\">STUDY</Value></DicomAttribute><DicomAttribute keyword=\"RetrieveAETitle\" tag=\"00080054\" vr=\"AE\"><Value number=\"1\">OSIRIX</Value></DicomAttribute><DicomAttribute keyword=\"StudyDescription\" tag=\"00081030\" vr=\"LO\"><Value number=\"1\">Ct Thorax</Value></DicomAttribute><DicomAttribute keyword=\"PatientID\" tag=\"00100020\" vr=\"LO\"><Value number=\"1\">USB0001</Value></DicomAttribute><DicomAttribute keyword=\"PatientName\" tag=\"00100010\" vr=\"LO\"><Value number=\"1\">VerdiJoe</Value></DicomAttribute><DicomAttribute keyword=\"StudyInstanceUID\" tag=\"0020000D\" vr=\"UI\"><Value number=\"1\">23320240</Value></DicomAttribute></NativeDicomModel>",
            "<?xml version=\"1.0\" encoding=\"UTF-8\"?><NativeDicomModel xml:space=\"preserve\"><DicomAttribute keyword=\"SpecificCharacterSet\" tag=\"00080005\" vr=\"CS\"><Value number=\"1\">ISO_IR 100</Value></DicomAttribute><DicomAttribute keyword=\"StudyDate\" tag=\"00080020\" vr=\"DA\"><Value number=\"1\">20110308</Value></DicomAttribute><DicomAttribute keyword=\"QueryRetrieveLevel\" tag=\"00080052\" vr=\"CS\"><Value number=\"1\">STUDY</Value></DicomAttribute><DicomAttribute keyword=\"RetrieveAETitle\" tag=\"00080054\" vr=\"AE\"><Value number=\"1\">OSIRIX</Value></DicomAttribute><DicomAttribute keyword=\"StudyDescription\" tag=\"00081030\" vr=\"LO\"><Value number=\"1\">Ct Thorax</Value></DicomAttribute><DicomAttribute keyword=\"PatientID\" tag=\"00100020\" vr=\"LO\"><Value number=\"1\">USB0002</Value></DicomAttribute><DicomAttribute keyword=\"PatientName\" tag=\"00100010\" vr=\"LO\"><Value number=\"1\">GiallaMaria</Value></DicomAttribute><DicomAttribute keyword=\"StudyInstanceUID\" tag=\"0020000D\" vr=\"UI\"><Value number=\"1\">233202123</Value></DicomAttribute></NativeDicomModel>",
            "<?xml version=\"1.0\" encoding=\"UTF-8\"?><NativeDicomModel xml:space=\"preserve\"><DicomAttribute keyword=\"SpecificCharacterSet\" tag=\"00080005\" vr=\"CS\"><Value number=\"1\">ISO_IR 100</Value></DicomAttribute><DicomAttribute keyword=\"StudyDate\" tag=\"00080020\" vr=\"DA\"><Value number=\"1\">20100202</Value></DicomAttribute><DicomAttribute keyword=\"QueryRetrieveLevel\" tag=\"00080052\" vr=\"CS\"><Value number=\"1\">STUDY</Value></DicomAttribute><DicomAttribute keyword=\"RetrieveAETitle\" tag=\"00080054\" vr=\"AE\"><Value number=\"1\">OSIRIX</Value></DicomAttribute><DicomAttribute keyword=\"StudyDescription\" tag=\"00081030\" vr=\"LO\"><Value number=\"1\">Ct Thorax</Value></DicomAttribute><DicomAttribute keyword=\"PatientID\" tag=\"00100020\" vr=\"LO\"><Value number=\"1\">USB0003</Value></DicomAttribute><DicomAttribute keyword=\"PatientName\" tag=\"00100010\" vr=\"LO\"><Value number=\"1\">RossiMario</Value></DicomAttribute><DicomAttribute keyword=\"StudyInstanceUID\" tag=\"0020000D\" vr=\"UI\"><Value number=\"1\">23320323</Value></DicomAttribute></NativeDicomModel>"
    };

   public void generateMocResults(String tempPath) {
     try {
         int i=1;
         for (String moc : MOCRESULTS) {
             PrintWriter writer = new PrintWriter(tempPath+"r00"+String.valueOf(i)+".dcm", "UTF-8");
             writer.println(moc);
             writer.close();
             i++;
         }


     } catch (Exception e) {
         e.printStackTrace();
     }

   }





}
