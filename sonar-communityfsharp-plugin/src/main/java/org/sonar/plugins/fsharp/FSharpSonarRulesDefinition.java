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
// based on plugins from from https://github.com/SonarSource
package org.sonar.plugins.fsharp;

import org.sonar.api.rule.Severity;
import org.sonar.api.server.rule.RuleParamType;
import org.sonar.api.server.rule.RulesDefinition;

public class FSharpSonarRulesDefinition implements RulesDefinition {
  private static final String MAXIMUM_ALLOWED_VALUES_PARAMETER = "MaxItems";
  private static final String MAXIMUM_ALLOWED_VALUES_DEXCRIPTION = "Maximum allowed values";

  @Override
  public void define(Context context) {
    NewRepository repository = context.createRepository(FSharpPlugin.REPOSITORY_KEY, FSharpPlugin.LANGUAGE_KEY)
        .setName(FSharpPlugin.REPOSITORY_NAME);

    // FL0001-FL0012 Formatting

    // FL0013-FL0058 Conventions
    createRule(repository, "RulesRedundantNewKeyword"); // FL0014

    NewRule nesting = createRule(repository, "RulesNestedStatementsError"); // FL015
    nesting.createParam("Depth").setDescription("Maximum depth").setType(RuleParamType.INTEGER).setDefaultValue("5");

    createRule(repository, "RulesFailwithWithSingleArgument"); // FL016
    createRule(repository, "RulesRaiseWithSingleArgument"); // FL0017
    createRule(repository, "RulesNullArgWithSingleArgument"); // FL0018
    createRule(repository, "RulesInvalidOpWithSingleArgument"); // FL00019
    createRule(repository, "RulesInvalidArgWithTwoArguments"); // FL0020
    createRule(repository, "RulesFailwithfWithArgumentsMatchingFormatString"); // FL0021

    NewRule fileLines2 = createRule(repository, "RulesSourceLengthError");
    fileLines2.createParam("MaxLinesInLambdaFunction") // FL0022
        .setDescription("The maximum lines in lambda function - 0 means disable").setType(RuleParamType.INTEGER)
        .setDefaultValue("10");
    fileLines2.createParam("MaxLinesInMatchLambdaFunction") // FL0023
        .setDescription("The maximum lines in  Match lambda function - 0 means disable").setType(RuleParamType.INTEGER)
        .setDefaultValue("10");
    fileLines2.createParam("MaxLinesInValue") // FL0024
        .setDescription("The maximum lines in value - 0 means disable").setType(RuleParamType.INTEGER)
        .setDefaultValue("5");
    fileLines2.createParam("MaxLinesInFunction") // FL0025
        .setDescription("The maximum lines in function - 0 means disable").setType(RuleParamType.INTEGER)
        .setDefaultValue("300");
    fileLines2.createParam("MaxLinesInMember") // FL00026
        .setDescription("The maximum lines in member - 0 means disable").setType(RuleParamType.INTEGER)
        .setDefaultValue("10");
    fileLines2.createParam("MaxLinesInConstructor") // FL0027
        .setDescription("The maximum lines in constructor - 0 means disable").setType(RuleParamType.INTEGER)
        .setDefaultValue("5");
    fileLines2.createParam("MaxLinesInProperty") // FL0028
        .setDescription("The maximum lines in property - 0 means disable").setType(RuleParamType.INTEGER)
        .setDefaultValue("3");
    fileLines2.createParam("MaxLinesInModule") // FL0029
        .setDescription("The maximum lines in module - 0 means disable").setType(RuleParamType.INTEGER)
        .setDefaultValue("1000");
    fileLines2.createParam("MaxLinesInRecord") // FL0030
        .setDescription("The maximum lines in record - 0 means disable").setType(RuleParamType.INTEGER)
        .setDefaultValue("20");
    fileLines2.createParam("MaxLinesInEnum") // FL0031
        .setDescription("The maximum lines in enum - 0 means disable").setType(RuleParamType.INTEGER)
        .setDefaultValue("20");
    fileLines2.createParam("MaxLinesInUnion") // FL0032
        .setDescription("The maximum lines in union - 0 means disable").setType(RuleParamType.INTEGER)
        .setDefaultValue("20");
    fileLines2.createParam("MaxLinesInClass") // FL0033
        .setDescription("The maximum lines in class - 0 means disable").setType(RuleParamType.INTEGER)
        .setDefaultValue("500");

    createRule(repository, "RulesReimplementsFunction"); // FL0034
    createRule(repository, "RulesCanBeReplacedWithComposition"); // FL0035

    NewRule tuples = createRule(repository, "RulesNumberOfItemsTupleError"); // FL0051
    tuples.createParam(MAXIMUM_ALLOWED_VALUES_PARAMETER).setDescription(MAXIMUM_ALLOWED_VALUES_DEXCRIPTION)
        .setType(RuleParamType.INTEGER).setDefaultValue("5");

    NewRule members = createRule(repository, "RulesNumberOfItemsFunctionError"); // FL052
    members.createParam(MAXIMUM_ALLOWED_VALUES_PARAMETER).setDescription(MAXIMUM_ALLOWED_VALUES_DEXCRIPTION)
        .setType(RuleParamType.INTEGER).setDefaultValue("5");

    NewRule parameters = createRule(repository, "RulesNumberOfItemsClassMembersError"); // FL0053
    parameters.createParam(MAXIMUM_ALLOWED_VALUES_PARAMETER).setDescription(MAXIMUM_ALLOWED_VALUES_DEXCRIPTION)
        .setType(RuleParamType.INTEGER).setDefaultValue("5");

    NewRule booleanOperators = createRule(repository, "RulesNumberOfItemsBooleanConditionsError"); // FL054
    booleanOperators.createParam(MAXIMUM_ALLOWED_VALUES_PARAMETER).setDescription(MAXIMUM_ALLOWED_VALUES_DEXCRIPTION)
        .setType(RuleParamType.INTEGER).setDefaultValue("4");

    createRule(repository, "RulesFavourIgnoreOverLetWildError"); // FL0055
    createRule(repository, "RulesWildcardNamedWithAsPattern"); // FL0056
    createRule(repository, "RulesUselessBindingError"); // FL0057
    createRule(repository, "RulesTupleOfWildcardsError"); // FL0058

    // FL0059-FL0064 Typography
    NewRule lineLength = createRule(repository, "RulesTypographyLineLengthError"); // FL0060
    lineLength.createParam("Length").setDescription("The maximum authorized line length").setType(RuleParamType.INTEGER)
        .setDefaultValue("120");

    NewRule trailingWhiteSpace = createRule(repository, "RulesTypographyTrailingWhitespaceError"); // FL0061
    trailingWhiteSpace.createParam("NumberOfSpacesAllowed").setDescription("Number of spaces allowed")
        .setType(RuleParamType.INTEGER).setDefaultValue("4");
    trailingWhiteSpace.createParam("OneSpaceAllowedAfterOperator").setDescription("Allow space after operator")
        .setType(RuleParamType.BOOLEAN).setDefaultValue("true");
    trailingWhiteSpace.createParam("IgnoreBlankLines").setDescription("Ignore white lines")
        .setType(RuleParamType.BOOLEAN).setDefaultValue("true");

    NewRule fileLines = createRule(repository, "RulesTypographyFileLengthError"); // FL062
    fileLines.createParam("Lines").setDescription("The maximum number of lines allowed in a file")
        .setType(RuleParamType.INTEGER).setDefaultValue("1000");

    createRule(repository, "RulesTypographyTrailingLineError"); // FL0063
    createRule(repository, "RulesTypographyTabCharacterError"); // FL0064

    // FL0065 Hints
    NewRule hintRefactor = createRule(repository, "RulesHintRefactor");
    // TODO hint matcher - Map of list
    hintRefactor.createParam("Hints").setDescription("Hints to use").setType(RuleParamType.STRING).setDefaultValue("");

    // lint errors
    repository.createRule("LintSourceError").setName("Parsing errors").setSeverity(Severity.INFO)
        .setHtmlDescription("<p></p>");
    repository.createRule("LintError").setName("Lint errors").setSeverity(Severity.INFO).setHtmlDescription("<p></p>");

    repository.done();
  }

  private static NewRule createRule(NewExtendedRepository repository, String ruleKey) {
    RuleProperty p = FSharpRuleProperties.ALL.get(ruleKey);
    return repository.createRule(ruleKey).setName(p.getName()).setSeverity(p.getSeverity())
        .setHtmlDescription(p.getHtmlDescription());
  }
}
