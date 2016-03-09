package com.fourquant.riqae.pacs;

import java.io.IOException;
import java.util.List;

public interface PACSFacade {

  List<DataRow> process(
        final List<DataRow> input, final Operation operation)
        throws IOException;

  List<DataRow> process(
        final List<DataRow> input)
        throws IOException;

  enum Operation {
    resolvePatientIds,
    resolveStudyDates,
    resolveStudyDescriptions,
    resolveStudyInstanceUids,
    resolveSeriesInstanceUids
  }
}
