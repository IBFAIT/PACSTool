package com.fourquant.riqae.pacs.csv;

public class DataRow {
  private String patientName;
  private String patientId;
  private String studyInstanceUid;
  private String studyDate;
  private String studyDescription;
  private String seriesInstanceUID;
  private String seriesDescription;
  private String result;

  public String getPatientName() {
    return patientName;
  }

  final void setPatientName(String patientName) {
    this.patientName = patientName;
  }

  public final String getPatientId() {
    return patientId;
  }

  final void setPatientId(final String patientId) {
    this.patientId = patientId;
  }

  public String getStudyInstanceUid() {
    return studyInstanceUid;
  }

  final void setStudyInstanceUid(final String studyInstanceUid) {
    this.studyInstanceUid = studyInstanceUid;
  }

  public String getSeriesInstanceUID() {
    return seriesInstanceUID;
  }

  final void setSeriesInstanceUID(final String seriesInstanceUID) {
    this.seriesInstanceUID = seriesInstanceUID;
  }

  public String getResult() {
    return result;
  }

  final void setResult(String result) {
    this.result = result;
  }

  public String getStudyDate() {
    return studyDate;
  }

  public void setStudyDate(String studyDate) {
    this.studyDate = studyDate;
  }

  public String getStudyDescription() {
    return studyDescription;
  }

  public void setStudyDescription(String studyDescription) {
    this.studyDescription = studyDescription;
  }

  public String getSeriesDescription() {
    return seriesDescription;
  }

  public void setSeriesDescription(String seriesDescription) {
    this.seriesDescription = seriesDescription;
  }

  @Override
  public String toString() {
    return "DataRow{" +
          "patientName='" + patientName + '\'' +
          ", patientId='" + patientId + '\'' +
          ", studyDate='" + studyDate + '\'' +
          ", studyInstanceUid='" + studyInstanceUid + '\'' +
          ", studyDescription='" + studyDescription + '\'' +
          ", seriesInstanceUID='" + seriesInstanceUID + '\'' +
          ", seriesDescription='" + seriesDescription + '\'' +
          ", result='" + result + '\'' +
          '}';
  }
}