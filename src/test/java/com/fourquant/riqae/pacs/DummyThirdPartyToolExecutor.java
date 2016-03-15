package com.fourquant.riqae.pacs;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static java.nio.file.FileSystems.getDefault;

public class DummyThirdPartyToolExecutor implements ThirdPartyToolExecutor {

  private final String[] dataFiles;

  public DummyThirdPartyToolExecutor(final String[] dataFiles) {
    this.dataFiles = dataFiles;
  }

  @Override
  public String[] execute(final String command) throws IOException {
    final String[] xml = new String[dataFiles.length];

    for (int i = 0; i < dataFiles.length; i++) {
      final String dataFile = dataFiles[i];
      xml[i] = readContent(getPath("/" + dataFile));
    }

    return xml;
  }

  private Path getPath(final String fileName) {

    final URL resource = getClass().getResource(fileName);
    final String path = resource.getPath();

    return getDefault().getPath(path);
  }

  private String readContent(final Path path) throws IOException {
    String content = "";
    final List<String> lines = Files.readAllLines(path);
    for (final String line : lines) {
      content += line;
      content += System.getProperty("line.separator");
    }
    return content;
  }
}