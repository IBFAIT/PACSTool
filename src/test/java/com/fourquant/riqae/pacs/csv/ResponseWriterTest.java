package com.fourquant.riqae.pacs.csv;

import com.fourquant.riqae.pacs.DefaultPACSFacade;
import com.fourquant.riqae.pacs.executors.DummyThirdPartyToolExecutor;
import com.fourquant.riqae.pacs.tools.OptionsFactory;
import org.apache.commons.cli.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.List;

import static com.fourquant.riqae.pacs.TestConstants.binaryPath;
import static com.fourquant.riqae.pacs.TestConstants.nameKate;
import static com.fourquant.riqae.pacs.csv.RequestFactory.createRequest;
import static com.fourquant.riqae.pacs.tools.OptionsFactory.optPatientName;
import static org.junit.Assert.assertTrue;

public class ResponseWriterTest {

  private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
  private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();

  @Before
  public void setUpStreams() {
    System.setOut(new PrintStream(outContent));
    System.setErr(new PrintStream(errContent));
  }

  @After
  public void cleanUpStreams() {
    System.setOut(null);
    System.setErr(null);
  }

  @Test
  public void testWrite() throws Exception {
    final String namesFile =
          getClass().
                getResource("/namesIdsStudyInstanceUIDsAndSeriesInstanceUIDs.csv").
                getFile();

    final List<DataRow> dataRows = createRequest(namesFile);
    ResponseWriter.write(dataRows);
    assertTrue(
          outContent.toString().
                contains("Kate Moss;USB000123471;591048351241.9457933;20111231;CT Thorax;591048351241.9457933.5;Series Description;"));
  }

  @Test
  public void testResponseWriter() throws ParseException, IOException,
        InterruptedException {

    final CommandLineParser parser = new DefaultParser();
    final Options options = OptionsFactory.createOptions();

    String[] args = new String[]{"-" + optPatientName, nameKate, "-" +
          OptionsFactory.optBinaryPath, binaryPath};

    final CommandLine line;
    line = parser.parse(options, args);

    final List<DataRow> request =
          createRequest(line.getOptionValues(optPatientName));

    final DefaultPACSFacade pacsFacade =
          new DefaultPACSFacade("localhost", 2133, "admin", binaryPath);

    pacsFacade.setThirdPartyToolExecutor(
          new DummyThirdPartyToolExecutor(
                new String[]{"kate.xml"}));

    final List<DataRow> response = pacsFacade.process(request);

    ResponseWriter.write(response);

    final String out = outContent.toString();
    assertTrue(out.contains(nameKate));
  }
}