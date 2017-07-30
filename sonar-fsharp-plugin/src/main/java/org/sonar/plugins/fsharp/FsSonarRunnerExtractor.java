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
import org.sonar.api.batch.bootstrap.ProjectReactor;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import org.sonar.api.batch.BatchSide;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;
import org.sonar.plugins.fsharp.utils.UnZip;

// addapted from https://github.com/SonarSource/sonar-csharp
@InstantiationStrategy(InstantiationStrategy.PER_BATCH)
@BatchSide()
public class FsSonarRunnerExtractor {
  public static final Logger LOG = Loggers.get(FsSonarRunnerExtractor.class);
  private static final String N_SONARQUBE_ANALYZER = "FsSonarRunner";
  private static final String N_SONARQUBE_ANALYZER_ZIP = N_SONARQUBE_ANALYZER + ".zip";
  private static final String N_SONARQUBE_ANALYZER_EXE = N_SONARQUBE_ANALYZER + ".exe";

  private final ProjectReactor reactor;
  private File file = null;

  public FsSonarRunnerExtractor(ProjectReactor reactor) {
    this.reactor = reactor;
  }

  public File executableFile() throws IOException {
    if (file == null) {
      file = unzipProjectCheckerFile();
    }

    return file;
  }

  private File unzipProjectCheckerFile() throws IOException {
    File workingDir = reactor.getRoot().getWorkDir();
    File toolWorkingDir = new File(workingDir, N_SONARQUBE_ANALYZER);
    File zipFile = new File(workingDir, N_SONARQUBE_ANALYZER_ZIP);
    
    if (zipFile.exists()) {
      return new File(toolWorkingDir, N_SONARQUBE_ANALYZER_EXE);
    }

    try {

      
      try (InputStream is = getClass().getResourceAsStream("/" + N_SONARQUBE_ANALYZER_ZIP)) {
        Files.copy(is, zipFile.toPath());
      }

      UnZip unZip = new UnZip();
      unZip.unZipIt(zipFile.getAbsolutePath(),toolWorkingDir.getAbsolutePath());
        
      return new File(toolWorkingDir, N_SONARQUBE_ANALYZER_EXE);
    } catch (IOException e) {
      LOG.error("Unable to unzip File: {} => {}", N_SONARQUBE_ANALYZER_EXE, e.getMessage());
      throw e;
    }
  }
}
