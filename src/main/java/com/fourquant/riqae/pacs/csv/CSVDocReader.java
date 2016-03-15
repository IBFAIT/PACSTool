package com.fourquant.riqae.pacs.csv;

import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.List;

import static com.fourquant.riqae.pacs.csv.Protocol.*;
import static java.util.stream.Collectors.toList;

public class CSVDocReader {

  public final List<DataRow> create(final String filePath) {
    try {
      final Reader in = new FileReader(filePath);

      final CSVParser csvParser = new CSVParser(in, format);
      final List<CSVRecord> records = csvParser.getRecords();

      return records.stream().map(this::create).collect(toList());

    } catch (IOException e) {
      throw new IllegalStateException(e);
    }
  }

  private DataRow create(final CSVRecord csvRecord) {
    final String patientId = csvRecord.get(patientIdField);
    final String patientName = csvRecord.get(patientNameField);
    final String studyDate = csvRecord.get(studyDateField);
    final String studyInstanceUid = csvRecord.get(studyInstanceUidField);
    final String studyDescription = csvRecord.get(studyDescriptionField);
    final String seriesInstanceUid = csvRecord.get(seriesInstanceUIDField);
    final String seriesDescription = csvRecord.get(seriesDescriptionField);

    final DataRow dataRow = new DataRow();
    dataRow.setPatientId(patientId);
    dataRow.setPatientName(patientName);
    dataRow.setStudyInstanceUid(studyInstanceUid);
    dataRow.setStudyDate(studyDate);
    dataRow.setStudyDescription(studyDescription);
    dataRow.setSeriesInstanceUID(seriesInstanceUid);
    dataRow.setSeriesDescription(seriesDescription);
    return dataRow;
  }
}
