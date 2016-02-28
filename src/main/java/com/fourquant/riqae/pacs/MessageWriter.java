package com.fourquant.riqae.pacs;

import com.fourquant.riqae.pacs.Message.Row;
import org.apache.commons.csv.CSVPrinter;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import static com.fourquant.riqae.pacs.Protocol.format;

public class MessageWriter {
  private static final Logger log =
        Logger.getLogger(MessageWriter.class.getName());

  public final void write(
        final Message message,
        final String filePath) {

    try {
      final FileWriter fw = new FileWriter(filePath);
      write(message, fw);
    } catch (IOException e) {
      throw new IllegalStateException(e);
    }
  }

  public final void write(final Message message) {
    write(message, System.out);
  }

  public final void write(
        final Message message,
        final Appendable appendable) {

    try {

      final CSVPrinter p = new CSVPrinter(appendable, format);

      for (Row row : message) {
        List<Serializable> line = new ArrayList<>();

        line.add(row.getPatientName());
        line.add(row.getPatientId());
        line.add(row.getExamId());
        line.add(row.getSeriesId());
        line.add(row.getTechnique());
        line.add(row.getType());

        p.printRecord(line);
      }
      p.flush();
      p.close();
    } catch (IOException e) {
      throw new IllegalStateException(e);
    }
  }
}
