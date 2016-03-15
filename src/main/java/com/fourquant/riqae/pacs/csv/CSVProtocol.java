package com.fourquant.riqae.pacs.csv;


import org.apache.commons.csv.CSVFormat;

import static org.apache.commons.csv.CSVFormat.DEFAULT;

public interface CSVProtocol {
  String patientNameField = "Patient Name";
  String patientIdField = "Patient ID";
  String studyInstanceUidField = "Study Instance UID";
  String studyDateField = "Study Date";
  String studyDescriptionField = "Study Description";
  String seriesInstanceUIDField = "Series Instance UID";
  String seriesDescriptionField = "Series Description";
  String resultField = "Result";

  CSVFormat format = DEFAULT.
        withDelimiter(';').
        withHeader(
              patientNameField,
              patientIdField,
              studyInstanceUidField,
              studyDateField,
              studyDescriptionField,
              seriesInstanceUIDField,
              seriesDescriptionField,
              resultField).
        withSkipHeaderRecord();
}