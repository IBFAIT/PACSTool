package com.fourquant.riqae.pacs;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import static java.util.stream.Collectors.toSet;

public class CSVDoc implements Iterable<CSVDoc.Row> {
  private static final Logger log =
        Logger.getLogger(CSVDoc.class.getName());

  private final List<Row> rows = new ArrayList<>();

  public void add(Row row) {
    rows.add(row);
  }

  @Override
  public Iterator<Row> iterator() {
    return rows.iterator();
  }

  public int size() {
    return rows.size();
  }

  public final Set<String> getPatientNames() {
    return rows.stream().map(
          Row::getPatientName).collect(toSet());
  }

  public final List<Row> getRows() {
    return rows;
  }

  public static class Row {
    private String patientName;
    private String patientId;
    private String studyDate;
    private String studyDescription;
    private String studyInstanceUid;
    private String seriesInstanceUID;
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

    @Override
    public String toString() {
      return "Row{" +
            "patientName='" + patientName + '\'' +
            ", patientId='" + patientId + '\'' +
            ", studyDate='" + studyDate + '\'' +
            ", studyDescription='" + studyDescription + '\'' +
            ", studyInstanceUid='" + studyInstanceUid + '\'' +
            ", seriesInstanceUID='" + seriesInstanceUID + '\'' +
            ", result='" + result + '\'' +
            '}';
    }
  }

}