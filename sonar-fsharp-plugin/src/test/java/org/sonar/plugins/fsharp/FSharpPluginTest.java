/*
 * Sonar F# Plugin :: Core
 * Copyright (C) 2015 Jorge Costa and SonarSource
 * dev@sonar.codehaus.org
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02
 */
package org.sonar.plugins.fsharp;

import org.sonar.plugins.fsharp.FSharpCPDMapping;
import org.sonar.plugins.fsharp.FSharpSonarRulesDefinition;
import org.sonar.plugins.fsharp.FSharp;
import org.sonar.plugins.fsharp.FSharpCodeCoverageProvider;
import org.sonar.plugins.fsharp.FSharpCommonRulesEngine;
import org.sonar.plugins.fsharp.FSharpSensor;
import org.sonar.plugins.fsharp.FSharpUnitTestResultsProvider;
import org.sonar.plugins.fsharp.FSharpCommonRulesDecorator;
import org.sonar.plugins.fsharp.FSharpPlugin;
import org.sonar.plugins.fsharp.FsSonarRunnerExtractor;
import org.sonar.plugins.fsharp.FSharpSonarWayProfile;
import org.sonar.plugins.fsharp.FSharpSourceCodeColorizer;
import com.google.common.collect.ImmutableList;
import org.junit.Test;
import org.sonar.api.config.PropertyDefinition;

import java.util.List;

import static org.fest.assertions.Assertions.assertThat;

public class FSharpPluginTest {

  @Test
  public void getExtensions() {
    List extensions = new FSharpPlugin().getExtensions();

    Class<?>[] expectedExtensions = new Class<?>[] {
      FSharp.class,
      FSharpCommonRulesEngine.class,
      FSharpCommonRulesDecorator.class,
      FSharpSourceCodeColorizer.class,
      FSharpSonarRulesDefinition.class,
      FSharpSonarWayProfile.class,
      FsSonarRunnerExtractor.class,
      FSharpSensor.class,
      FSharpCPDMapping.class
    };

    assertThat(nonProperties(extensions)).contains(expectedExtensions);

    assertThat(extensions).hasSize(expectedExtensions.length
        + FSharpCodeCoverageProvider.extensions().size()
        + FSharpUnitTestResultsProvider.extensions().size()
        + 1);
  }

  private static List nonProperties(List extensions) {
    ImmutableList.Builder builder = ImmutableList.builder();
    for (Object extension : extensions) {
      if (!(extension instanceof PropertyDefinition)) {
        builder.add(extension);
      }
    }
    return builder.build();
  }

}
