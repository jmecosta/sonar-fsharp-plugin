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

import org.sonar.api.server.profile.BuiltInQualityProfilesDefinition;

public class FSharpSonarWayProfile implements BuiltInQualityProfilesDefinition {

  @Override
  public void define(Context context) {

    NewBuiltInQualityProfile profile = context.createBuiltInQualityProfile(
            FSharpPlugin.FSHARP_WAY_PROFILE, FSharpPlugin.LANGUAGE_KEY);

    profile.setDefault(true);
    FSharpRuleProperties.ALL.forEach((k, p) -> activateRule(profile, k, p.getSeverity()));
    profile.done();
  }

  private void activateRule(NewBuiltInQualityProfile profile, String ruleKey, String severity) {
    NewBuiltInActiveRule rule1 = profile.activateRule(FSharpPlugin.REPOSITORY_KEY, ruleKey);
    rule1.overrideSeverity(severity);
  }
}
