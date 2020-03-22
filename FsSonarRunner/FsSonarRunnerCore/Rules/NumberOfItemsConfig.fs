module NumberOfItemsConfig

open FSharpLint.Framework.Configuration
open FSharpLint.Rules

let SonarConfiguration(config : ConfHelper.InputConfigution.AnalysisInput) : NumberOfItemsConfig option =
    let EnableNumberOfItemsRule (ruleId : string, defaultValue: int): RuleConfig<Helper.NumberOfItems.Config> option =
        match ConfHelper.GetEnaFlagForRule(config, ruleId)  with
        | true ->
            Some {
                enabled = true;
                config = Some { maxItems = ConfHelper.GetValueForInt(config, ruleId, "MaxItems", defaultValue) } }
        | false-> None

    Some {
        maxNumberOfItemsInTuple = EnableNumberOfItemsRule("RulesNumberOfItemsTupleError", 5) // FL0051
        maxNumberOfFunctionParameters = EnableNumberOfItemsRule("RulesNumberOfItemsFunctionError", 5) // FL0052
        maxNumberOfMembers = EnableNumberOfItemsRule("RulesNumberOfItemsClassMembersError", 5) // FL0053
        maxNumberOfBooleanOperatorsInCondition = EnableNumberOfItemsRule("RulesNumberOfItemsBooleanConditionsError", 4) // FL0054
    }
