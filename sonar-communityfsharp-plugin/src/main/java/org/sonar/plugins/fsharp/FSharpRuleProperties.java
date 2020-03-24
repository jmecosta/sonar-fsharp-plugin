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

        // FL0001-FL0012 Formatting

        // FL0013-FL0058 Conventions
        rules.put("RulesRedundantNewKeyword", // FL0014
                new RuleProperty(Severity.MINOR, "Redundant keyword \"new\" should be avoided",
                        EMPTY_HTML_DESCRIPTION));

        rules.put("RulesNestedStatementsError", // FL0015
                new RuleProperty(Severity.MAJOR, "Maximum allowed of nesting", EMPTY_HTML_DESCRIPTION));

        rules.put("RulesFailwithWithSingleArgument", // FL0016
                new RuleProperty(Severity.MAJOR, "Fail with should have a sigle argument", EMPTY_HTML_DESCRIPTION));
        rules.put("RulesRaiseWithSingleArgument", // FL0017
                new RuleProperty(Severity.MAJOR, "Expected raise to have a single argument", EMPTY_HTML_DESCRIPTION));
        rules.put("RulesNullArgWithSingleArgument", // FL0018
                new RuleProperty(Severity.MAJOR, "Expected nullArg to have a single argument", EMPTY_HTML_DESCRIPTION));
        rules.put("RulesInvalidOpWithSingleArgument", // FL0019
                new RuleProperty(Severity.MAJOR, "Expected invalidOp to have a single argument",
                        EMPTY_HTML_DESCRIPTION));
        rules.put("RulesInvalidArgWithTwoArguments", // FL0020
                new RuleProperty(Severity.MAJOR, "Expected invalidArg to have two arguments", EMPTY_HTML_DESCRIPTION));
        rules.put("RulesFailwithfWithArgumentsMatchingFormatString", // FL0021
                new RuleProperty(Severity.MAJOR,
                        "Expected failwithf's arguments to match the format string (there were too many arguments)",
                        EMPTY_HTML_DESCRIPTION));

        rules.put("RulesSourceLengthError", // FL0022-FL0033
                new RuleProperty(Severity.MAJOR, "Source length check", EMPTY_HTML_DESCRIPTION));

        rules.put("RulesReimplementsFunction", // FL0034
                new RuleProperty(Severity.MAJOR, "Pointless function redefines", EMPTY_HTML_DESCRIPTION));
        rules.put("RulesCanBeReplacedWithComposition", // FL0035
                new RuleProperty(Severity.MAJOR, "Function composition should be used instead of current function",
                        EMPTY_HTML_DESCRIPTION));

        rules.put("RulesNumberOfItemsTupleError", // FL0051
                new RuleProperty(Severity.MAJOR, "The maximum number of tuples allowed", EMPTY_HTML_DESCRIPTION));
        rules.put("RulesNumberOfItemsFunctionError", // FL0052
                new RuleProperty(Severity.MAJOR, "The maximum number of parameters allowed", EMPTY_HTML_DESCRIPTION));
        rules.put("RulesNumberOfItemsClassMembersError", // FL0053
                new RuleProperty(Severity.MAJOR, "The maximum number of members in class allowed",
                        EMPTY_HTML_DESCRIPTION));
        rules.put("RulesNumberOfItemsBooleanConditionsError", // FL0054
                new RuleProperty(Severity.MAJOR, "Maximum allowed boolean operatores in condition",
                        EMPTY_HTML_DESCRIPTION));

        rules.put("RulesFavourIgnoreOverLetWildError", // FL0055
                new RuleProperty(Severity.MAJOR, "Favour using the ignore function rather than let",
                        EMPTY_HTML_DESCRIPTION));
        rules.put("RulesWildcardNamedWithAsPattern", // FK0056
                new RuleProperty(Severity.MAJOR, "Unnecessary wildcard named using the as pattern",
                        EMPTY_HTML_DESCRIPTION));
        rules.put("RulesUselessBindingError", // FL0057
                new RuleProperty(Severity.MAJOR, "Useless binding", EMPTY_HTML_DESCRIPTION));
        rules.put("RulesTupleOfWildcardsError", // FL0058
                new RuleProperty(Severity.MAJOR,
                        "A constructor argument in a pattern that is a tuple consisting of entirely wildcards can be replaced with a single wildcard",
                        EMPTY_HTML_DESCRIPTION));

        // FL0059-FL0064 Typography
        rules.put("RulesTypographyLineLengthError", // FL0060
                new RuleProperty(Severity.MAJOR, "Lines should not be too long", EMPTY_HTML_DESCRIPTION));
        rules.put("RulesTypographyTrailingWhitespaceError", // FL0061
                new RuleProperty(Severity.MINOR, "Lines should not have trailing whitespace", EMPTY_HTML_DESCRIPTION));
        rules.put("RulesTypographyFileLengthError", // FL0062
                new RuleProperty(Severity.MAJOR, "File should not have too many lines", EMPTY_HTML_DESCRIPTION));
        rules.put("RulesTypographyTrailingLineError", // FL0063
                new RuleProperty(Severity.MINOR, "File should not have a trailing new line", EMPTY_HTML_DESCRIPTION));
        rules.put("RulesTypographyTabCharacterError", // FL0064
                new RuleProperty(Severity.MAJOR, "Tabulation character should not be used", EMPTY_HTML_DESCRIPTION));

        // FL0065 Hints
        rules.put("RulesHintRefactor", new RuleProperty(Severity.INFO, "Hint Refactor", EMPTY_HTML_DESCRIPTION));
        // TODO hint matcher - Map of list
        // rules.put("RulesHintSuggestion", new RuleProperty(Severity.INFO, "Hint
        // Suggestion", EMPTY_HTML_DESCRIPTION));

        ALL = unmodifiableMap(rules);
    }
}