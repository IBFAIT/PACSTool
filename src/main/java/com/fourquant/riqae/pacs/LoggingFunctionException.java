package com.fourquant.riqae.pacs;

public class LoggingFunctionException extends Exception {

  public LoggingFunctionException(
        final ReflectiveOperationException exception) {

    super(exception);
  }
}
