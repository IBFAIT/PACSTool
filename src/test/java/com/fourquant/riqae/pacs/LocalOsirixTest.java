package com.fourquant.riqae.pacs;

import com.fourquant.riqae.pacs.csv.CSVDataRow;
import com.fourquant.riqae.pacs.csv.CSVReaderService;
import com.fourquant.riqae.pacs.csv.CSVWriterService;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;

import static com.fourquant.riqae.pacs.Dcm4CheWrapperHelper.createTempDirectory;
import static com.fourquant.riqae.pacs.TestConstants.*;
import static com.fourquant.riqae.pacs.tools.Operation.*;
import static java.util.Collections.addAll;
import static org.junit.Assert.assertFalse;

public class LocalOsirixTest {

  private Dcm4CheWrapper dcm4CheWrapper =
        new Dcm4CheWrapper(userName, server, port);

  private CSVReaderService csvReaderService = new CSVReaderService();

  private CSVWriterService csvWriterService = new CSVWriterService();

  @Test
  public void testWorkflow() throws IOException, LoggingFunctionException {

    /*

      1. Parse csv file with PatientNames (osirixNames.csv)

      2. Resolve PatientNames to PatientIDs

      3. Write csv file with PatientNames and PatientIDs -> namesAndIds.csv

      4. Edit file manually

      5. Parse csv file with PatientNames and PatientIDs (namesAndIds.csv)

      6. Resolve PatientIds to StudyInstanceUIDs

      7. Write csv file with StudyInstanceUIDs -> studyInstanceUIDs.csv

      8. Edit file manually

      9. Parse csv file with StudyInstanceUIDs (studyInstanceUIDs.csv)

      10. Resolve StudyInstanceUIDs to SeriesInstanceUIDs

      11. Write csv file with SeriesInstanceUIDs -> seriesInstanceUIDs.csv

      12. Edit file manually

      13. Fetch images

      14. Have RIQAE process the images

     */


    /*
      1. Parse csv file with PatientNames (osirixNames.csv)
    */
    final String namesFile = "/osirixNames.csv";

    final Set<CSVDataRow> namesDataRows =
          csvReaderService.createCSVDataRows(
                getClass().getResource(namesFile).getFile());

    /*
      2. Resolve PatientNames to PatientIDs
     */

    final Set<CSVDataRow> nameAndIdDataRows =
          dcm4CheWrapper.execute(RESOLVE_PATIENT_IDS).on(namesDataRows);

    /*
      3. Write csv file with PatientNames and PatientIDs -> namesAndIds.csv
     */
    final Path tmpDirectoryNamesAndIds =
          createTempDirectory().toPath();

    final String namesAndIdFile =
          tmpDirectoryNamesAndIds.toString() + "/namesAndIDs.csv";

    csvWriterService.writeCSVFile(nameAndIdDataRows, namesAndIdFile);

    /*
      4. Edit file manually
     */

    /*
      5. Parse csv file with PatientNames and PatientIDs (namesAndIds.csv)
     */
    final Set<CSVDataRow> editedNameAndIdDataRows =
          csvReaderService.createCSVDataRows(namesAndIdFile);

    /*
      6. Resolve PatientIds to StudyInstanceUIDs
     */
    final Set<CSVDataRow> studyIdDataRows =
          dcm4CheWrapper.execute(RESOLVE_STUDY_INSTANCE_UIDS).
                on(editedNameAndIdDataRows);

    for (final CSVDataRow csvDataRow : studyIdDataRows) {
      assertFalse(csvDataRow.getPatientName().equals("Brebix"));
    }

    /*
      7. Write csv file with StudyInstanceUIDs -> studyInstanceUIDs.csv
     */
    final Path tmpDirectoryStudyInstanceUIDs =
          createTempDirectory().toPath();

    final String studyInstanceUIDFile =
          tmpDirectoryStudyInstanceUIDs.toString() +
                "/studyInstanceUIDs.csv";

    csvWriterService.writeCSVFile(studyIdDataRows, studyInstanceUIDFile);

    /*
      8. Edit file manually
     */

    /*
      9. Parse csv file with StudyInstanceUIDs (studyInstanceUIDs.csv)
     */
    final Set<CSVDataRow> editedStudyIdDataRows =
          csvReaderService.createCSVDataRows(studyInstanceUIDFile);

    /*
      10. Resolve StudyInstanceUIDs to SeriesInstanceUIDs
     */
    final Set<CSVDataRow> seriesIdDataRows =
          dcm4CheWrapper.execute(RESOLVE_SERIES_INSTANCE_UIDS).
                on(editedStudyIdDataRows);

    for (final CSVDataRow csvDataRow : seriesIdDataRows) {
      assertFalse(csvDataRow.getPatientName().equals("Brebix"));
    }

    /*
      11. Write csv file with SeriesInstanceUIDs -> seriesInstanceUIDs.csv
     */
    final Path tmpDirectorySeriesInstanceUIDs =
          createTempDirectory().toPath();

    final String seriesInstanceUIDsFile =
          tmpDirectorySeriesInstanceUIDs.toString()
                + "/seriesInstanceUIDs.csv";

    csvWriterService.writeCSVFile(seriesIdDataRows, seriesInstanceUIDsFile);

    /*
      12. Edit file manually
     */

    /*
      13. Fetch images
     */

    final Set<File> imageFiles = fetchSeries(seriesIdDataRows);

    for (final File file : imageFiles) {
      System.out.println("file = " + file);
    }

//    todo: dcm2jpg laufen lassen...


    /*
      14. Have RIQAE process the images
    */

    /*
      15. Clean up
    */

    new File(tmpDirectoryNamesAndIds.toUri()).deleteOnExit();

    new File(tmpDirectoryStudyInstanceUIDs.toUri()).deleteOnExit();

    new File(tmpDirectorySeriesInstanceUIDs.toUri()).deleteOnExit();

  }

  private Set<File> fetchSeries(final Set<CSVDataRow> seriesIDDataRows)
        throws IOException, LoggingFunctionException {

    final Set<File> files = new HashSet<>();

    for (final CSVDataRow csvDataRow : seriesIDDataRows) {

      final File[] fileResults =
            dcm4CheWrapper.fetchSeries(
                  csvDataRow.getSeriesInstanceUID());

      addAll(files, fileResults);
    }

    return files;
  }
}
