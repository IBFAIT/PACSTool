package com.fourquant.riqae.pacs.csv;


import org.apache.commons.csv.CSVFormat;

import static org.apache.commons.csv.CSVFormat.DEFAULT;

public interface CSVProtocol {
  String PATIENT_NAME_FIELD = "Patient Name";
  String PATIENT_NAME_KEYWORD = "PatientName";
  String PATIENT_ID_FIELD = "Patient ID";
  String PATIENT_ID_KEYWORD = "PatientID";
  String STUDY_INSTANCE_UID_FIELD = "Study Instance UID";
  String STUDY_INSTANCE_UI_KEYWORD = "StudyInstanceUID";
  String STUDY_DATE_FIELD = "Study Date";
  String STUDY_DATE_KEYWORD = "StudyDate";
  String STUDY_DESCRIPTION_FIELD = "Study Description";
  String STUDY_DESCRIPTION_KEYWORD = "StudyDescription";
  String SERIES_INSTANCE_UID_FIELD = "Series Instance UID";
  String SERIES_INSTANCE_UID_KEYWORD = "SeriesInstanceUID";
  String SERIES_DESCRIPTION_FIELD = "Series Description";
  String SERIES_DESCRIPTION_KEYWORD = "SeriesDescription";
  String RESULT_FIELD = "Result";

  CSVFormat CSV_FORMAT = DEFAULT.
        withDelimiter(';').
        withHeader(
              PATIENT_NAME_FIELD,
              PATIENT_ID_FIELD,
              STUDY_INSTANCE_UID_FIELD,
              STUDY_DATE_FIELD,
              STUDY_DESCRIPTION_FIELD,
              SERIES_INSTANCE_UID_FIELD,
              SERIES_DESCRIPTION_FIELD,
              RESULT_FIELD).
        withSkipHeaderRecord();
}