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

import org.sonar.api.rule.Severity;
import org.sonar.api.server.profile.BuiltInQualityProfilesDefinition;

public class FSharpSonarWayProfile implements BuiltInQualityProfilesDefinition {

  @Override
  public void define(Context context) {

    NewBuiltInQualityProfile profile = context.createBuiltInQualityProfile(
            FSharpPlugin.FSHARP_WAY_PROFILE, FSharpPlugin.LANGUAGE_KEY);

    profile.setDefault(true);
    activateRule(profile, "RulesTypographyTrailingLineError", Severity.MAJOR);
    activateRule(profile, "RulesTypographyTabCharacterError", Severity.MAJOR);
    activateRule(profile, "RulesTypographyFileLengthError", Severity.MAJOR);
    activateRule(profile, "RulesTypographyLineLengthError", Severity.MAJOR);
    activateRule(profile, "RulesTypographyTrailingWhitespaceError", Severity.MAJOR);
    activateRule(profile, "RulesNestedStatementsError", Severity.MAJOR);
    activateRule(profile, "RulesHintRefactor", Severity.MAJOR);
    activateRule(profile, "RulesHintSuggestion", Severity.MAJOR);
    activateRule(profile, "RulesXmlDocumentationExceptionError", Severity.MAJOR);
    activateRule(profile, "RulesXmlDocumentationUnionError", Severity.MAJOR);
    activateRule(profile, "RulesXmlDocumentationRecordError", Severity.MAJOR);
    activateRule(profile, "RulesXmlDocumentationMemberError", Severity.MAJOR);
    activateRule(profile, "RulesXmlDocumentationTypeError", Severity.MAJOR);
    activateRule(profile, "RulesXmlDocumentationAutoPropertyError", Severity.MAJOR);
    activateRule(profile, "RulesXmlDocumentationEnumError", Severity.MAJOR);
    activateRule(profile, "RulesXmlDocumentationModuleError", Severity.MAJOR);
    activateRule(profile, "RulesXmlDocumentationLetError", Severity.MAJOR);
    activateRule(profile, "RulesNamingConventionsExceptionError", Severity.MAJOR);
    activateRule(profile, "RulesNamingConventionsCamelCaseError", Severity.MAJOR);
    activateRule(profile, "RulesNamingConventionsPascalCaseError", Severity.MAJOR);
    activateRule(profile, "RulesNamingConventionsInterfaceError", Severity.MAJOR);
    activateRule(profile, "RulesRaiseWithSingleArgument", Severity.MAJOR);
    activateRule(profile, "RulesFailwithWithSingleArgument", Severity.MAJOR);
    activateRule(profile, "RulesNullArgWithSingleArgument", Severity.MAJOR);
    activateRule(profile, "RulesInvalidOpWithSingleArgument", Severity.MAJOR);
    activateRule(profile, "RulesInvalidArgWithTwoArguments", Severity.MAJOR);
    activateRule(profile, "RulesFailwithfWithArgumentsMatchingFormatString", Severity.MAJOR);
    activateRule(profile, "RulesTupleOfWildcardsError", Severity.MAJOR);
    activateRule(profile, "RulesWildcardNamedWithAsPattern", Severity.MAJOR);
    activateRule(profile, "RulesUselessBindingError", Severity.MAJOR);
    activateRule(profile, "RulesFavourIgnoreOverLetWildError", Severity.MAJOR);
    activateRule(profile, "RulesCanBeReplacedWithComposition", Severity.MAJOR);
    activateRule(profile, "RulesReimplementsFunction", Severity.MAJOR);
    activateRule(profile, "RulesSourceLengthError", Severity.MAJOR);
    activateRule(profile, "RulesNumberOfItemsTupleError", Severity.MAJOR);
    activateRule(profile, "RulesNumberOfItemsClassMembersError", Severity.MAJOR);
    activateRule(profile, "RulesNumberOfItemsFunctionError", Severity.MAJOR);
    activateRule(profile, "RulesNumberOfItemsBooleanConditionsError", Severity.MAJOR);
    profile.done();
  }

  private void activateRule(NewBuiltInQualityProfile profile, String ruleKey, String severity) {
    NewBuiltInActiveRule rule1 = profile.activateRule(FSharpPlugin.REPOSITORY_KEY, ruleKey);
    rule1.overrideSeverity(severity);
  }
}
