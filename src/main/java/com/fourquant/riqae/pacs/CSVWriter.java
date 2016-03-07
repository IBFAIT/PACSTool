package com.fourquant.riqae.pacs;

import org.dcm4che3.data.Attributes;
import org.dcm4che3.data.Fragments;
import org.dcm4che3.data.Sequence;
import org.dcm4che3.io.DicomInputHandler;
import org.dcm4che3.io.DicomInputStream;

import java.io.IOException;

public class CSVWriter implements DicomInputHandler {

  final CSVGenerator csvGenerator;

  public CSVWriter(final CSVGenerator csvGenerator) {
    this.csvGenerator = csvGenerator;
  }

  @Override
  public void readValue(DicomInputStream dicomInputStream, Attributes attributes) throws IOException {
    System.out.println("attributes = " + attributes);
  }

  @Override
  public void readValue(DicomInputStream dicomInputStream, Sequence sequence) throws IOException {
    System.out.println("sequence = " + sequence);

  }

  @Override
  public void readValue(DicomInputStream dicomInputStream, Fragments fragments) throws IOException {
    System.out.println("fragments = " + fragments);
  }

  @Override
  public void startDataset(DicomInputStream dicomInputStream) throws IOException {
    System.out.println("dicomInputStream = " + dicomInputStream);

  }

  @Override
  public void endDataset(DicomInputStream dicomInputStream) throws IOException {
    System.out.println("dicomInputStream = " + dicomInputStream);
  }

  public void write(Attributes dataset) {
    System.out.println("dataset = " + dataset);
  }
}
