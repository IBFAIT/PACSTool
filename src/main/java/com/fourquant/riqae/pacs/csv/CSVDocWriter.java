package com.fourquant.riqae.pacs.csv;

import org.apache.commons.csv.CSVPrinter;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import static com.fourquant.riqae.pacs.csv.Protocol.format;


public class CSVDocWriter {
  private static final Logger log =
        Logger.getLogger(CSVDocWriter.class.getName());

  public final void write(
        final List<DataRow> dataRows,
        final String filePath) {

    try {
      final FileWriter fw = new FileWriter(filePath);
      write(dataRows, fw);
    } catch (IOException e) {
      throw new IllegalStateException(e);
    }
  }

  public final void write(final List<DataRow> dataRows) throws IOException {
    write(dataRows, System.out);
  }

  public final void write(
        final List<DataRow> dataRows,
        final Appendable appendable) throws IOException {

    final CSVPrinter p = new CSVPrinter(appendable, format);

    for (DataRow dataRow : dataRows) {
      final List<Serializable> line = new ArrayList<>();

      line.add(dataRow.getPatientName());
      line.add(dataRow.getPatientId());
      line.add(dataRow.getStudyInstanceUid());
      line.add(dataRow.getStudyDate());
      line.add(dataRow.getStudyDescription());
      line.add(dataRow.getSeriesInstanceUID());
      line.add(dataRow.getSeriesDescription());
      line.add(dataRow.getResult());

      p.printRecord(line);
    }
    p.flush();
    p.close();
  }
}
