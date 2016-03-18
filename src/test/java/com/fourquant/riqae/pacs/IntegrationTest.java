package com.fourquant.riqae.pacs;

import com.fourquant.riqae.pacs.csv.CSVDataRow;
import com.fourquant.riqae.pacs.csv.CSVReaderService;
import com.fourquant.riqae.pacs.csv.CSVWriterService;
import com.fourquant.riqae.pacs.csv.XML2CSVConverterService;
import org.junit.Test;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;

import static com.fourquant.riqae.pacs.TestConstants.*;
import static java.util.Collections.addAll;
import static org.junit.Assert.assertFalse;

public class IntegrationTest {

  private SCUOperationWrapper scuOperationWrapper =
        new SCUOperationWrapper(userName, server, port);

  private CSVReaderService csvReaderService = new CSVReaderService();

  private CSVWriterService csvWriterService = new CSVWriterService();

  private XML2CSVConverterService xml2CSVConverterService =
        new XML2CSVConverterService();

  @Test
  public void testEverything() throws IOException, InterruptedException, ParserConfigurationException, SAXException {

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

    final Set<CSVDataRow> nameAndIdDataRows = new HashSet<>();

    /*
      2. Resolve PatientNames to PatientIDs
     */
    for (final CSVDataRow nameDataRow : namesDataRows) {

      final String[] xmlResults =
            scuOperationWrapper.resolvePatientIDs(nameDataRow.getPatientName());

      nameAndIdDataRows.addAll(
            xml2CSVConverterService.createCSVDataRows(xmlResults));
    }

    /*
      3. Write csv file with PatientNames and PatientIDs -> namesAndIds.csv
     */
    final Path tmpDirectoryNamesAndIds =
          scuOperationWrapper.createTempDirectory().toPath();

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
          resolveStudyInstanceIDs(editedNameAndIdDataRows);

    for (final CSVDataRow csvDataRow : studyIdDataRows) {
      assertFalse(csvDataRow.getPatientName().equals("Brebix"));
    }

    /*
      7. Write csv file with StudyInstanceUIDs -> studyInstanceUIDs.csv
     */
    final Path tmpDirectoryStudyInstanceUIDs =
          scuOperationWrapper.createTempDirectory().toPath();

    final String studyInstanceUIDFile = tmpDirectoryStudyInstanceUIDs.toString() +
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
          resolveSeriesInstanceUIds(editedStudyIdDataRows);

    for (final CSVDataRow csvDataRow : seriesIdDataRows) {
      assertFalse(csvDataRow.getPatientName().equals("Brebix"));
    }

    /*
      11. Write csv file with SeriesInstanceUIDs -> seriesInstanceUIDs.csv
     */
    final Path tmpDirectorySeriesInstanceUIDs =
          scuOperationWrapper.createTempDirectory().toPath();

    final String seriesInstanceUIDsFile = tmpDirectorySeriesInstanceUIDs.toString()
          + "/seriesInstanceUIDs.csv";

    csvWriterService.writeCSVFile(seriesIdDataRows, seriesInstanceUIDsFile);

    /*
      12. Edit file manually
     */

    /*
      13. Fetch images
     */
    final Set<File> imageFiles = fetchSeries(seriesIdDataRows);

    new File(tmpDirectoryNamesAndIds.toUri()).deleteOnExit();
    new File(tmpDirectoryStudyInstanceUIDs.toUri()).deleteOnExit();
    new File(tmpDirectorySeriesInstanceUIDs.toUri()).deleteOnExit();
  }

  private Set<CSVDataRow> resolveStudyInstanceIDs(
        final Set<CSVDataRow> nameAndIdDataRows)
        throws IOException, InterruptedException, ParserConfigurationException,
        SAXException {

    final Set<CSVDataRow> studyIdDataRows = new HashSet<>();

    for (final CSVDataRow row : nameAndIdDataRows) {

      final String[] xmlResults =
            scuOperationWrapper.resolveStudyInstanceUIDs(
                  row.getPatientID());

      studyIdDataRows.addAll(
            xml2CSVConverterService.createCSVDataRows(xmlResults));
    }
    return studyIdDataRows;
  }

  private Set<File> fetchSeries(final Set<CSVDataRow> seriesIDDataRows)
        throws IOException, InterruptedException, ParserConfigurationException,
        SAXException {

    final Set<File> files = new HashSet<>();

    for (final CSVDataRow csvDataRow : seriesIDDataRows) {

      final File[] fileResults =
            scuOperationWrapper.fetchSeries(
                  csvDataRow.getSeriesInstanceUID());

      addAll(files, fileResults);
    }

    return files;
  }

  private Set<CSVDataRow> resolveSeriesInstanceUIds(
        final Set<CSVDataRow> studyInstanceUidDataRows)
        throws IOException, InterruptedException, ParserConfigurationException,
        SAXException {

    final Set<CSVDataRow> seriesInstanceUIDDataRows = new HashSet<>();

    for (final CSVDataRow row : studyInstanceUidDataRows) {

      final String[] xmlResults =
            scuOperationWrapper.resolveSeriesInstanceUIDs(
                  row.getStudyInstanceUID());

      seriesInstanceUIDDataRows.addAll(
            xml2CSVConverterService.createCSVDataRows(xmlResults));
    }
    return seriesInstanceUIDDataRows;
  }
}
