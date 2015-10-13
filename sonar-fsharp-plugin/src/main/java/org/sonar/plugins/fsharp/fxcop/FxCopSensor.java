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
package org.sonar.plugins.fsharp.fxcop;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.batch.Sensor;
import org.sonar.api.batch.SensorContext;
import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.fs.InputFile.Type;
import org.sonar.api.component.ResourcePerspectives;
import org.sonar.api.config.Settings;
import org.sonar.api.issue.Issuable;
import org.sonar.api.profiles.RulesProfile;
import org.sonar.api.resources.Project;
import org.sonar.api.rule.RuleKey;
import org.sonar.api.rules.ActiveRule;

import javax.annotation.Nullable;

import java.io.File;
import java.util.List;

public class FxCopSensor implements Sensor {

  private static final String CUSTOM_RULE_KEY = "CustomRuleTemplate";
  private static final String CUSTOM_RULE_CHECK_ID_PARAMETER = "CheckId";
  private static final Logger LOG = LoggerFactory.getLogger(FxCopSensor.class);

  private final FxCopConfiguration fxCopConf;
  private final Settings settings;
  private final RulesProfile profile;
  private final FileSystem fs;
  private final ResourcePerspectives perspectives;

  public FxCopSensor(FxCopConfiguration fxCopConf, Settings settings, RulesProfile profile, FileSystem fs, ResourcePerspectives perspectives) {
    this.fxCopConf = fxCopConf;
    this.settings = settings;
    this.profile = profile;
    this.fs = fs;
    this.perspectives = perspectives;
  }

  @Override
  public boolean shouldExecuteOnProject(Project project) {
    boolean shouldExecute;

    if (!hasFilesToAnalyze()) {
      shouldExecute = false;
    } else if (profile.getActiveRulesByRepository(fxCopConf.repositoryKey()).isEmpty()) {
      LOG.info("All FxCop rules are disabled, skipping its execution.");
      shouldExecute = false;
    } else {
      shouldExecute = true;
    }

    return shouldExecute;
  }

  private boolean hasFilesToAnalyze() {
    return fs.files(fs.predicates().and(fs.predicates().hasLanguage(fxCopConf.languageKey()), fs.predicates().hasType(Type.MAIN))).iterator().hasNext();
  }

  @Override
  public void analyse(Project project, SensorContext context) {
    analyse(context, new FxCopReportParser());
  }

  @VisibleForTesting
  void analyse(SensorContext context, FxCopReportParser parser) {
    fxCopConf.checkProperties(settings);

    File reportFile;
    String reportPath = settings.getString(fxCopConf.reportPathPropertyKey());
    LOG.debug("Using the provided FxCop report" + reportPath);
    reportFile = new File(reportPath);

    for (FxCopIssue issue : parser.parse(reportFile)) {
      if (!hasFileAndLine(issue)) {
        logSkippedIssue(issue, "which has no associated file.");
        continue;
      }

      File file = new File(new File(issue.path()), issue.file());
      InputFile inputFile = fs.inputFile(fs.predicates().and(fs.predicates().hasType(Type.MAIN), fs.predicates().hasAbsolutePath(file.getAbsolutePath())));
      if (inputFile == null) {
        logSkippedIssueOutsideOfSonarQube(issue, file);
      } else {
        Issuable issuable = perspectives.as(Issuable.class, inputFile);
        if (issuable == null) {
          logSkippedIssueOutsideOfSonarQube(issue, file);
        } else {
          issuable.addIssue(
            issuable.newIssueBuilder()
              .ruleKey(RuleKey.of(fxCopConf.repositoryKey(), ruleKey(issue.ruleConfigKey())))
              .line(issue.line())
              .message(issue.message())
              .build());
        }
      }
    }
  }

  private static List<String> splitOnCommas(@Nullable String property) {
    if (property == null) {
      return ImmutableList.of();
    } else {
      return ImmutableList.copyOf(Splitter.on(",").trimResults().omitEmptyStrings().split(property));
    }
  }

  private static boolean hasFileAndLine(FxCopIssue issue) {
    return issue.path() != null && issue.file() != null && issue.line() != null;
  }

  private static void logSkippedIssueOutsideOfSonarQube(FxCopIssue issue, File file) {
    logSkippedIssue(issue, "whose file \"" + file.getAbsolutePath() + "\" is not in SonarQube.");
  }

  private static void logSkippedIssue(FxCopIssue issue, String reason) {
    LOG.debug("Skipping the FxCop issue at line " + issue.reportLine() + " " + reason);
  }

  private List<String> enabledRuleConfigKeys() {
    ImmutableList.Builder<String> builder = ImmutableList.builder();
    for (ActiveRule activeRule : profile.getActiveRulesByRepository(fxCopConf.repositoryKey())) {
      if (!CUSTOM_RULE_KEY.equals(activeRule.getRuleKey())) {
        String effectiveConfigKey = activeRule.getConfigKey();
        if (effectiveConfigKey == null) {
          effectiveConfigKey = activeRule.getParameter(CUSTOM_RULE_CHECK_ID_PARAMETER);
        }

        builder.add(effectiveConfigKey);
      }
    }
    return builder.build();
  }

  private String ruleKey(String ruleConfigKey) {
    for (ActiveRule activeRule : profile.getActiveRulesByRepository(fxCopConf.repositoryKey())) {
      if (ruleConfigKey.equals(activeRule.getConfigKey()) || ruleConfigKey.equals(activeRule.getParameter(CUSTOM_RULE_CHECK_ID_PARAMETER))) {
        return activeRule.getRuleKey();
      }
    }

    throw new IllegalStateException(
      "Unable to find the rule key corresponding to the rule config key \"" + ruleConfigKey + "\" in repository \"" + fxCopConf.repositoryKey() + "\".");
  }

}
