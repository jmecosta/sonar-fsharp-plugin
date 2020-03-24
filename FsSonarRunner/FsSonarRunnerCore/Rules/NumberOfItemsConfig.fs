module NumberOfItemsConfig

open FSharpLint.Framework.Configuration
open FSharpLint.Rules

[<Literal>]
let MaxNumberOfItemsInTuple = 4 // FL0051

[<Literal>]
let MaxNumberOfFunctionParameters = 5 // FL0052

[<Literal>]
let MaxNumberOfMembers = 32 // FL0053

[<Literal>]
let MaxNumberOfBooleanOperatorsInCondition = 4 // FL0054

(** Rules FL0051-FL0054 *)
let SonarConfiguration(config : ConfHelper.InputConfigution.AnalysisInput) : NumberOfItemsConfig option =
    let EnableNumberOfItemsRule (ruleId : string, defaultValue: int): RuleConfig<Helper.NumberOfItems.Config> option =
        match ConfHelper.RuleExists(config, ruleId)  with
        | false-> None
        | true ->
            Some {
                enabled = true;
                config = Some { maxItems = ConfHelper.GetValueForInt(config, ruleId, "MaxItems", defaultValue) } }

    Some {
        maxNumberOfItemsInTuple = EnableNumberOfItemsRule("RulesNumberOfItemsTupleError", MaxNumberOfItemsInTuple) // FL0051
        maxNumberOfFunctionParameters = EnableNumberOfItemsRule("RulesNumberOfItemsFunctionError", MaxNumberOfFunctionParameters) // FL0052
        maxNumberOfMembers = EnableNumberOfItemsRule("RulesNumberOfItemsClassMembersError", MaxNumberOfMembers) // FL0053
        maxNumberOfBooleanOperatorsInCondition = EnableNumberOfItemsRule("RulesNumberOfItemsBooleanConditionsError", MaxNumberOfBooleanOperatorsInCondition) // FL0054
    }
