package com.fourquant.riqae.pacs;


import org.apache.commons.csv.CSVFormat;

import static org.apache.commons.csv.CSVFormat.DEFAULT;

public interface Protocol {
  String patientNameField = "Patient Name";
  String patientIdField = "Patient ID";
  String studyDate = "Study Date";
  String studyDescription = "Study Description";
  String studyInstanceUidField = "Study Instance UID";
  String seriesInstanceUIDField = "Series Instance UID";
  String result = "Result";

  CSVFormat format = DEFAULT.
        withDelimiter(',').
        withHeader(
              patientNameField,
              patientIdField,
              studyDate,
              studyDescription,
              studyInstanceUidField,
              seriesInstanceUIDField,
              result).
        withSkipHeaderRecord();
}