package com.fourquant.riqae.pacs;

import java.io.IOException;
import java.util.List;

/**
 *  Given a PatientID, and date range,
 * find list of studies.  Produce csv of studies.
 * Created by tomjre on 3/12/16.
 */
public class PatientIDtoStudiesResolver {

    private static final String FINDSCU= "findscu -L STUDY ";
    private static final String SPACE= " ";
    private static final String REQUESTS= " -r PatientName -r PatientID -r StudyDate -r StudyDescription -r StudyInstanceUID";
    private static final String PATIENTID_TAG= " -m PatientID=";
    private static final String STUDYDATE_TAG= " -m StudyDate=";
    private static final String PACS_CONNECTION_FLAG= " -c ";
    private static final String[] EMPTY_RESULT= {};

    private String _PACSconnectionParameters;
    public PatientIDtoStudiesResolver(String PACSconnectionParameters) {
        _PACSconnectionParameters= PACSconnectionParameters;
    }

    /**
     * Retrieve XML list of Patient Studies for specific PatientID
     * and Date range.
     *
     * @param patientID hospital ID for desired Patient
     * @param dateRange range of dates in form: YYYYMMDD-YYYYMMDD
     * @return results of PACS query in XML form for one patient
     */
    public String[] resolveXML(String patientID, String dateRange) {
        // generate specific find command based on this
        String[] result= EMPTY_RESULT;
        String cmd= generateCommand(patientID, dateRange);
        try {
            FindscuExecuter findscuExe = new FindscuExecuter();
            result= findscuExe.execute(cmd);
        }
        catch (IOException e) {
            System.out.print(e);
        }
        return result;
    }

    protected String generateCommand(String patientID, String dateRange) {
        return FINDSCU+REQUESTS+PATIENTID_TAG+patientID+STUDYDATE_TAG+dateRange
                +PACS_CONNECTION_FLAG+_PACSconnectionParameters;
    }
}
