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
package org.sonar.plugins.fsharp;

import org.sonar.api.rule.Severity;
import org.sonar.api.server.rule.RuleParamType;
import org.sonar.api.server.rule.RulesDefinition;
import org.sonar.squidbridge.rules.ExternalDescriptionLoader;
import org.sonar.squidbridge.rules.SqaleXmlLoader;

public class FSharpSonarRulesDefinition implements RulesDefinition {

  @Override
  public void define(Context context) {
    NewRepository repository = context
      .createRepository(FSharpPlugin.REPOSITORY_KEY, FSharpPlugin.LANGUAGE_KEY)
      .setName(FSharpPlugin.REPOSITORY_NAME);

    // Typography
    repository.createRule("RulesTypographyTrailingLineError").setName("File should not have a trailing new line").setSeverity(Severity.MAJOR);        
    repository.createRule("RulesTypographyTabCharacterError").setName("Tabulation character should not be used").setSeverity(Severity.MAJOR);
    NewRule fileLines = repository.createRule("RulesTypographyFileLengthError").setName("File should not have too many lines").setSeverity(Severity.MAJOR);
    fileLines.createParam("Lines").setDescription("The maximum number of lines allowed in a file")
      .setType(RuleParamType.INTEGER).setDefaultValue("1000");
    NewRule lineLength = repository.createRule("RulesTypographyLineLengthError").setName("Lines should not be too long").setSeverity(Severity.MAJOR);
    lineLength.createParam("Length").setDescription("The maximum authorized line length")
      .setType(RuleParamType.INTEGER).setDefaultValue("200");
    NewRule trailingWhiteSpace = repository.createRule("RulesTypographyTrailingWhitespaceError").setName("Lines should not have trailing whitespace").setSeverity(Severity.MAJOR);
    trailingWhiteSpace.createParam("NumberOfSpacesAllowed").setDescription("Number of spaces allowed")
      .setType(RuleParamType.INTEGER).setDefaultValue("4");    
    trailingWhiteSpace.createParam("OneSpaceAllowedAfterOperator").setDescription("Allow space after operator")
      .setType(RuleParamType.BOOLEAN).setDefaultValue("true");    
    trailingWhiteSpace.createParam("IgnoreBlankLines").setDescription("Ignore white lines")
      .setType(RuleParamType.BOOLEAN).setDefaultValue("true");    
    
    // nested statements
    NewRule nesting = repository.createRule("RulesNestedStatementsError").setName("Maximum allowed of nesting").setSeverity(Severity.MAJOR);
    nesting.createParam("Depth").setDescription("Maximum depth")
      .setType(RuleParamType.INTEGER).setDefaultValue("5");       
    
    // hint matcher - todo Map of list
    NewRule hint = repository.createRule("RulesHintRefactor").setName("Hint Refactor").setSeverity(Severity.MAJOR);
    hint.createParam("Hints").setDescription("Hints to use")
      .setType(RuleParamType.STRING).setDefaultValue("");    
   
    // XmlDocumentation
    repository.createRule("RulesXmlDocumentationExceptionError").setName("Expected exception type to have xml documentation").setSeverity(Severity.MINOR);
    
    // name convention
    repository.createRule("RulesNamingConventionsExceptionError").setName("Exception naming convention").setSeverity(Severity.MINOR);
    repository.createRule("RulesNamingConventionsUnderscoreError").setName("Identifiers naming convention").setSeverity(Severity.MINOR);
    repository.createRule("RulesNamingConventionsCamelCaseError").setName("Expected camel case in types").setSeverity(Severity.MINOR);
    repository.createRule("RulesNamingConventionsPascalCaseError").setName("Pascal case for parameters").setSeverity(Severity.MINOR);
    repository.createRule("RulesNamingConventionsInterfaceError").setName("Interfaces naming convention").setSeverity(Severity.MINOR);
    
    // RaiseWithTooManyArguments
    repository.createRule("RulesRaiseWithSingleArgument").setName("Expected raise to have a single argument").setSeverity(Severity.MAJOR);
    repository.createRule("RulesFailwithWithSingleArgument").setName("Fail with should have a sigle argument").setSeverity(Severity.MAJOR);
    repository.createRule("RulesNullArgWithSingleArgument").setName("Expected nullArg to have a single argument").setSeverity(Severity.MAJOR);    
    repository.createRule("RulesInvalidOpWithSingleArgument").setName("Expected invalidOp to have a single argument").setSeverity(Severity.MAJOR);    
    repository.createRule("RulesInvalidArgWithTwoArguments").setName("Expected invalidArg to have two arguments").setSeverity(Severity.MAJOR);    
    repository.createRule("RulesFailwithfWithArgumentsMatchingFormatString").setName("Expected failwithf's arguments to match the format string (there were too many arguments)").setSeverity(Severity.MAJOR);
    
    // bindings
    repository.createRule("RulesTupleOfWildcardsError").setName("A constructor argument in a pattern that is a tuple consisting of entirely wildcards can be replaced with a single wildcard").setSeverity(Severity.MAJOR);    
    repository.createRule("RulesWildcardNamedWithAsPattern").setName("Unnecessary wildcard named using the as pattern").setSeverity(Severity.MAJOR);
    repository.createRule("RulesUselessBindingError").setName("Useless binding").setSeverity(Severity.MAJOR);
    repository.createRule("RulesFavourIgnoreOverLetWildError").setName("Favour using the ignore function rather than let").setSeverity(Severity.MAJOR);
    
            
    // function reinplementation
    repository.createRule("RulesCanBeReplacedWithComposition").setName("Function composition should be used instead of current function").setSeverity(Severity.MAJOR);
    repository.createRule("RulesReimplementsFunction").setName("Pointless function redefines").setSeverity(Severity.MAJOR);    

    
    // complexity
    NewRule methodComplexity = repository.createRule("RulesCyclomaticComplexityError").setName("Expression Complexity should not be too high").setSeverity(Severity.MAJOR);
    methodComplexity.createParam("MaxCyclomaticComplexity").setDescription("The maximum authorized complexity in function")
      .setType(RuleParamType.INTEGER).setDefaultValue("10");    
    methodComplexity.createParam("IncludeMatchStatements").setDescription("The maximum authorized complexity in function")
      .setType(RuleParamType.BOOLEAN).setDefaultValue("true");    
    
    
    // source length
    NewRule fileLines2 = repository.createRule("RulesSourceLengthError").setName("Source length check").setSeverity(Severity.MAJOR);
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
    NewRule tuples = repository.createRule("RulesNumberOfItemsTupleError").setName("The maximum number of tuples allowed").setSeverity(Severity.MAJOR);
    tuples.createParam("MaxItems").setDescription("Maximum allowed values")
      .setType(RuleParamType.INTEGER).setDefaultValue("5");    
    NewRule parameters = repository.createRule("RulesNumberOfItemsClassMembersError").setName("The maximum number of members in class allowed").setSeverity(Severity.MAJOR);
    parameters.createParam("MaxItems").setDescription("Maximum allowed values")
      .setType(RuleParamType.INTEGER).setDefaultValue("5");    
    NewRule members = repository.createRule("RulesNumberOfItemsFunctionError").setName("The maximum number of parameters allowed").setSeverity(Severity.MAJOR);
    members.createParam("MaxItems").setDescription("Maximum allowed values")
      .setType(RuleParamType.INTEGER).setDefaultValue("5");          
    NewRule booleanOperators = repository.createRule("RulesNumberOfItemsBooleanConditionsError").setName("Maximum allowed boolean operatores in condition").setSeverity(Severity.MAJOR);
    booleanOperators.createParam("MaxItems").setDescription("Maximum allowed values")
      .setType(RuleParamType.INTEGER).setDefaultValue("4");    
    

    // lint errors
    repository.createRule("LintSourceError").setName("Parsing errors").setSeverity(Severity.INFO);
    repository.createRule("LintError").setName("Lint errors").setSeverity(Severity.INFO);
    
    ExternalDescriptionLoader.loadHtmlDescriptions(repository, "/org/sonar/l10n/fsharp/rules/fsharplint");
    SqaleXmlLoader.load(repository, "/com/sonar/sqale/fsharp-fslint-model.xml");
    repository.done();
  }

}
