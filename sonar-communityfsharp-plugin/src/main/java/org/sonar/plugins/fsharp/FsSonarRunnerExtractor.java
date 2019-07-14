/*
 * Sonar FSharp Plugin, open source software quality management tool.
 *
 * Sonar FSharp Plugin is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * Sonar FSharp Plugin is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 */
package org.sonar.plugins.fsharp;

import org.sonar.api.batch.InstantiationStrategy;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import org.sonar.api.batch.ScannerSide;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;
import org.sonar.plugins.fsharp.utils.UnZip;

// addapted from https://github.com/SonarSource/sonar-csharp
@InstantiationStrategy(InstantiationStrategy.PER_BATCH)
@ScannerSide()
public class FsSonarRunnerExtractor {
  public static final Logger LOG = Loggers.get(FsSonarRunnerExtractor.class);
  private static final String N_SONARQUBE_ANALYZER = "FsSonarRunner";
  private static final String N_SONARQUBE_ANALYZER_ZIP = N_SONARQUBE_ANALYZER + ".zip";
  private static final String N_SONARQUBE_ANALYZER_EXE = N_SONARQUBE_ANALYZER + ".exe";

  private File file = null;

  public File executableFile(String workDir) throws IOException {
    if (file == null) {
      file = unzipProjectCheckerFile(N_SONARQUBE_ANALYZER_EXE, workDir);
    }

    return file;
  }

  private File unzipProjectCheckerFile(String fileName, String workDir) throws IOException {
    File toolWorkingDir = new File(workDir, "ProjectTools");
    File zipFile = new File(workDir, N_SONARQUBE_ANALYZER_ZIP);

    if (zipFile.exists()) {
      return new File(toolWorkingDir, fileName);
    }

    try {
      try (InputStream is = getClass().getResourceAsStream("/" + N_SONARQUBE_ANALYZER_ZIP)) {
        Files.copy(is, zipFile.toPath());
      }

      UnZip unZip = new UnZip();
      unZip.unZipIt(zipFile.getAbsolutePath(), toolWorkingDir.getAbsolutePath());
      return new File(toolWorkingDir, fileName);
    } catch (IOException e) {
      LOG.error("Unable to unzip File: {} => {}", fileName, e.getMessage());
      throw e;
    }
  }
}
