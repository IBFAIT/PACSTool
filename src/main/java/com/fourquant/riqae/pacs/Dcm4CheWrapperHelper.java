package com.fourquant.riqae.pacs;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.nio.file.Path;

import static java.nio.file.Files.readAllBytes;

public class Dcm4CheWrapperHelper {

  static FileFilter dcmFilter() {
    return pathname -> pathname.getName().endsWith(".dcm");
  }

  static FileFilter all() {
    return pathname -> true;
  }

  static String[] readFiles(final Path directory, final FileFilter fileFilter)
        throws IOException {

    final File directoryFile = directory.toFile();

    final File[] dcmFiles = (
          directoryFile.listFiles(
                fileFilter));

    final String[] xmlContents = new String[dcmFiles.length];

    if (dcmFiles.length > 0) {

      int i = 0;

      for (final File dcmFile : dcmFiles) {

        xmlContents[i++] =
              new String(
                    readAllBytes(
                          dcmFile.toPath()));

      }
    }

    return xmlContents;
  }

  static String[] readFiles(final Path directory) throws IOException {

    return readFiles(directory, all());

  }

  static File createTempDirectory() throws IOException {

    final Path tempDirectory =
          java.nio.file.Files.createTempDirectory("integrationTest-temp-");

    return tempDirectory.toFile();

  }

  static void deleteOnExit(final File file) {

    file.deleteOnExit();
  }

}
