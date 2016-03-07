package com.fourquant.riqae.pacs;

import com.fourquant.riqae.pacs.CSVDoc.Row;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.dcm4che3.data.Attributes;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.List;
import java.util.logging.Logger;

import static com.fourquant.riqae.pacs.Protocol.format;
import static com.fourquant.riqae.pacs.Protocol.patientNameField;
import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;

public class CSVDocFactory {
  private static final Logger log =
        Logger.getLogger(CSVDocFactory.class.getName());

  public final CSVDoc create(final String[] patientNames) {
    return create(asList(patientNames));
  }

  public final CSVDoc create(final List<String> patientNames) {

    final CSVDoc pacsRequest = new CSVDoc();
    for (String patientName : patientNames) {
      final Row row = new Row();
      row.setPatientName(patientName);
      pacsRequest.add(row);
    }

    return pacsRequest;
  }

  public final CSVDoc create(final String filePath) {
    try {
      final Reader in = new FileReader(filePath);

      final CSVParser csvParser = new CSVParser(in, format);
      final List<CSVRecord> records = csvParser.getRecords();

      return create(
            records.stream().map(
                  record -> record.get(patientNameField)).collect(toList()));

    } catch (IOException e) {
      throw new IllegalStateException(e);
    }
  }

  public final CSVDoc create(final Attributes attributes) {
    final CSVDoc CSVDoc = new CSVDoc();
    final Row row = new Row();
    return null;
  }
}