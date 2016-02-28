package com.fourquant.riqae.pacs;


import org.apache.commons.csv.CSVFormat;

import static org.apache.commons.csv.CSVFormat.DEFAULT;

public interface Protocol {
  String patientNameField = "Patient Name";
  String patientIdField = "Patient ID";
  String examIdField = "Exam ID";
  String seriesIdField = "Series ID";
  String techniqueField = "Technique";
  String typeField = "Type";

  CSVFormat format = DEFAULT.
        withDelimiter(',').
        withHeader(
              patientNameField,
              patientIdField,
              examIdField,
              seriesIdField,
              techniqueField,
              typeField).
        withSkipHeaderRecord();
}