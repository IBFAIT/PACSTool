package com.fourquant.riqae.pacs;

import java.io.IOException;

/**
 * Given a specific StudyInstanceUID, find all Series.
 * Created by tomjre on 3/12/16.
 */
public class StudyUIDtoSeriesResolver {

    private static final String FINDSCU= "findscu -L SERIES ";
    private static final String SPACE= " ";
    private static final String REQUESTS=
            " -r PatientName -r PatientID -r StudyDate -r StudyDescription -r SeriesDescription"
                    + " -r StudyInstanceUID -r SeriesInstanceUID ";
    private static final String STUDYUID_TAG= " -m StudyInstanceUID=";
    private static final String PACS_CONNECTION_FLAG= " -c ";
    private static final String[] EMPTY_RESULT= {};

    private String _PACSconnectionParameters;
    public StudyUIDtoSeriesResolver(String PACSconnectionParameters) {
        _PACSconnectionParameters= PACSconnectionParameters;
    }

    /**
     * Retrieve list of Series for one specific Study
     *
     * @param studyInstanceUID universal identifier for study
     * @return XML results of query
     */
    public String[] resolveXML(String studyInstanceUID) {
        // generate specific find command based on this
        String[] result= EMPTY_RESULT;
        String cmd= generateCommand(studyInstanceUID);
        try {
            FindscuExecuter findscuExe = new FindscuExecuter();
            result= findscuExe.execute(cmd);
        }
        catch (IOException e) {
            System.out.print(e);
        }
        return result;
    }

    protected String generateCommand(String studyInstanceUID) {
        return FINDSCU+REQUESTS+STUDYUID_TAG+studyInstanceUID
                +PACS_CONNECTION_FLAG+_PACSconnectionParameters;
    }


    /** Perform test from command line */
    public static void main(String[] args) {
        try {
            String aet= args[0];
            String studyInstanceUID= args[1];
            StudyUIDtoSeriesResolver psresolver= new StudyUIDtoSeriesResolver(aet);
            String[] outArray= psresolver.resolveXML(studyInstanceUID);
            int i=0;
            for (String out : outArray) {
                System.out.println(out);
            }
        } catch (Exception e) {
            System.out.println("usage: StudyUIDtoSeriesResolverTest AET@IP:port StudyInstanceUID");
        }
    }
}
