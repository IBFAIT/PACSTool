package com.fourquant.riqae.pacs;

import org.apache.commons.cli.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.List;

import static com.fourquant.riqae.pacs.PACSTool.RequestFactory.createRequest;
import static com.fourquant.riqae.pacs.PACSTool.optPatientName;
import static com.fourquant.riqae.pacs.TestConstants.nameKate;
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
    PACSTool.ResponseWriter.write(dataRows);
    assertTrue(
          outContent.toString().
                contains("Kate Moss,USB000123471,20111231,CT Thorax,591048351241.9457933,591048351241.9457933.5,"));
  }

  @Test
  public void testResponseWriter() {
    final CommandLineParser parser = new DefaultParser();
    final Options options = PACSTool.OptionsFactory.createOptions();
    String[] args = new String[]{"-" + optPatientName, nameKate};
    final CommandLine line;
    try {
      line = parser.parse(options, args);

      final List<DataRow> request =
            createRequest(line.getOptionValues(optPatientName));

      final DefaultPACSFacade pacsFacade =
            new DefaultPACSFacade("localhost", 2133, "admin");

      pacsFacade.setThirdPartyToolExecutor(
            new DummyThirdPartyToolExecutor(
                  new String[]{"kate.xml"}));

      final List<DataRow> response = pacsFacade.process(request);

      PACSTool.ResponseWriter.write(response);

      final String out = outContent.toString();
      assertTrue(out.contains(nameKate));
    } catch (ParseException | IOException e) {
      e.printStackTrace();
    }
  }
}