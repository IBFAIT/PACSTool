package com.fourquant.riqae.pacs;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.nio.file.Path;

import static java.nio.file.Files.readAllBytes;

public class Dcm4CheWrapperHelper {

  public static FileFilter dcmFilter() {
    return pathname -> pathname.getName().endsWith(".dcm");
  }

  public static FileFilter all() {
    return pathname -> true;
  }

  public static String[] readFiles(final Path directory,
                                   final FileFilter fileFilter)
        throws IOException {

    final File directoryFile = directory.toFile();

    final File[] files = (
          directoryFile.listFiles(
                fileFilter));

    final String[] xmlContents = new String[files.length];

    if (files.length > 0) {

      int i = 0;

      for (final File dcmFile : files) {

        xmlContents[i++] =
              new String(
                    readAllBytes(
                          dcmFile.toPath()));

      }
    }

    return xmlContents;
  }

  public static String[] readFiles(final Path directory) throws IOException {

    return readFiles(directory, all());

  }

  public static File createTempDirectory() throws IOException {

    final Path tempDirectory =
          java.nio.file.Files.createTempDirectory("integrationTest-temp-");

    return tempDirectory.toFile();

  }

  public static void deleteOnExit(final File file) {

    file.deleteOnExit();
  }

}
