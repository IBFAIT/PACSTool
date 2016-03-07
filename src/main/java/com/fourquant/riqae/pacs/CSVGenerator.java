package com.fourquant.riqae.pacs;

import java.io.Closeable;
import java.io.Flushable;
import java.io.IOException;
import java.io.StringWriter;


public class CSVGenerator implements Flushable, Closeable {

  final StringWriter writer;

  public CSVGenerator(final StringWriter writer) {
    this.writer = writer;
  }

  @Override
  public void close() throws IOException {

  }

  @Override
  public void flush() throws IOException {

  }
}
