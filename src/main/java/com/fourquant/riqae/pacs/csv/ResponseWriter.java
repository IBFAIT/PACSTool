package com.fourquant.riqae.pacs.csv;

import java.io.IOException;
import java.util.List;

//todo: kann evtl. ersetzt werden
public class ResponseWriter {
  public static void write(final List<DataRow> dataRows,
                           final String outputFileName) {

    final CSVDocWriter writer = new CSVDocWriter();
    writer.write(dataRows, outputFileName);
  }

  public static void write(final List<DataRow> dataRows) throws IOException {

    final CSVDocWriter writer = new CSVDocWriter();
    writer.write(dataRows);
  }
}
