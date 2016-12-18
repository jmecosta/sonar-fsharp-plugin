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
    repository.createRule("RulesTypographyTrailingLineError").setName("File should not have a trailing new line").setSeverity(Severity.MAJOR).setHtmlDescription("<p></p>");          
    repository.createRule("RulesTypographyTabCharacterError").setName("Tabulation character should not be used").setSeverity(Severity.MAJOR).setHtmlDescription("<p></p>");  
    NewRule fileLines = repository.createRule("RulesTypographyFileLengthError").setName("File should not have too many lines").setSeverity(Severity.MAJOR).setHtmlDescription("<p></p>");  
    fileLines.createParam("Lines").setDescription("The maximum number of lines allowed in a file")
      .setType(RuleParamType.INTEGER).setDefaultValue("1000");
    NewRule lineLength = repository.createRule("RulesTypographyLineLengthError").setName("Lines should not be too long").setSeverity(Severity.MAJOR).setHtmlDescription("<p></p>");  
    lineLength.createParam("Length").setDescription("The maximum authorized line length")
      .setType(RuleParamType.INTEGER).setDefaultValue("200");
    NewRule trailingWhiteSpace = repository.createRule("RulesTypographyTrailingWhitespaceError").setName("Lines should not have trailing whitespace").setSeverity(Severity.MAJOR).setHtmlDescription("<p></p>");  
    trailingWhiteSpace.createParam("NumberOfSpacesAllowed").setDescription("Number of spaces allowed")
      .setType(RuleParamType.INTEGER).setDefaultValue("4");    
    trailingWhiteSpace.createParam("OneSpaceAllowedAfterOperator").setDescription("Allow space after operator")
      .setType(RuleParamType.BOOLEAN).setDefaultValue("true");    
    trailingWhiteSpace.createParam("IgnoreBlankLines").setDescription("Ignore white lines")
      .setType(RuleParamType.BOOLEAN).setDefaultValue("true");    
    
    // nested statements
    NewRule nesting = repository.createRule("RulesNestedStatementsError").setName("Maximum allowed of nesting").setSeverity(Severity.MAJOR).setHtmlDescription("<p></p>");  
    nesting.createParam("Depth").setDescription("Maximum depth")
      .setType(RuleParamType.INTEGER).setDefaultValue("5");       
    
    // hint matcher - todo Map of list
    NewRule hintRefactor = repository.createRule("RulesHintRefactor").setName("Hint Refactor").setSeverity(Severity.MAJOR).setHtmlDescription("<p></p>");  
    hintRefactor.createParam("Hints").setDescription("Hints to use")
      .setType(RuleParamType.STRING).setDefaultValue("");    
    NewRule hintSuggestions = repository.createRule("RulesHintSuggestion").setName("Hint Suggestion").setSeverity(Severity.MAJOR).setHtmlDescription("<p></p>");  
    hintSuggestions.createParam("Hints").setDescription("Hints to use")
      .setType(RuleParamType.STRING).setDefaultValue("");    
    
    // XmlDocumentation
    repository
            .createRule("RulesXmlDocumentationExceptionError")
            .setName("Expected exception type to have xml documentation").setSeverity(Severity.MINOR).setHtmlDescription("<p></p>");
    repository
            .createRule("RulesXmlDocumentationUnionError")
            .setName("Expected union case {0} to have xml documentation.").setSeverity(Severity.MINOR).setHtmlDescription("<p></p>");
    repository
            .createRule("RulesXmlDocumentationRecordError")
            .setName("Expected record case {0} to have xml documentation.").setSeverity(Severity.MINOR).setHtmlDescription("<p></p>");
    repository
            .createRule("RulesXmlDocumentationMemberError")
            .setName("Expected member {0} to have xml documentation.").setSeverity(Severity.MINOR).setHtmlDescription("<p></p>");    
    repository
            .createRule("RulesXmlDocumentationTypeError")
            .setName("Expected type {0} to have xml documentation.").setSeverity(Severity.MINOR).setHtmlDescription("<p></p>");    
    repository
            .createRule("RulesXmlDocumentationAutoPropertyError")
            .setName("Expected auto property {0} to have xml documentation.").setSeverity(Severity.MINOR).setHtmlDescription("<p></p>");    
    repository
            .createRule("RulesXmlDocumentationEnumError")
            .setName("Expected enum {0} to have xml documentation.").setSeverity(Severity.MINOR).setHtmlDescription("<p></p>");    
    repository
            .createRule("RulesXmlDocumentationModuleError")
            .setName("Expected module {0} to have xml documentation.").setSeverity(Severity.MINOR).setHtmlDescription("<p></p>");    
    repository
            .createRule("RulesXmlDocumentationLetError")
            .setName("Expected let to have xml documentation.").setSeverity(Severity.MINOR).setHtmlDescription("<p></p>");

        
    // name convention
    repository.createRule("RulesNamingConventionsExceptionError").setName("Exception naming convention").setSeverity(Severity.MINOR).setHtmlDescription("<p></p>");
    repository.createRule("RulesNamingConventionsUnderscoreError").setName("Identifiers naming convention").setSeverity(Severity.MINOR).setHtmlDescription("<p></p>");
    repository.createRule("RulesNamingConventionsCamelCaseError").setName("Expected camel case in types").setSeverity(Severity.MINOR).setHtmlDescription("<p></p>");
    repository.createRule("RulesNamingConventionsPascalCaseError").setName("Pascal case for parameters").setSeverity(Severity.MINOR).setHtmlDescription("<p></p>");
    repository.createRule("RulesNamingConventionsInterfaceError").setName("Interfaces naming convention").setSeverity(Severity.MINOR).setHtmlDescription("<p></p>");
    
    // RaiseWithTooManyArguments
    repository.createRule("RulesRaiseWithSingleArgument").setName("Expected raise to have a single argument").setSeverity(Severity.MAJOR).setHtmlDescription("<p></p>");  
    repository.createRule("RulesFailwithWithSingleArgument").setName("Fail with should have a sigle argument").setSeverity(Severity.MAJOR).setHtmlDescription("<p></p>");  
    repository.createRule("RulesNullArgWithSingleArgument").setName("Expected nullArg to have a single argument").setSeverity(Severity.MAJOR).setHtmlDescription("<p></p>");      
    repository.createRule("RulesInvalidOpWithSingleArgument").setName("Expected invalidOp to have a single argument").setSeverity(Severity.MAJOR).setHtmlDescription("<p></p>");      
    repository.createRule("RulesInvalidArgWithTwoArguments").setName("Expected invalidArg to have two arguments").setSeverity(Severity.MAJOR).setHtmlDescription("<p></p>");      
    repository.createRule("RulesFailwithfWithArgumentsMatchingFormatString").setName("Expected failwithf's arguments to match the format string (there were too many arguments)").setSeverity(Severity.MAJOR).setHtmlDescription("<p></p>");  
    
    // bindings
    repository.createRule("RulesTupleOfWildcardsError").setName("A constructor argument in a pattern that is a tuple consisting of entirely wildcards can be replaced with a single wildcard").setSeverity(Severity.MAJOR).setHtmlDescription("<p></p>");      
    repository.createRule("RulesWildcardNamedWithAsPattern").setName("Unnecessary wildcard named using the as pattern").setSeverity(Severity.MAJOR).setHtmlDescription("<p></p>");  
    repository.createRule("RulesUselessBindingError").setName("Useless binding").setSeverity(Severity.MAJOR).setHtmlDescription("<p></p>");  
    repository.createRule("RulesFavourIgnoreOverLetWildError").setName("Favour using the ignore function rather than let").setSeverity(Severity.MAJOR).setHtmlDescription("<p></p>");  
    
            
    // function reinplementation
    repository.createRule("RulesCanBeReplacedWithComposition").setName("Function composition should be used instead of current function").setSeverity(Severity.MAJOR).setHtmlDescription("<p></p>");  
    repository.createRule("RulesReimplementsFunction").setName("Pointless function redefines").setSeverity(Severity.MAJOR).setHtmlDescription("<p></p>");      
            
    // source length
    NewRule fileLines2 = repository.createRule("RulesSourceLengthError").setName("Source length check").setSeverity(Severity.MAJOR).setHtmlDescription("<p></p>");  
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
    NewRule tuples = repository.createRule("RulesNumberOfItemsTupleError").setName("The maximum number of tuples allowed").setSeverity(Severity.MAJOR).setHtmlDescription("<p></p>");  
    tuples.createParam("MaxItems").setDescription("Maximum allowed values")
      .setType(RuleParamType.INTEGER).setDefaultValue("5");    
    NewRule parameters = repository.createRule("RulesNumberOfItemsClassMembersError").setName("The maximum number of members in class allowed").setSeverity(Severity.MAJOR).setHtmlDescription("<p></p>");  
    parameters.createParam("MaxItems").setDescription("Maximum allowed values")
      .setType(RuleParamType.INTEGER).setDefaultValue("5");    
    NewRule members = repository.createRule("RulesNumberOfItemsFunctionError").setName("The maximum number of parameters allowed").setSeverity(Severity.MAJOR).setHtmlDescription("<p></p>"); 
    members.createParam("MaxItems").setDescription("Maximum allowed values")
      .setType(RuleParamType.INTEGER).setDefaultValue("5");          
    NewRule booleanOperators = repository.createRule("RulesNumberOfItemsBooleanConditionsError").setName("Maximum allowed boolean operatores in condition").setSeverity(Severity.MAJOR).setHtmlDescription("<p></p>");
    booleanOperators.createParam("MaxItems").setDescription("Maximum allowed values")
      .setType(RuleParamType.INTEGER).setDefaultValue("4");
      
    

    // lint errors
    repository.createRule("LintSourceError").setName("Parsing errors").setSeverity(Severity.INFO).setHtmlDescription("<p></p>");  
    repository.createRule("LintError").setName("Lint errors").setSeverity(Severity.INFO).setHtmlDescription("<p></p>");
    
    repository.done();
  }

}
