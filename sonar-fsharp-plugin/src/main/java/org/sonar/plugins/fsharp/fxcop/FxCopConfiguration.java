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

import com.google.common.base.Preconditions;
import org.sonar.api.config.Settings;

import java.io.File;

public class FxCopConfiguration {

  private final String languageKey;
  private final String repositoryKey;
  private final String reportPathPropertyKey;

  public FxCopConfiguration(String languageKey, String repositoryKey,
    String reportPathPropertyKey) {
    this.languageKey = languageKey;
    this.repositoryKey = repositoryKey;
    this.reportPathPropertyKey = reportPathPropertyKey;
  }

  public String languageKey() {
    return languageKey;
  }

  public String repositoryKey() {
    return repositoryKey;
  }

  public String reportPathPropertyKey() {
    return reportPathPropertyKey;
  }

  public void checkProperties(Settings settings) {
      checkReportPathProperty(settings);
  }

  private void checkReportPathProperty(Settings settings) {
    File file = new File(settings.getString(reportPathPropertyKey));
    Preconditions.checkArgument(
      file.isFile(),
      "Cannot find the FxCop report \"" + file.getAbsolutePath() + "\" provided by the property \"" + reportPathPropertyKey + "\".");
  }
}
