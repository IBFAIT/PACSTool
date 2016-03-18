package com.fourquant.riqae.pacs.csv;

public class CSVDataRow {
  private String patientName;
  private String patientID;
  private String studyInstanceUID;
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

  public final String getPatientID() {
    return patientID;
  }

  final void setPatientID(final String patientID) {
    this.patientID = patientID;
  }

  public String getStudyInstanceUID() {
    return studyInstanceUID;
  }

  final void setStudyInstanceUID(final String studyInstanceUID) {
    this.studyInstanceUID = studyInstanceUID;
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
  public boolean equals(Object o) {
    if (this == o) return true;

    if (o == null || getClass() != o.getClass()) return false;

    CSVDataRow that = (CSVDataRow) o;

    if (patientName != null ? !patientName.equals(that.patientName) :
          that.patientName != null)

      return false;

    if (patientID != null ? !patientID.equals(that.patientID) :
          that.patientID != null)

      return false;

    if (studyInstanceUID != null ?
          !studyInstanceUID.equals(that.studyInstanceUID) :
          that.studyInstanceUID != null)

      return false;

    if (studyDate != null ? !studyDate.equals(that.studyDate) :
          that.studyDate != null)

      return false;

    if (studyDescription != null ?
          !studyDescription.equals(that.studyDescription) :
          that.studyDescription != null)

      return false;

    if (seriesInstanceUID != null ?
          !seriesInstanceUID.equals(that.seriesInstanceUID) :
          that.seriesInstanceUID != null)

      return false;

    if (seriesDescription != null ?
          !seriesDescription.equals(that.seriesDescription) :
          that.seriesDescription != null)

      return false;

    return result != null ? result.equals(that.result) : that.result == null;

  }

  @Override
  public int hashCode() {
    int result = patientName != null ? patientName.hashCode() : 0;

    result = 31 * result + (patientID != null ? patientID.hashCode() : 0);

    result = 31 * result + (studyInstanceUID != null ?
          studyInstanceUID.hashCode() : 0);

    result = 31 * result + (studyDate != null ? studyDate.hashCode() : 0);

    result = 31 * result + (studyDescription != null ?
          studyDescription.hashCode() : 0);

    result = 31 * result + (seriesInstanceUID != null ?
          seriesInstanceUID.hashCode() : 0);

    result = 31 * result + (seriesDescription != null ?
          seriesDescription.hashCode() : 0);

    result = 31 * result + (this.result != null ? this.result.hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    return "CSVDataRow{" +
          "patientName='" + patientName + '\'' +
          ", patientID='" + patientID + '\'' +
          ", studyDate='" + studyDate + '\'' +
          ", studyInstanceUID='" + studyInstanceUID + '\'' +
          ", studyDescription='" + studyDescription + '\'' +
          ", seriesInstanceUID='" + seriesInstanceUID + '\'' +
          ", seriesDescription='" + seriesDescription + '\'' +
          ", result='" + result + '\'' +
          '}';
  }
}