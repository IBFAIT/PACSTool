package com.fourquant.riqae.pacs;

import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import static com.fourquant.riqae.pacs.Protocol.*;

public class CSVDocReader {
  public final List<DataRow> create(final String filePath) {
    try {
      final Reader in = new FileReader(filePath);

      final CSVParser csvParser = new CSVParser(in, format);
      final List<CSVRecord> records = csvParser.getRecords();

      final List<DataRow> dataRows = new ArrayList<>();

      for (CSVRecord csvRecord : records) {
        dataRows.add(create(csvRecord));
      }

      return dataRows;

    } catch (IOException e) {
      throw new IllegalStateException(e);
    }
  }

  private DataRow create(final CSVRecord csvRecord) {
    final String patientId = csvRecord.get(patientIdField);
    final String patientName = csvRecord.get(patientNameField);
    final String studyDate = csvRecord.get(studyDateField);
    final String studyDescription = csvRecord.get(studyDescriptionField);
    final String seriesDescription = csvRecord.get(seriesDescriptionField);
    final String studyInstanceUid = csvRecord.get(studyInstanceUidField);
    final String seriesInstanceUid = csvRecord.get(seriesInstanceUIDField);

    final DataRow dataRow = new DataRow();
    dataRow.setPatientId(patientId);
    dataRow.setPatientName(patientName);
    dataRow.setStudyDate(studyDate);
    dataRow.setStudyDescription(studyDescription);
    dataRow.setSeriesDescription(seriesDescription);
    dataRow.setStudyInstanceUid(studyInstanceUid);
    dataRow.setSeriesInstanceUID(seriesInstanceUid);
    return dataRow;
  }
}
