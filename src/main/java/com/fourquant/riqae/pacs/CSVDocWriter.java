package com.fourquant.riqae.pacs;

import com.fourquant.riqae.pacs.CSVDoc.Row;
import org.apache.commons.csv.CSVPrinter;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import static com.fourquant.riqae.pacs.Protocol.format;

public class CSVDocWriter {
  private static final Logger log =
        Logger.getLogger(CSVDocWriter.class.getName());

  public final void write(
        final CSVDoc CSVDoc,
        final String filePath) {

    try {
      final FileWriter fw = new FileWriter(filePath);
      write(CSVDoc, fw);
    } catch (IOException e) {
      throw new IllegalStateException(e);
    }
  }

  public final void write(final CSVDoc CSVDoc) {
    write(CSVDoc, System.out);
  }

  public final void write(
        final CSVDoc CSVDoc,
        final Appendable appendable) {

    try {

      final CSVPrinter p = new CSVPrinter(appendable, format);

      for (Row row : CSVDoc) {
        final List<Serializable> line = new ArrayList<>();

        line.add(row.getPatientName());
        line.add(row.getPatientId());
        line.add(row.getStudyDate());
        line.add(row.getStudyDescription());
        line.add(row.getStudyInstanceUid());
        line.add(row.getSeriesInstanceUID());
        line.add(row.getResult());

        p.printRecord(line);
      }
      p.flush();
      p.close();
    } catch (IOException e) {
      throw new IllegalStateException(e);
    }
  }
}
