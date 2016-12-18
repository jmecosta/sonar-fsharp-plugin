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

import java.util.ArrayList;
import java.util.Arrays;
import org.sonar.api.config.PropertyDefinition;
import org.sonar.api.config.Settings;
import org.sonar.api.resources.Qualifiers;
import org.sonar.plugins.dotnet.tests.CoverageAggregator;
import org.sonar.plugins.dotnet.tests.CoverageConfiguration;
import org.sonar.plugins.dotnet.tests.CoverageReportImportSensor;

import java.util.List;
import org.sonar.api.batch.fs.FileSystem;

// addapted from https://github.com/SonarSource/sonar-csharp/blob/master/src/main/java/org/sonar/plugins/csharp/CSharpCodeCoverageProvider.java
public class FSharpCodeCoverageProvider {

  private static final String CATEGORY = "F#";
  private static final String SUBCATEGORY = "Code Coverage";

  public static final String COVERAGE_PROPERTY_KEY = "sonar.fs.coverage.reportsPaths";
  public static final String UNIT_PROPERTY_KEY = "sonar.fs.test.reportsPaths";
  
  private static final String NCOVER3_PROPERTY_KEY = "sonar.fs.ncover3.reportsPaths";
  private static final String OPENCOVER_PROPERTY_KEY = "sonar.fs.opencover.reportsPaths";
  private static final String DOTCOVER_PROPERTY_KEY = "sonar.fs.dotcover.reportsPaths";
  private static final String VISUAL_STUDIO_COVERAGE_XML_PROPERTY_KEY = "sonar.fs.vscoveragexml.reportsPaths";

  private static final String IT_NCOVER3_PROPERTY_KEY = "sonar.fs.ncover3.it.reportsPaths";
  private static final String IT_OPENCOVER_PROPERTY_KEY = "sonar.fs.opencover.it.reportsPaths";
  private static final String IT_DOTCOVER_PROPERTY_KEY = "sonar.fs.dotcover.it.reportsPaths";
  private static final String IT_VISUAL_STUDIO_COVERAGE_XML_PROPERTY_KEY = "sonar.fs.vscoveragexml.it.reportsPaths";
  
  private static final CoverageConfiguration COVERAGE_CONF = new CoverageConfiguration(
    FSharpPlugin.LANGUAGE_KEY,
    NCOVER3_PROPERTY_KEY,
    OPENCOVER_PROPERTY_KEY,
    DOTCOVER_PROPERTY_KEY,
    VISUAL_STUDIO_COVERAGE_XML_PROPERTY_KEY);
  
  private static final CoverageConfiguration IT_COVERAGE_CONF = new CoverageConfiguration(
    FSharpPlugin.LANGUAGE_KEY,
    IT_NCOVER3_PROPERTY_KEY,
    IT_OPENCOVER_PROPERTY_KEY,
    IT_DOTCOVER_PROPERTY_KEY,
    IT_VISUAL_STUDIO_COVERAGE_XML_PROPERTY_KEY);  

  private FSharpCodeCoverageProvider() {
  }

  public static List extensions() {
    return new ArrayList<>(Arrays.asList(
      FSharpCoverageAggregator.class, FSharpIntegrationCoverageAggregator.class,
      FSharpCoverageReportImportSensor.class, FSharpIntegrationCoverageReportImportSensor.class,
      PropertyDefinition.builder(NCOVER3_PROPERTY_KEY)
        .name("NCover3 Reports Paths")
        .description("Example: \"report.nccov\", \"report1.nccov,report2.nccov\" or \"C:/report.nccov\"")
        .category(CATEGORY)
        .subCategory(SUBCATEGORY)
        .onlyOnQualifiers(Qualifiers.PROJECT, Qualifiers.MODULE)
        .build(),
      PropertyDefinition.builder(IT_NCOVER3_PROPERTY_KEY)
        .name("NCover3 Integration Tests Reports Paths")
        .description("Example: \"report.nccov\", \"report1.nccov,report2.nccov\" or \"C:/report.nccov\"")
        .category(CATEGORY)
        .subCategory(SUBCATEGORY)
        .onlyOnQualifiers(Qualifiers.PROJECT, Qualifiers.MODULE)
        .build(),
      PropertyDefinition.builder(OPENCOVER_PROPERTY_KEY)
        .name("OpenCover Reports Paths")
        .description("Example: \"report.xml\", \"report1.xml,report2.xml\" or \"C:/report.xml\"")
        .category(CATEGORY)
        .subCategory(SUBCATEGORY)
        .onlyOnQualifiers(Qualifiers.PROJECT, Qualifiers.MODULE)
        .build(),
      PropertyDefinition.builder(IT_OPENCOVER_PROPERTY_KEY)
        .name("OpenCover Integration Tests Reports Paths")
        .description("Example: \"report.xml\", \"report1.xml,report2.xml\" or \"C:/report.xml\"")
        .category(CATEGORY)
        .subCategory(SUBCATEGORY)
        .onlyOnQualifiers(Qualifiers.PROJECT, Qualifiers.MODULE)
        .build(),
      PropertyDefinition.builder(DOTCOVER_PROPERTY_KEY)
        .name("dotCover (HTML) Reports Paths")
        .description("Example: \"report.html\", \"report1.html,report2.html\" or \"C:/report.html\"")
        .category(CATEGORY)
        .subCategory(SUBCATEGORY)
        .onlyOnQualifiers(Qualifiers.PROJECT, Qualifiers.MODULE)
        .build(),
      PropertyDefinition.builder(IT_DOTCOVER_PROPERTY_KEY)
        .name("dotCover Integration Tests (HTML) Reports Paths")
        .description("Example: \"report.html\", \"report1.html,report2.html\" or \"C:/report.html\"")
        .category(CATEGORY)
        .subCategory(SUBCATEGORY)
        .onlyOnQualifiers(Qualifiers.PROJECT, Qualifiers.MODULE)
        .build(),      
      PropertyDefinition.builder(VISUAL_STUDIO_COVERAGE_XML_PROPERTY_KEY)
        .name("Visual Studio (XML) Reports Paths")
        .description("Example: \"report.coveragexml\", \"report1.coveragexml,report2.coveragexml\" or \"C:/report.coveragexml\"")
        .category(CATEGORY)
        .subCategory(SUBCATEGORY)
        .onlyOnQualifiers(Qualifiers.PROJECT, Qualifiers.MODULE)
        .build(),
      PropertyDefinition.builder(IT_VISUAL_STUDIO_COVERAGE_XML_PROPERTY_KEY)
        .name("Visual Studio Integration Tests (XML) Reports Paths")
        .description("Example: \"report.coveragexml\", \"report1.coveragexml,report2.coveragexml\" or \"C:/report.coveragexml\"")
        .category(CATEGORY)
        .subCategory(SUBCATEGORY)
        .onlyOnQualifiers(Qualifiers.PROJECT, Qualifiers.MODULE)
        .build()));
  }

  public static class FSharpCoverageAggregator extends CoverageAggregator {

    public FSharpCoverageAggregator(Settings settings) {
      super(COVERAGE_CONF, settings);
    }

  }

  public static class FSharpCoverageReportImportSensor extends CoverageReportImportSensor {
    
    public FSharpCoverageReportImportSensor(FSharpCoverageAggregator coverageAggregator, FileSystem fs) {
      super(COVERAGE_CONF, coverageAggregator, false);
    }        
  }

  public static class FSharpIntegrationCoverageAggregator extends CoverageAggregator {

    public FSharpIntegrationCoverageAggregator(Settings settings) {
      super(IT_COVERAGE_CONF, settings);
    }

  }

  public static class FSharpIntegrationCoverageReportImportSensor extends CoverageReportImportSensor {

    public FSharpIntegrationCoverageReportImportSensor(FSharpIntegrationCoverageAggregator coverageAggregator, FileSystem fs) {
      super(IT_COVERAGE_CONF, coverageAggregator, true);
    }

  }  
}
