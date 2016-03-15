package com.fourquant.riqae.pacs.csv;

import java.util.List;

public class RequestFactory {
  final static DataRowFactory factory = new DataRowFactory();

  public static List<DataRow> createRequest(final String filePath) {
    return factory.create(filePath);
  }

  public static List<DataRow> createRequest(final String[] patientNames) {
    return factory.create(patientNames);
  }
}
