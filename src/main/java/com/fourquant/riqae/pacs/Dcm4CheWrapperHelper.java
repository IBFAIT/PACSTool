package com.fourquant.riqae.pacs;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Logger;

import static java.nio.file.FileVisitResult.CONTINUE;
import static java.nio.file.Files.readAllBytes;
import static java.nio.file.Files.walkFileTree;

public class Dcm4CheWrapperHelper {

  private static final Logger log =
        Logger.getLogger(Dcm4CheWrapperHelper.class.getName());

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

  public static long sizeOfDirectory(final Path path) throws IOException {

    final AtomicLong size = new AtomicLong(0);

    walkFileTree(path, new SimpleFileVisitor<Path>() {

      @Override
      public FileVisitResult visitFile(final Path directory,
                                       final BasicFileAttributes attributes) {

        size.addAndGet(attributes.size());

        return CONTINUE;
      }

      @Override
      public FileVisitResult visitFileFailed(final Path directory,
                                             final IOException exception) {

        log.warning("skipped: " + directory + " (" + exception + ")");

        return CONTINUE;
      }

      @Override
      public FileVisitResult postVisitDirectory(final Path dir,
                                                final IOException exception) {

        if (exception != null)
          log.warning("had trouble traversing: " + dir + " (" + exception + ")");

        return CONTINUE;
      }
    });

    return size.get();
  }
}
