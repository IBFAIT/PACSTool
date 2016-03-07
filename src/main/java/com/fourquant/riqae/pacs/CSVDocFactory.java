package com.fourquant.riqae.pacs;

import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import static com.fourquant.riqae.pacs.Protocol.format;
import static com.fourquant.riqae.pacs.Protocol.patientNameField;
import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;

public class CSVDocFactory {
  private static final Logger log =
        Logger.getLogger(CSVDocFactory.class.getName());

  public final List<DataRow> create(final String[] patientNames) {
    return create(asList(patientNames));
  }

  public final List<DataRow> create(final List<String> patientNames) {

    final List<DataRow> pacsRequest = new ArrayList<>();
    for (String patientName : patientNames) {
      final DataRow dataRow = new DataRow();
      //todo
      dataRow.setPatientName(patientName);
      pacsRequest.add(dataRow);
    }

    return pacsRequest;
  }

  public final List<DataRow> create(final String filePath) {
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

}