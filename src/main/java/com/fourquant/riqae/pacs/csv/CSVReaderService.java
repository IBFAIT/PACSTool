package com.fourquant.riqae.pacs.csv;

import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.HashSet;
import java.util.Set;

import static com.fourquant.riqae.pacs.csv.CSVProtocol.*;
import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toSet;

public class CSVReaderService {

  public final Set<CSVDataRow> createDataRows(final String filePath)
        throws IOException {

    final Reader in = new FileReader(filePath);

    final CSVParser csvParser = new CSVParser(in, CSV_FORMAT);

    final Set<CSVRecord> records = new HashSet<>(csvParser.getRecords());

    return records.stream().map(this::createDataRow).collect(toSet());
  }

  public final CSVDataRow createDataRow(final CSVRecord csvRecord) {

    final String patientID = csvRecord.get(PATIENT_ID_FIELD);
    final String patientName = csvRecord.get(PATIENT_NAME_FIELD);
    final String studyDate = csvRecord.get(STUDY_DATE_FIELD);
    final String studyInstanceUid = csvRecord.get(STUDY_INSTANCE_UID_FIELD);
    final String studyDescription = csvRecord.get(STUDY_DESCRIPTION_FIELD);
    final String seriesInstanceUid = csvRecord.get(SERIES_INSTANCE_UID_FIELD);
    final String seriesDescription = csvRecord.get(SERIES_DESCRIPTION_FIELD);

    final CSVDataRow CSVDataRow = new CSVDataRow();
    CSVDataRow.setPatientID(patientID);
    CSVDataRow.setPatientName(patientName);
    CSVDataRow.setStudyInstanceUID(studyInstanceUid);
    CSVDataRow.setStudyDate(studyDate);
    CSVDataRow.setStudyDescription(studyDescription);
    CSVDataRow.setSeriesInstanceUID(seriesInstanceUid);
    CSVDataRow.setSeriesDescription(seriesDescription);

    return CSVDataRow;
  }

  public final Set<CSVDataRow> createDataRowsWithNames(
        final Set<String> patientNames) {

    final Set<CSVDataRow> csvDataRows = new HashSet<>();
    for (String patientName : patientNames) {
      final CSVDataRow CSVDataRow = new CSVDataRow();
      CSVDataRow.setPatientName(patientName);
      csvDataRows.add(CSVDataRow);
    }

    return csvDataRows;
  }

  public final Set<CSVDataRow> createDataRowsWithNames(
        final String[] patientNames) {

    return createDataRowsWithNames(
          new HashSet<>(
                asList(patientNames)));
  }

  public final Set<CSVDataRow> createCSVDataRows(final String csvFile)
        throws IOException {

    final CSVReaderService csvReaderService = new CSVReaderService();

    return csvReaderService.createDataRows(csvFile);
  }
}
