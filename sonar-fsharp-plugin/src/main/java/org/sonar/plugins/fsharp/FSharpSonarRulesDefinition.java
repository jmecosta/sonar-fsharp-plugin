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
  @Override
  public void define(Context context) {
    NewRepository repository = context
      .createRepository(FSharpPlugin.REPOSITORY_KEY, FSharpPlugin.LANGUAGE_KEY)
      .setName(FSharpPlugin.REPOSITORY_NAME);

    // Typography
    createRule(repository, "RulesTypographyTrailingLineError");
    createRule(repository, "RulesTypographyTabCharacterError");
    NewRule fileLines = createRule(repository, "RulesTypographyFileLengthError");
    fileLines.createParam("Lines").setDescription("The maximum number of lines allowed in a file")
      .setType(RuleParamType.INTEGER).setDefaultValue("1000");
    NewRule lineLength = createRule(repository, "RulesTypographyLineLengthError");
    lineLength.createParam("Length").setDescription("The maximum authorized line length")
      .setType(RuleParamType.INTEGER).setDefaultValue("200");
    NewRule trailingWhiteSpace = createRule(repository, "RulesTypographyTrailingWhitespaceError");
    trailingWhiteSpace.createParam("NumberOfSpacesAllowed").setDescription("Number of spaces allowed")
      .setType(RuleParamType.INTEGER).setDefaultValue("4");
    trailingWhiteSpace.createParam("OneSpaceAllowedAfterOperator").setDescription("Allow space after operator")
      .setType(RuleParamType.BOOLEAN).setDefaultValue("true");
    trailingWhiteSpace.createParam("IgnoreBlankLines").setDescription("Ignore white lines")
      .setType(RuleParamType.BOOLEAN).setDefaultValue("true");

    // nested statements
    NewRule nesting = createRule(repository, "RulesNestedStatementsError");
    nesting.createParam("Depth").setDescription("Maximum depth")
      .setType(RuleParamType.INTEGER).setDefaultValue("5");

    // hint matcher - todo Map of list
    NewRule hintRefactor = createRule(repository, "RulesHintRefactor");
    hintRefactor.createParam("Hints").setDescription("Hints to use")
      .setType(RuleParamType.STRING).setDefaultValue("");
    NewRule hintSuggestions = createRule(repository, "RulesHintSuggestion");
    hintSuggestions.createParam("Hints").setDescription("Hints to use")
      .setType(RuleParamType.STRING).setDefaultValue("");

    // name convention
    NewRule typeNaming = repository.createRule("RulesNamingConventionsTypesError").setName("Type naming convention").setSeverity(Severity.MAJOR).setHtmlDescription("<p></p>");
    NewRule recordsNaming = repository.createRule("RulesNamingConventionsRecordsError").setName("Record naming convention").setSeverity(Severity.MAJOR).setHtmlDescription("<p></p>");
    NewRule enumNaming = repository.createRule("RulesNamingConventionsEnumError").setName("Enum naming convention").setSeverity(Severity.MAJOR).setHtmlDescription("<p></p>");
    NewRule unionNaming = repository.createRule("RulesNamingConventionsUnionError").setName("Union naming convention").setSeverity(Severity.MAJOR).setHtmlDescription("<p></p>");
    NewRule moduleNaming = repository.createRule("RulesNamingConventionsModuleError").setName("Module naming convention").setSeverity(Severity.MAJOR).setHtmlDescription("<p></p>");
    NewRule literalNaming = repository.createRule("RulesNamingConventionsLiteralError").setName("Literal naming convention").setSeverity(Severity.MAJOR).setHtmlDescription("<p></p>");
    NewRule nameSpaceNaming = repository.createRule("RulesNamingConventionsNamespaceError").setName("Namespace naming convention").setSeverity(Severity.MAJOR).setHtmlDescription("<p></p>");
    NewRule memberNaming = repository.createRule("RulesNamingConventionsMemberError").setName("Member naming convention").setSeverity(Severity.MAJOR).setHtmlDescription("<p></p>");
    NewRule paramsNaming = repository.createRule("RulesNamingConventionsParamsError").setName("Params naming convention").setSeverity(Severity.MAJOR).setHtmlDescription("<p></p>");
    NewRule measureNaming = repository.createRule("RulesNamingConventionsMeasureError").setName("Measure naming convention").setSeverity(Severity.MAJOR).setHtmlDescription("<p></p>");
    NewRule activePatternNaming = repository.createRule("RulesNamingConventionsActivePatternError").setName("Active pattern naming convention").setSeverity(Severity.MAJOR).setHtmlDescription("<p></p>");
    NewRule publicValuesNaming = repository.createRule("RulesNamingConventionsPublicValuesError").setName("Public values naming convention").setSeverity(Severity.MAJOR).setHtmlDescription("<p></p>");
    NewRule nonPublicNaming = repository.createRule("RulesNamingConventionsNonPublicError").setName("Non Public naming convention").setSeverity(Severity.MAJOR).setHtmlDescription("<p></p>");

    interfaceNaming.createParam("Naming").setDescription("Naming Convention: PascalCase(0), CamelCase(1)").setType(RuleParamType.multipleListOfValues("0", "1")).setDefaultValue("0");
    interfaceNaming.createParam("Underscores").setDescription("Allow underscores: None(0), AllowPrefix(1), AllowAny(2)").setType(RuleParamType.multipleListOfValues("0", "1", "2")).setDefaultValue("0");
    interfaceNaming.createParam("Prefix").setDescription("Prefix to use Convention: empty or any prefix").setType(RuleParamType.STRING).setDefaultValue("I");

    exceptionNaming.createParam("Naming").setDescription("Naming Convention: PascalCase(0), CamelCase(1)").setType(RuleParamType.multipleListOfValues("0", "1")).setDefaultValue("0");
    exceptionNaming.createParam("Underscores").setDescription("Allow underscores: None(0), AllowPrefix(1), AllowAny(2)").setType(RuleParamType.multipleListOfValues("0", "1", "2")).setDefaultValue("0");
    exceptionNaming.createParam("Suffix").setDescription("Suffix to use Convention: empty or any suffix").setType(RuleParamType.STRING).setDefaultValue("Exception");

    typeNaming.createParam("Naming").setDescription("Naming Convention: PascalCase(0), CamelCase(1)").setType(RuleParamType.multipleListOfValues("0", "1")).setDefaultValue("0");
    typeNaming.createParam("Underscores").setDescription("Allow underscores: None(0), AllowPrefix(1), AllowAny(2)").setType(RuleParamType.multipleListOfValues("0", "1", "2")).setDefaultValue("0");

    recordsNaming.createParam("Naming").setDescription("Naming Convention: PascalCase(0), CamelCase(1)").setType(RuleParamType.multipleListOfValues("0", "1")).setDefaultValue("0");
    recordsNaming.createParam("Underscores").setDescription("Allow underscores: None(0), AllowPrefix(1), AllowAny(2)").setType(RuleParamType.multipleListOfValues("0", "1", "2")).setDefaultValue("0");

    enumNaming.createParam("Naming").setDescription("Naming Convention: PascalCase(0), CamelCase(1)").setType(RuleParamType.multipleListOfValues("0", "1")).setDefaultValue("0");
    enumNaming.createParam("Underscores").setDescription("Allow underscores: None(0), AllowPrefix(1), AllowAny(2)").setType(RuleParamType.multipleListOfValues("0", "1", "2")).setDefaultValue("0");

    unionNaming.createParam("Naming").setDescription("Naming Convention: PascalCase(0), CamelCase(1)").setType(RuleParamType.multipleListOfValues("0", "1")).setDefaultValue("0");
    unionNaming.createParam("Underscores").setDescription("Allow underscores: None(0), AllowPrefix(1), AllowAny(2)").setType(RuleParamType.multipleListOfValues("0", "1", "2")).setDefaultValue("0");

    moduleNaming.createParam("Naming").setDescription("Naming Convention: PascalCase(0), CamelCase(1)").setType(RuleParamType.multipleListOfValues("0", "1")).setDefaultValue("0");
    moduleNaming.createParam("Underscores").setDescription("Allow underscores: None(0), AllowPrefix(1), AllowAny(2)").setType(RuleParamType.multipleListOfValues("0", "1", "2")).setDefaultValue("0");

    literalNaming.createParam("Naming").setDescription("Naming Convention: PascalCase(0), CamelCase(1)").setType(RuleParamType.multipleListOfValues("0", "1")).setDefaultValue("0");
    literalNaming.createParam("Underscores").setDescription("Allow underscores: None(0), AllowPrefix(1), AllowAny(2)").setType(RuleParamType.multipleListOfValues("0", "1", "2")).setDefaultValue("0");

    nameSpaceNaming.createParam("Naming").setDescription("Naming Convention: PascalCase(0), CamelCase(1)").setType(RuleParamType.multipleListOfValues("0", "1")).setDefaultValue("0");
    nameSpaceNaming.createParam("Underscores").setDescription("Allow underscores: None(0), AllowPrefix(1), AllowAny(2)").setType(RuleParamType.multipleListOfValues("0", "1", "2")).setDefaultValue("0");

    memberNaming.createParam("Naming").setDescription("Naming Convention: PascalCase(0), CamelCase(1)").setType(RuleParamType.multipleListOfValues("0", "1")).setDefaultValue("0");
    memberNaming.createParam("Underscores").setDescription("Allow underscores: None(0), AllowPrefix(1), AllowAny(2)").setType(RuleParamType.multipleListOfValues("0", "1", "2")).setDefaultValue("0");

    paramsNaming.createParam("Naming").setDescription("Naming Convention: PascalCase(0), CamelCase(1)").setType(RuleParamType.multipleListOfValues("0", "1")).setDefaultValue("1");
    paramsNaming.createParam("Underscores").setDescription("Allow underscores: None(0), AllowPrefix(1), AllowAny(2)").setType(RuleParamType.multipleListOfValues("0", "1", "2")).setDefaultValue("0");

    measureNaming.createParam("Underscores").setDescription("Allow underscores: None(0), AllowPrefix(1), AllowAny(2)").setType(RuleParamType.multipleListOfValues("0", "1", "2")).setDefaultValue("0");

    activePatternNaming.createParam("Naming").setDescription("Naming Convention: PascalCase(0), CamelCase(1)").setType(RuleParamType.multipleListOfValues("0", "1")).setDefaultValue("0");
    activePatternNaming.createParam("Underscores").setDescription("Allow underscores: None(0), AllowPrefix(1), AllowAny(2)").setType(RuleParamType.multipleListOfValues("0", "1", "2")).setDefaultValue("0");

    publicValuesNaming.createParam("Underscores").setDescription("Allow underscores: None(0), AllowPrefix(1), AllowAny(2)").setType(RuleParamType.multipleListOfValues("0", "1", "2")).setDefaultValue("0");

    nonPublicNaming.createParam("Naming").setDescription("Naming Convention: PascalCase(0), CamelCase(1)").setType(RuleParamType.multipleListOfValues("0", "1")).setDefaultValue("1");
    nonPublicNaming.createParam("Underscores").setDescription("Allow underscores: None(0), AllowPrefix(1), AllowAny(2)").setType(RuleParamType.multipleListOfValues("0", "1", "2")).setDefaultValue("0");

    // RaiseWithTooManyArguments
    createRule(repository, "RulesRaiseWithSingleArgument");
    createRule(repository, "RulesFailwithWithSingleArgument");
    createRule(repository, "RulesNullArgWithSingleArgument");
    createRule(repository, "RulesInvalidOpWithSingleArgument");
    createRule(repository, "RulesInvalidArgWithTwoArguments");
    createRule(repository, "RulesFailwithfWithArgumentsMatchingFormatString");

    // bindings
    createRule(repository, "RulesTupleOfWildcardsError");
    createRule(repository, "RulesWildcardNamedWithAsPattern");
    createRule(repository, "RulesUselessBindingError");
    createRule(repository, "RulesFavourIgnoreOverLetWildError");

    // function reinplementation
    createRule(repository, "RulesCanBeReplacedWithComposition");
    createRule(repository, "RulesReimplementsFunction");

    // source length
    NewRule fileLines2 = createRule(repository, "RulesSourceLengthError");
    fileLines2.createParam("MaxLinesInFunction").setDescription("The maximum lines in function - 0 means disable")
      .setType(RuleParamType.INTEGER).setDefaultValue("300");
    fileLines2.createParam("MaxLinesInLambdaFunction").setDescription("The maximum lines in lambda function - 0 means disable")
      .setType(RuleParamType.INTEGER).setDefaultValue("10");
    fileLines2.createParam("MaxLinesInMatchLambdaFunction").setDescription("The maximum lines in  Match lambda function - 0 means disable")
      .setType(RuleParamType.INTEGER).setDefaultValue("10");
    fileLines2.createParam("MaxLinesInValue").setDescription("The maximum lines in value - 0 means disable")
      .setType(RuleParamType.INTEGER).setDefaultValue("5");
    fileLines2.createParam("MaxLinesInConstructor").setDescription("The maximum lines in constructor - 0 means disable")
      .setType(RuleParamType.INTEGER).setDefaultValue("5");
    fileLines2.createParam("MaxLinesInMember").setDescription("The maximum lines in member - 0 means disable")
      .setType(RuleParamType.INTEGER).setDefaultValue("10");
    fileLines2.createParam("MaxLinesInProperty").setDescription("The maximum lines in property - 0 means disable")
      .setType(RuleParamType.INTEGER).setDefaultValue("3");
    fileLines2.createParam("MaxLinesInClass").setDescription("The maximum lines in class - 0 means disable")
      .setType(RuleParamType.INTEGER).setDefaultValue("500");
    fileLines2.createParam("MaxLinesInEnum").setDescription("The maximum lines in enum - 0 means disable")
      .setType(RuleParamType.INTEGER).setDefaultValue("20");
    fileLines2.createParam("MaxLinesInUnion").setDescription("The maximum lines in union - 0 means disable")
      .setType(RuleParamType.INTEGER).setDefaultValue("20");
    fileLines2.createParam("MaxLinesInRecord").setDescription("The maximum lines in record - 0 means disable")
      .setType(RuleParamType.INTEGER).setDefaultValue("20");
    fileLines2.createParam("MaxLinesInModule").setDescription("The maximum lines in module - 0 means disable")
      .setType(RuleParamType.INTEGER).setDefaultValue("1000");

    // NumberOfItems
    NewRule tuples = createRule(repository, "RulesNumberOfItemsTupleError");
    tuples.createParam("MaxItems").setDescription("Maximum allowed values")
      .setType(RuleParamType.INTEGER).setDefaultValue("5");
    NewRule parameters = createRule(repository, "RulesNumberOfItemsClassMembersError");
    parameters.createParam("MaxItems").setDescription("Maximum allowed values")
      .setType(RuleParamType.INTEGER).setDefaultValue("5");
    NewRule members = createRule(repository, "RulesNumberOfItemsFunctionError");
    members.createParam("MaxItems").setDescription("Maximum allowed values")
      .setType(RuleParamType.INTEGER).setDefaultValue("5");
    NewRule booleanOperators = createRule(repository, "RulesNumberOfItemsBooleanConditionsError");
    booleanOperators.createParam("MaxItems").setDescription("Maximum allowed values")
      .setType(RuleParamType.INTEGER).setDefaultValue("4");

    // RulesRedundantNewKeyword
    repository.createRule("RulesRedundantNewKeyword").setName("Redudant usage of new Keywork").setSeverity(Severity.MAJOR).setHtmlDescription("<p></p>");

    // lint errors
    repository.createRule("LintSourceError").setName("Parsing errors").setSeverity(Severity.INFO).setHtmlDescription("<p></p>");
    repository.createRule("LintError").setName("Lint errors").setSeverity(Severity.INFO).setHtmlDescription("<p></p>");

    createRule(repository, "RulesNamingConventionsCamelCaseError");
    createRule(repository, "RulesNamingConventionsPascalCaseError");

    repository.done();
  }

  private static NewRule createRule(NewExtendedRepository repository, String ruleKey)
  {
    RuleProperty p = FSharpRuleProperties.ALL.get(ruleKey);
    return repository.createRule(ruleKey).setName(p.getName()).setSeverity(p.getSeverity()).setHtmlDescription(p.getHtmlDescription());
  }
}
