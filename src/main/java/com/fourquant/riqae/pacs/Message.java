package com.fourquant.riqae.pacs;


import java.util.*;
import java.util.logging.Logger;

public class Message implements Iterable<Message.Row> {
  private static final Logger log =
        Logger.getLogger(Message.class.getName());

  private final List<Row> list = new ArrayList<>();

  public void add(Row row) {
    list.add(row);
  }

  @Override
  public Iterator<Row> iterator() {
    return list.iterator();
  }

  public Set<String> getPatientNames() {
    final Set<String> patientNames = new HashSet<>();
    for (Row row : list) {
      patientNames.add(row.getPatientName());
    }
    return patientNames;
  }

  public static class Row {
    private String patientName;
    private int patientId;
    private int examId;
    private int seriesId;
    private String technique;
    private String type;

    public String getPatientName() {
      return patientName;
    }

    final void setPatientName(String patientName) {
      this.patientName = patientName;
    }

    public final int getPatientId() {
      return patientId;
    }

    final void setPatientId(int patientId) {
      this.patientId = patientId;
    }

    public int getExamId() {
      return examId;
    }

    final void setExamId(int examId) {
      this.examId = examId;
    }

    public int getSeriesId() {
      return seriesId;
    }

    final void setSeriesId(int seriesId) {
      this.seriesId = seriesId;
    }

    public String getTechnique() {
      return technique;
    }

    final void setTechnique(String technique) {
      this.technique = technique;
    }

    public String getType() {
      return type;
    }

    final void setType(String type) {
      this.type = type;
    }
  }
}