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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import org.junit.Test;
import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.batch.sensor.Sensor;
import org.sonar.api.batch.sensor.internal.DefaultSensorDescriptor;
import org.sonar.api.issue.NoSonarFilter;
import org.sonar.api.measures.FileLinesContextFactory;

public class FSharpSensorTest {

    @Test
    public void describe_languageAndKey_asExpected() {
        // Arrange
        FsSonarRunnerExtractor extractor = new FsSonarRunnerExtractor();
        FileSystem fs = mock(FileSystem.class);
        FileLinesContextFactory fileLinesContextFactory = mock(FileLinesContextFactory.class);
        NoSonarFilter noSonarFilter = new NoSonarFilter();
        Sensor sensor = new FSharpSensor(extractor, fs, fileLinesContextFactory, noSonarFilter);

        DefaultSensorDescriptor descriptor = new DefaultSensorDescriptor();

        // Act
        sensor.describe(descriptor);

        // Assert
        assertEquals(FSharpPlugin.LANGUAGE_NAME, descriptor.name());
        assertEquals(1, descriptor.languages().size());
        assertTrue("LANGUAGE_KEY not found", descriptor.languages().contains(FSharpPlugin.LANGUAGE_KEY));
    }
}
