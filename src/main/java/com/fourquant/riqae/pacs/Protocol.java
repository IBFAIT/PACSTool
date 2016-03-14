package com.fourquant.riqae.pacs;


import org.apache.commons.csv.CSVFormat;

import static org.apache.commons.csv.CSVFormat.DEFAULT;

public interface Protocol {
  String patientNameField = "Patient Name";
  String patientIdField = "Patient ID";
  String studyDateField = "Study Date";
  String studyDescriptionField = "Study Description";
  String seriesDescriptionField = "Series Description";
  String studyInstanceUidField = "Study Instance UID";
  String seriesInstanceUIDField = "Series Instance UID";
  String resultField = "Result";

  CSVFormat format = DEFAULT.
        withDelimiter(';').
        withHeader(
              patientNameField,
              patientIdField,
              studyDateField,
              studyDescriptionField,
              seriesDescriptionField,
              studyInstanceUidField,
              seriesInstanceUIDField,
              resultField).
        withSkipHeaderRecord();
}