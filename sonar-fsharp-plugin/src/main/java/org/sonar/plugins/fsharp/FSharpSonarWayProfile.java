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
    activateRule(profile, "RulesTypographyTrailingLineError", "MAJOR");
    activateRule(profile, "RulesTypographyTabCharacterError", "MAJOR");
    activateRule(profile, "RulesTypographyFileLengthError", "MAJOR");
    activateRule(profile, "RulesTypographyLineLengthError", "MAJOR");
    activateRule(profile, "RulesTypographyTrailingWhitespaceError", "MAJOR");
    activateRule(profile, "RulesNestedStatementsError", "MAJOR");
    activateRule(profile, "RulesHintRefactor", "MAJOR");
    activateRule(profile, "RulesHintSuggestion", "MAJOR");
    activateRule(profile, "RulesXmlDocumentationExceptionError", "MAJOR");
    activateRule(profile, "RulesXmlDocumentationUnionError", "MAJOR");
    activateRule(profile, "RulesXmlDocumentationRecordError", "MAJOR");
    activateRule(profile, "RulesXmlDocumentationMemberError", "MAJOR");
    activateRule(profile, "RulesXmlDocumentationTypeError", "MAJOR");
    activateRule(profile, "RulesXmlDocumentationAutoPropertyError", "MAJOR");
    activateRule(profile, "RulesXmlDocumentationEnumError", "MAJOR");
    activateRule(profile, "RulesXmlDocumentationModuleError", "MAJOR");
    activateRule(profile, "RulesXmlDocumentationLetError", "MAJOR");
    activateRule(profile, "RulesNamingConventionsExceptionError", "MAJOR");
    activateRule(profile, "RulesNamingConventionsCamelCaseError", "MAJOR");
    activateRule(profile, "RulesNamingConventionsPascalCaseError", "MAJOR");
    activateRule(profile, "RulesNamingConventionsInterfaceError", "MAJOR");
    activateRule(profile, "RulesRaiseWithSingleArgument", "MAJOR");
    activateRule(profile, "RulesFailwithWithSingleArgument", "MAJOR");
    activateRule(profile, "RulesNullArgWithSingleArgument", "MAJOR");
    activateRule(profile, "RulesInvalidOpWithSingleArgument", "MAJOR");
    activateRule(profile, "RulesInvalidArgWithTwoArguments", "MAJOR");
    activateRule(profile, "RulesFailwithfWithArgumentsMatchingFormatString", "MAJOR");
    activateRule(profile, "RulesTupleOfWildcardsError", "MAJOR");
    activateRule(profile, "RulesWildcardNamedWithAsPattern", "MAJOR");
    activateRule(profile, "RulesUselessBindingError", "MAJOR");
    activateRule(profile, "RulesFavourIgnoreOverLetWildError", "MAJOR");
    activateRule(profile, "RulesCanBeReplacedWithComposition", "MAJOR");
    activateRule(profile, "RulesReimplementsFunction", "MAJOR");
    activateRule(profile, "RulesSourceLengthError", "MAJOR");
    activateRule(profile, "RulesNumberOfItemsTupleError", "MAJOR");
    activateRule(profile, "RulesNumberOfItemsClassMembersError", "MAJOR");
    activateRule(profile, "RulesNumberOfItemsFunctionError", "MAJOR");
    activateRule(profile, "RulesNumberOfItemsBooleanConditionsError", "MAJOR");
    profile.done();
  }

  private void activateRule(NewBuiltInQualityProfile profile, String ruleKey, String severity) {
    NewBuiltInActiveRule rule1 = profile.activateRule(FSharpPlugin.REPOSITORY_KEY, ruleKey);
    rule1.overrideSeverity(severity);
  }
}
