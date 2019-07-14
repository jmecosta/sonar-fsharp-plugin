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
    private static final String EMPTY_HTML_DESCRIPTION = "<p></p>";

    private FSharpRuleProperties() {
        throw new IllegalStateException("Utility class");
    }

    static {
        Map<String, RuleProperty> rules = new HashMap<>();

        // Typography
        rules.put("RulesTypographyTrailingLineError", new RuleProperty(Severity.MINOR, "File should not have a trailing new line", EMPTY_HTML_DESCRIPTION));
        rules.put("RulesTypographyTabCharacterError", new RuleProperty(Severity.MAJOR, "Tabulation character should not be used", EMPTY_HTML_DESCRIPTION));
        rules.put("RulesTypographyFileLengthError", new RuleProperty(Severity.MAJOR, "File should not have too many lines", EMPTY_HTML_DESCRIPTION));
        rules.put("RulesTypographyLineLengthError", new RuleProperty(Severity.MAJOR, "Lines should not be too long", EMPTY_HTML_DESCRIPTION));
        rules.put("RulesTypographyTrailingWhitespaceError", new RuleProperty(Severity.MINOR, "Lines should not have trailing whitespace", EMPTY_HTML_DESCRIPTION));

        // nested statements
        rules.put("RulesNestedStatementsError", new RuleProperty(Severity.MAJOR, "Maximum allowed of nesting", EMPTY_HTML_DESCRIPTION));

        // hint matcher - todo Map of list
        rules.put("RulesHintRefactor", new RuleProperty(Severity.INFO, "Hint Refactor", EMPTY_HTML_DESCRIPTION));
        // rules.put("RulesHintSuggestion", new RuleProperty(Severity.INFO, "Hint Suggestion", EMPTY_HTML_DESCRIPTION));

        // name convention
        // rules.put("RulesNamingConventionsCamelCaseError", new RuleProperty(Severity.INFO, "Use PascalCasing for all public member, type, and namespace names consisting of multiple words", EMPTY_HTML_DESCRIPTION));
        // rules.put("RulesNamingConventionsPascalCaseError", new RuleProperty(Severity.INFO, "Use camelCasing for parameter names", EMPTY_HTML_DESCRIPTION));

        // RaiseWithTooManyArguments
        rules.put("RulesRaiseWithSingleArgument", new RuleProperty(Severity.MAJOR, "Expected raise to have a single argument", EMPTY_HTML_DESCRIPTION));
        rules.put("RulesFailwithWithSingleArgument", new RuleProperty(Severity.MAJOR, "Fail with should have a sigle argument", EMPTY_HTML_DESCRIPTION));
        rules.put("RulesNullArgWithSingleArgument", new RuleProperty(Severity.MAJOR, "Expected nullArg to have a single argument", EMPTY_HTML_DESCRIPTION));
        rules.put("RulesInvalidOpWithSingleArgument", new RuleProperty(Severity.MAJOR, "Expected invalidOp to have a single argument", EMPTY_HTML_DESCRIPTION));
        rules.put("RulesInvalidArgWithTwoArguments", new RuleProperty(Severity.MAJOR, "Expected invalidArg to have two arguments", EMPTY_HTML_DESCRIPTION));
        rules.put("RulesFailwithfWithArgumentsMatchingFormatString", new RuleProperty(Severity.MAJOR, "Expected failwithf's arguments to match the format string (there were too many arguments)", EMPTY_HTML_DESCRIPTION));

        // bindings
        rules.put("RulesTupleOfWildcardsError", new RuleProperty(Severity.MAJOR, "A constructor argument in a pattern that is a tuple consisting of entirely wildcards can be replaced with a single wildcard", EMPTY_HTML_DESCRIPTION));
        rules.put("RulesWildcardNamedWithAsPattern", new RuleProperty(Severity.MAJOR, "Unnecessary wildcard named using the as pattern", EMPTY_HTML_DESCRIPTION));
        rules.put("RulesUselessBindingError", new RuleProperty(Severity.MAJOR, "Useless binding", EMPTY_HTML_DESCRIPTION));
        rules.put("RulesFavourIgnoreOverLetWildError", new RuleProperty(Severity.MAJOR, "Favour using the ignore function rather than let", EMPTY_HTML_DESCRIPTION));

        // function reinplementation
        rules.put("RulesCanBeReplacedWithComposition", new RuleProperty(Severity.MAJOR, "Function composition should be used instead of current function", EMPTY_HTML_DESCRIPTION));
        rules.put("RulesReimplementsFunction", new RuleProperty(Severity.MAJOR, "Pointless function redefines", EMPTY_HTML_DESCRIPTION));

        // source length
        rules.put("RulesSourceLengthError", new RuleProperty(Severity.MAJOR, "Source length check", EMPTY_HTML_DESCRIPTION));

        // NumberOfItems
        rules.put("RulesNumberOfItemsTupleError", new RuleProperty(Severity.MAJOR, "The maximum number of tuples allowed", EMPTY_HTML_DESCRIPTION));
        rules.put("RulesNumberOfItemsClassMembersError", new RuleProperty(Severity.MAJOR, "The maximum number of members in class allowed", EMPTY_HTML_DESCRIPTION));
        rules.put("RulesNumberOfItemsFunctionError", new RuleProperty(Severity.MAJOR, "The maximum number of parameters allowed", EMPTY_HTML_DESCRIPTION));
        rules.put("RulesNumberOfItemsBooleanConditionsError", new RuleProperty(Severity.MAJOR, "Maximum allowed boolean operatores in condition", EMPTY_HTML_DESCRIPTION));

        // RulesRedundantNewKeyword
        rules.put("RulesRedundantNewKeyword", new RuleProperty(Severity.MINOR, "Redundant keyword \"new\" should be avoided", EMPTY_HTML_DESCRIPTION));

        ALL = unmodifiableMap(rules);
    }
}