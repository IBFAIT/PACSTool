package com.fourquant.riqae.pacs.executors;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

import static java.io.File.createTempFile;

public class FindScuExecutor implements ThirdPartyToolExecutor {

  private static final String TEMPFILE_PREFIX = "FINDSCU_";
  private static final String TEMPFILE_POSTFIX = "_TMP";
  private static final String OUT_DIR_OPTION = "--out-dir";
  private static final String OUT_XML = "--xml";
  private static final String SPACE = " ";

  protected static FileFilter getOutputFileFilter() {
    return pathname -> pathname.getName().endsWith(".dcm");
  }

  @Override
  public String[] execute(final String command) throws IOException, InterruptedException {
    final File outFile = createTempFile(TEMPFILE_PREFIX, TEMPFILE_POSTFIX);

    final Path tempDirectory = Files.createTempDirectory("findscu-temp-");

    final StringBuilder stringBuilder = new StringBuilder(command);
    final String outExtraOptions = SPACE + OUT_DIR_OPTION + SPACE + tempDirectory.toAbsolutePath() + SPACE + OUT_XML + SPACE;

    // Insert Extra Finddsc options in command
    // (dcm4che treats first found command line option as override of matching options)
    int posOutDir = command.indexOf(OUT_DIR_OPTION);
    if (posOutDir < 0) {
      stringBuilder.append(outExtraOptions);
    } else { // first option in command line is the one obeyed
      stringBuilder.insert(posOutDir, outExtraOptions);
    }
    final String actualCommand = stringBuilder.toString();

    final Process p = Runtime.getRuntime().exec(actualCommand);
    p.waitFor();

    final File outPutDirectory = new File(tempDirectory.toFile().getPath());

    final File[] outFileList = (
          outPutDirectory.listFiles(
                getOutputFileFilter()));

    final String[] results = new String[outFileList.length];

    if (outFileList.length > 0) {
      int i = 0;
      for (final File file : outFileList) {
        final BufferedReader buf = new BufferedReader(new FileReader(file));
        final StringBuilder result = new StringBuilder();
        String line = buf.readLine();

        while (line != null) {
          result.append(line);
          // result.append("\n");
          line = buf.readLine();
        }

        results[i++] = result.toString();
      }
    }

    return results;
  }
}