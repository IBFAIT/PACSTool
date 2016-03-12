package com.fourquant.riqae.pacs;


import org.apache.commons.csv.CSVFormat;

import static org.apache.commons.csv.CSVFormat.DEFAULT;

public interface Protocol {
  String patientNameField = "PatientName";
  String patientIdField = "PatientID";
  String studyDateField = "StudyDate";
  String studyDescriptionField = "StudyDescription";
  String seriesDescriptionFiedl = "SeriesDescription";
  String studyInstanceUidField = "StudyInstance UID";
  String seriesInstanceUIDField = "SeriesInstance UID";
  String resultField = "Result";

  CSVFormat format = DEFAULT.
        withDelimiter(';').
        withHeader(
              patientNameField,
              patientIdField,
              studyDateField,
              studyDescriptionField,
              seriesDescriptionFiedl,
              studyInstanceUidField,
              seriesInstanceUIDField,
              resultField).
        withSkipHeaderRecord();
}