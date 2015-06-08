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

import com.google.common.collect.ImmutableList;
import org.sonar.api.Properties;
import org.sonar.api.Property;
import org.sonar.api.PropertyType;
import org.sonar.api.SonarPlugin;

import java.util.List;

@Properties({
  @Property(
    key = FSharpPlugin.FILE_SUFFIXES_KEY,
    defaultValue = FSharpPlugin.FILE_SUFFIXES_DEFVALUE,
    name = "File suffixes",
    description = "Comma-separated list of suffixes of files to analyze.",
    project = true, global = true
  )
})
public class FSharpPlugin extends SonarPlugin {

  public static final String LANGUAGE_KEY = "fs";
  public static final String LANGUAGE_NAME = "F#";

  public static final String FILE_SUFFIXES_KEY = "sonar.fs.file.suffixes";
  public static final String FILE_SUFFIXES_DEFVALUE = ".fs,.fsx,.fsi";

  public static final String FSHARP_WAY_PROFILE = "Sonar way";

  public static final String REPOSITORY_KEY = "fsharplint";
  public static final String REPOSITORY_NAME = "SonarQube";

  @Override
  public List getExtensions() {
    ImmutableList.Builder builder = ImmutableList.builder();

    builder.add(
      FSharp.class,
      FSharpSonarRulesDefinition.class,
      FSharpSonarWayProfile.class,
      FSharpCommonRulesEngine.class,
      FSharpCommonRulesDecorator.class,
      FSharpSourceCodeColorizer.class,
      FsSonarRunnerExtractor.class,
      FSharpSensor.class,
      FSharpCPDMapping.class);

    builder.addAll(FSharpCodeCoverageProvider.extensions());
    builder.addAll(FSharpUnitTestResultsProvider.extensions());
    builder.addAll(FSharpFxCopProvider.extensions());

    return builder.build();
  }

}
