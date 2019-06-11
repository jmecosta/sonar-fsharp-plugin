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

import org.sonar.api.profiles.ProfileDefinition;
import org.sonar.api.profiles.RulesProfile;
import org.sonar.api.rules.Rule;
import org.sonar.api.utils.ValidationMessages;

// based on plugins from from https://github.com/SonarSource
public class FSharpSonarWayProfile extends ProfileDefinition {

  @Override
  public RulesProfile createProfile(ValidationMessages validation) {
    RulesProfile profile = RulesProfile.create(FSharpPlugin.FSHARP_WAY_PROFILE, FSharpPlugin.LANGUAGE_KEY);

    activateRule(profile, "RulesTypographyTrailingLineError");
    activateRule(profile, "RulesTypographyTabCharacterError");
    activateRule(profile, "RulesTypographyFileLengthError");
    activateRule(profile, "RulesTypographyLineLengthError");
    activateRule(profile, "RulesTypographyTrailingWhitespaceError");
    activateRule(profile, "RulesNestedStatementsError");
    activateRule(profile, "RulesHintRefactor");
    activateRule(profile, "RulesHintSuggestion");
    activateRule(profile, "RulesXmlDocumentationExceptionError");
    activateRule(profile, "RulesXmlDocumentationUnionError");
    activateRule(profile, "RulesXmlDocumentationRecordError");
    activateRule(profile, "RulesXmlDocumentationMemberError");
    activateRule(profile, "RulesXmlDocumentationTypeError");
    activateRule(profile, "RulesXmlDocumentationAutoPropertyError");
    activateRule(profile, "RulesXmlDocumentationEnumError");
    activateRule(profile, "RulesXmlDocumentationModuleError");
    activateRule(profile, "RulesXmlDocumentationLetError");
    activateRule(profile, "RulesNamingConventionsExceptionError");
    activateRule(profile, "RulesNamingConventionsCamelCaseError");
    activateRule(profile, "RulesNamingConventionsPascalCaseError");
    activateRule(profile, "RulesNamingConventionsInterfaceError");
    activateRule(profile, "RulesRaiseWithSingleArgument");
    activateRule(profile, "RulesFailwithWithSingleArgument");
    activateRule(profile, "RulesNullArgWithSingleArgument");
    activateRule(profile, "RulesInvalidOpWithSingleArgument");
    activateRule(profile, "RulesInvalidArgWithTwoArguments");
    activateRule(profile, "RulesFailwithfWithArgumentsMatchingFormatString");
    activateRule(profile, "RulesTupleOfWildcardsError");
    activateRule(profile, "RulesWildcardNamedWithAsPattern");
    activateRule(profile, "RulesUselessBindingError");
    activateRule(profile, "RulesFavourIgnoreOverLetWildError");
    activateRule(profile, "RulesCanBeReplacedWithComposition");
    activateRule(profile, "RulesReimplementsFunction");

    activateRule(profile, "RulesSourceLengthError");
    activateRule(profile, "RulesNumberOfItemsTupleError");
    activateRule(profile, "RulesNumberOfItemsClassMembersError");
    activateRule(profile, "RulesNumberOfItemsFunctionError");
    activateRule(profile, "RulesNumberOfItemsBooleanConditionsError");

    return profile;
  }

  private static void activateRule(RulesProfile profile, String ruleKey) {
    profile.activateRule(Rule.create(FSharpPlugin.REPOSITORY_KEY, ruleKey), null);
  }

}
