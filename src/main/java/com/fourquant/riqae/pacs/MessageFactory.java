package com.fourquant.riqae.pacs;

import com.fourquant.riqae.pacs.Message.Row;
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

public class MessageFactory {
  private static final Logger log =
        Logger.getLogger(MessageFactory.class.getName());

  public final Message create(final List<String> patientNames) {

    final Message pacsRequest = new Message();
    for (String patientName : patientNames) {
      Row row = new Row();
      row.setPatientName(patientName);
      pacsRequest.add(row);
    }

    return pacsRequest;

  }

  public final Message create(final String filePath) {
    try {
      final Reader in = new FileReader(filePath);

      final CSVParser csvParser = new CSVParser(in, format);
      final List<CSVRecord> records = csvParser.getRecords();

      final List<String> patientNames = new ArrayList<>();

      for (CSVRecord record : records) {
        patientNames.add(record.get(patientNameField));
      }
      return create(patientNames);

    } catch (IOException e) {
      throw new IllegalStateException(e);
    }
  }
}