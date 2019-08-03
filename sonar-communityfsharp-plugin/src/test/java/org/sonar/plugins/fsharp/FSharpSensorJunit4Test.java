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

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import org.junit.Rule;
import org.junit.Test;
import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.batch.sensor.Sensor;
import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.api.issue.NoSonarFilter;
import org.sonar.api.measures.FileLinesContextFactory;
import org.sonar.api.utils.log.LogTester;
import org.sonar.api.utils.log.LoggerLevel;

@Deprecated
// Rule Annotation not supported by JUnit5
// http://javadocs.sonarsource.org/6.0/apidocs/org/sonar/api/utils/log/LogTester.html
// org.sonar.api.utils.log.LogTester depends on Rule-API and used here
// Could not be resolved with org.junit.vintage:junit-vintage-engine
public class FSharpSensorJunit4Test {
    @Rule
    public LogTester logTester = new LogTester();

    @Test
    public void execute_noContect_exceptionCatched() {
        // Arrange
        FsSonarRunnerExtractor extractor = mock(FsSonarRunnerExtractor.class);
        FileSystem fs = mock(FileSystem.class);
        FileLinesContextFactory fileLinesContextFactory = mock(FileLinesContextFactory.class);
        NoSonarFilter noSonarFilter = new NoSonarFilter();
        Sensor sensor = new FSharpSensor(extractor, fs, fileLinesContextFactory, noSonarFilter);

        SensorContext context = mock(SensorContext.class); // SensorContextTester.create();

        logTester.setLevel(LoggerLevel.ERROR);
        logTester.clear();

        // Act
        sensor.execute(context);

        // Assert
        assertThat(logTester.logs()).hasSize(1);
        assertThat(logTester.logs()).contains("SonarQube Community F# plugin analyzis failed");
    }
}
