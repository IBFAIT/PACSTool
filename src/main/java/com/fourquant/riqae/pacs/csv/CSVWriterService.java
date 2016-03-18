package com.fourquant.riqae.pacs.csv;

import org.apache.commons.csv.CSVPrinter;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import static com.fourquant.riqae.pacs.csv.CSVProtocol.CSV_FORMAT;


public class CSVWriterService {

  private static final Logger log =
        Logger.getLogger(CSVWriterService.class.getName());

  public final void writeDataRows(final Collection<CSVDataRow> CSVDataRows,
                                  final String filePath) throws IOException {

    final FileWriter fw = new FileWriter(filePath);

    writeDataRows(CSVDataRows, fw);
  }

  public final void writeDataRows(final Collection<CSVDataRow> CSVDataRows)
        throws IOException {

    writeDataRows(CSVDataRows, System.out);
  }

  public final void writeDataRows(
        final Collection<CSVDataRow> CSVDataRows,
        final Appendable appendable) throws IOException {

    final CSVPrinter csvPrinter = new CSVPrinter(appendable, CSV_FORMAT);

    for (final CSVDataRow csvDataRow : CSVDataRows) {

      final List<Serializable> line = new ArrayList<>();

      line.add(csvDataRow.getPatientName());
      line.add(csvDataRow.getPatientID());
      line.add(csvDataRow.getStudyInstanceUID());
      line.add(csvDataRow.getStudyDate());
      line.add(csvDataRow.getStudyDescription());
      line.add(csvDataRow.getSeriesInstanceUID());
      line.add(csvDataRow.getSeriesDescription());
      line.add(csvDataRow.getResult());

      csvPrinter.printRecord(line);
    }

    csvPrinter.flush();
    csvPrinter.close();
  }

  public final void writeCSVFile(final Set<CSVDataRow> csvDataRows,
                                 final String csvFile) throws IOException {

    final CSVWriterService csvWriterService = new CSVWriterService();
    csvWriterService.writeDataRows(csvDataRows, csvFile);
  }
}
