package com.fourquant.riqae.pacs.csv;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import static java.util.Arrays.asList;

public class DataRowFactory {
  private static final Logger log =
        Logger.getLogger(DataRowFactory.class.getName());

  final CSVDocReader csvDocReader = new CSVDocReader();

  public final List<DataRow> create(final String[] patientNames) {
    return create(asList(patientNames));
  }

  public final List<DataRow> create(final List<String> patientNames) {

    final List<DataRow> dataRows = new ArrayList<>();
    for (String patientName : patientNames) {
      final DataRow dataRow = new DataRow();
      //todo: implement other setters
      dataRow.setPatientName(patientName);
      dataRows.add(dataRow);
    }

    return dataRows;
  }

  public final List<DataRow> create(final String filePath) {
    return csvDocReader.create(filePath);
  }
}


