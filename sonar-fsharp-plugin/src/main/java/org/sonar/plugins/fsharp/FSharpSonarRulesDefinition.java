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
           
    // name convention
    NewRule interfaceNaming = repository.createRule("RulesNamingConventionsInterfaceError").setName("Interface naming convention").setSeverity(Severity.MAJOR);
    NewRule exceptionNaming = repository.createRule("RulesNamingConventionsExceptionError").setName("Exception naming convention").setSeverity(Severity.MAJOR);
    NewRule typeNaming = repository.createRule("RulesNamingConventionsTypesError").setName("Type naming convention").setSeverity(Severity.MAJOR);
    NewRule recordsNaming = repository.createRule("RulesNamingConventionsRecordsError").setName("Record naming convention").setSeverity(Severity.MAJOR);
    NewRule enumNaming = repository.createRule("RulesNamingConventionsEnumError").setName("Enum naming convention").setSeverity(Severity.MAJOR);
    NewRule unionNaming = repository.createRule("RulesNamingConventionsUnionError").setName("Union naming convention").setSeverity(Severity.MAJOR);
    NewRule moduleNaming = repository.createRule("RulesNamingConventionsModuleError").setName("Module naming convention").setSeverity(Severity.MAJOR);
    NewRule literalNaming = repository.createRule("RulesNamingConventionsLiteralError").setName("Literal naming convention").setSeverity(Severity.MAJOR);
    NewRule nameSpaceNaming = repository.createRule("RulesNamingConventionsNamespaceError").setName("Namespace naming convention").setSeverity(Severity.MAJOR);    
    NewRule memberNaming = repository.createRule("RulesNamingConventionsMemberError").setName("Member naming convention").setSeverity(Severity.MAJOR);    
    NewRule paramsNaming = repository.createRule("RulesNamingConventionsParamsError").setName("Params naming convention").setSeverity(Severity.MAJOR);    
    NewRule measureNaming = repository.createRule("RulesNamingConventionsMeasureError").setName("Measure naming convention").setSeverity(Severity.MAJOR);    
    NewRule activePatternNaming = repository.createRule("RulesNamingConventionsActivePatternError").setName("Active pattern naming convention").setSeverity(Severity.MAJOR);    
    NewRule publicValuesNaming = repository.createRule("RulesNamingConventionsPublicValuesError").setName("Public values naming convention").setSeverity(Severity.MAJOR);    
    NewRule nonPublicNaming = repository.createRule("RulesNamingConventionsNonPublicError").setName("Non Public naming convention").setSeverity(Severity.MAJOR);
    
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
    
    // RulesRedundantNewKeywordError
    repository.createRule("RulesRedundantNewKeywordError").setName("Redudant usage of new Keywork").setSeverity(Severity.MAJOR);
        
    // lint errors
    repository.createRule("LintSourceError").setName("Parsing errors").setSeverity(Severity.INFO);
    repository.createRule("LintError").setName("Lint errors").setSeverity(Severity.INFO);
    
    ExternalDescriptionLoader.loadHtmlDescriptions(repository, "/org/sonar/l10n/fsharp/rules/fsharplint");
    SqaleXmlLoader.load(repository, "/com/sonar/sqale/fsharp-fslint-model.xml");
    repository.done();
  }

}
