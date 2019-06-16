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

import static java.util.Collections.unmodifiableMap;

import java.util.HashMap;
import java.util.Map;

final class FSharpRuleProperties {
    public static final Map<String, RuleProperty> ALL;

    static {
        Map<String, RuleProperty> rules = new HashMap<>();

        // Typography
        rules.put("RulesTypographyTrailingLineError", new RuleProperty(Severity.MAJOR, "File should not have a trailing new line", "<p></p>"));
        rules.put("RulesTypographyTabCharacterError", new RuleProperty(Severity.MAJOR, "Tabulation character should not be used", "<p></p>"));
        rules.put("RulesTypographyFileLengthError", new RuleProperty(Severity.MAJOR, "File should not have too many lines", "<p></p>"));
        rules.put("RulesTypographyLineLengthError", new RuleProperty(Severity.MAJOR, "Lines should not be too long", "<p></p>"));
        rules.put("RulesTypographyTrailingWhitespaceError", new RuleProperty(Severity.MAJOR, "Lines should not have trailing whitespace", "<p></p>"));

        // nested statements
        rules.put("RulesNestedStatementsError", new RuleProperty(Severity.MAJOR, "Maximum allowed of nesting", "<p></p>"));

        // hint matcher - todo Map of list
        rules.put("RulesHintRefactor", new RuleProperty(Severity.MAJOR, "Hint Refactor", "<p></p>"));
        rules.put("RulesHintSuggestion", new RuleProperty(Severity.MAJOR, "Hint Suggestion", "<p></p>"));

        rules.put("RulesXmlDocumentationExceptionError", new RuleProperty(Severity.INFO, "deprecated rule", "<p></p>"));
        rules.put("RulesXmlDocumentationUnionError", new RuleProperty(Severity.INFO, "deprecated rule", "<p></p>"));
        rules.put("RulesXmlDocumentationRecordError", new RuleProperty(Severity.INFO, "deprecated rule", "<p></p>"));
        rules.put("RulesXmlDocumentationMemberError", new RuleProperty(Severity.INFO, "deprecated rule", "<p></p>"));
        rules.put("RulesXmlDocumentationTypeError", new RuleProperty(Severity.INFO, "deprecated rule", "<p></p>"));
        rules.put("RulesXmlDocumentationAutoPropertyError", new RuleProperty(Severity.INFO, "deprecated rule", "<p></p>"));
        rules.put("RulesXmlDocumentationEnumError", new RuleProperty(Severity.INFO, "deprecated rule", "<p></p>"));
        rules.put("RulesXmlDocumentationModuleError", new RuleProperty(Severity.INFO, "deprecated rule", "<p></p>"));
        rules.put("RulesXmlDocumentationLetError", new RuleProperty(Severity.INFO, "deprecated rule", "<p></p>"));

        // name convention
        rules.put("RulesNamingConventionsInterfaceError", new RuleProperty(Severity.MAJOR, "Interface naming convention", "<p></p>"));
        rules.put("RulesNamingConventionsExceptionError", new RuleProperty(Severity.MAJOR, "Exception naming convention", "<p></p>"));
        rules.put("RulesNamingConventionsCamelCaseError", new RuleProperty(Severity.INFO, "deprecated rule", "<p></p>"));
        rules.put("RulesNamingConventionsPascalCaseError", new RuleProperty(Severity.INFO, "deprecated rule", "<p></p>"));

        // RaiseWithTooManyArguments
        rules.put("RulesRaiseWithSingleArgument", new RuleProperty(Severity.MAJOR, "Expected raise to have a single argument", "<p></p>"));
        rules.put("RulesFailwithWithSingleArgument", new RuleProperty(Severity.MAJOR, "Fail with should have a sigle argument", "<p></p>"));
        rules.put("RulesNullArgWithSingleArgument", new RuleProperty(Severity.MAJOR, "Expected nullArg to have a single argument", "<p></p>"));
        rules.put("RulesInvalidOpWithSingleArgument", new RuleProperty(Severity.MAJOR, "Expected invalidOp to have a single argument", "<p></p>"));
        rules.put("RulesInvalidArgWithTwoArguments", new RuleProperty(Severity.MAJOR, "Expected invalidArg to have two arguments", "<p></p>"));
        rules.put("RulesFailwithfWithArgumentsMatchingFormatString", new RuleProperty(Severity.MAJOR, "Expected failwithf's arguments to match the format string (there were too many arguments)", "<p></p>"));

        // bindings
        rules.put("RulesTupleOfWildcardsError", new RuleProperty(Severity.MAJOR, "A constructor argument in a pattern that is a tuple consisting of entirely wildcards can be replaced with a single wildcard", "<p></p>"));
        rules.put("RulesWildcardNamedWithAsPattern", new RuleProperty(Severity.MAJOR, "Unnecessary wildcard named using the as pattern", "<p></p>"));
        rules.put("RulesUselessBindingError", new RuleProperty(Severity.MAJOR, "Useless binding", "<p></p>"));
        rules.put("RulesFavourIgnoreOverLetWildError", new RuleProperty(Severity.MAJOR, "Favour using the ignore function rather than let", "<p></p>"));

        // function reinplementation
        rules.put("RulesCanBeReplacedWithComposition", new RuleProperty(Severity.MAJOR, "Function composition should be used instead of current function", "<p></p>"));
        rules.put("RulesReimplementsFunction", new RuleProperty(Severity.MAJOR, "Pointless function redefines", "<p></p>"));

        // source length
        rules.put("RulesSourceLengthError", new RuleProperty(Severity.MAJOR, "Source length check", "<p></p>"));

        // NumberOfItems
        rules.put("RulesNumberOfItemsTupleError", new RuleProperty(Severity.MAJOR, "The maximum number of tuples allowed", "<p></p>"));
        rules.put("RulesNumberOfItemsClassMembersError", new RuleProperty(Severity.MAJOR, "The maximum number of members in class allowed", "<p></p>"));
        rules.put("RulesNumberOfItemsFunctionError", new RuleProperty(Severity.MAJOR, "The maximum number of parameters allowed", "<p></p>"));
        rules.put("RulesNumberOfItemsBooleanConditionsError", new RuleProperty(Severity.MAJOR, "Maximum allowed boolean operatores in condition", "<p></p>"));

        ALL = unmodifiableMap(rules);
    }
}