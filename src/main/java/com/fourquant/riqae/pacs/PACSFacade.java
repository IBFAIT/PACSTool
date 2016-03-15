package com.fourquant.riqae.pacs;

import com.fourquant.riqae.pacs.csv.DataRow;
import com.fourquant.riqae.pacs.executors.ThirdPartyToolExecutor;

import java.io.IOException;
import java.util.List;

public interface PACSFacade {

  List<DataRow> process(final List<DataRow> input, final Operation operation)
        throws IOException, InterruptedException;

  List<DataRow> process(final List<DataRow> input)
        throws IOException, InterruptedException;

  void setThirdPartyToolExecutor(
        final ThirdPartyToolExecutor thirdPartyToolExecutor);

  enum Operation {
    resolvePatientIds,
    resolveStudyDates,
    resolveStudyDescriptions,
    resolveStudyInstanceUids,
    resolveSeriesInstanceUids
  }
}
