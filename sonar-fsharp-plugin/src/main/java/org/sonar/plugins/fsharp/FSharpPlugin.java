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

import org.sonar.api.Properties;
import org.sonar.api.Property;

import org.sonar.api.Plugin;

// adapted from https://github.com/SonarSource/sonar-csharp
@Properties({
  @Property(
    key = FSharpPlugin.FILE_SUFFIXES_KEY,
    defaultValue = FSharpPlugin.FILE_SUFFIXES_DEFVALUE,
    name = "File suffixes",
    description = "Comma-separated list of suffixes of files to analyze.",
    project = true, global = true
  )
})
public class FSharpPlugin implements Plugin {

  public static final String LANGUAGE_KEY = "fs";
  public static final String LANGUAGE_NAME = "F#";

  public static final String FILE_SUFFIXES_KEY = "sonar.fs.file.suffixes";
  public static final String FILE_SUFFIXES_DEFVALUE = ".fs,.fsx,.fsi";

  public static final String FSHARP_WAY_PROFILE = "Sonar way";

  public static final String REPOSITORY_KEY = "fsharplint";
  public static final String REPOSITORY_NAME = "SonarQube";

  @Override
  public void define(Context context) {
    context.addExtension(FSharp.class);    
    context.addExtension(FSharpSonarRulesDefinition.class);
    context.addExtension(FSharpSonarWayProfile.class);
    context.addExtension(FsSonarRunnerExtractor.class);
    context.addExtension(FSharpSensor.class);       
  }
}
