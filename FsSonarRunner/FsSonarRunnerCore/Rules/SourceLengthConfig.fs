module SourceLengthConfig

open FSharpLint.Framework.Configuration
open FSharpLint.Rules

[<Literal>]
let LambdaFunctionLength = 7 // FL0022

[<Literal>]
let MatchLambdaFunctionLength = 100 // FL0023

[<Literal>]
let ValueLength = 100 // FL0024

[<Literal>]
let FunctionLength = 100 // FL0025

[<Literal>]
let MemberLength = 100 // FL0026

[<Literal>]
let ConstructorLength = 100 // FL0027

[<Literal>]
let PropertyLength = 70 // FL0028

[<Literal>]
let ModuleLength = 1000 // FL0029

[<Literal>]
let RecordLength = 500 // FL0030

[<Literal>]
let EnumLength = 500 // FL0031

[<Literal>]
let UnionLength = 500 // FL0032

[<Literal>]
let ClassLength = 500 // FL0033

(** Rules FL0022-FL0033 *)
let SonarConfiguration(config : ConfHelper.InputConfigution.AnalysisInput) : SourceLengthConfig option =
    let ruleId = "RulesSourceLengthError"

    let EnableNumberOfItemsRule (paramName : string, defaultValue: int): RuleConfig<Helper.SourceLength.Config> option =
        match ConfHelper.RuleExists(config, ruleId)  with
        | false-> None
        | true ->
            let maxLines = ConfHelper.GetValueForInt(config, ruleId, paramName, defaultValue)
            Some {
                enabled = maxLines > 0 // only enable if line number is positive
                config = Some { maxLines = maxLines } }
    Some {
        maxLinesInLambdaFunction = EnableNumberOfItemsRule("MaxLinesInLambdaFunction", LambdaFunctionLength); // FL0022
        maxLinesInMatchLambdaFunction = EnableNumberOfItemsRule("MaxLinesInMatchLambdaFunction", MatchLambdaFunctionLength); // FL0023
        maxLinesInValue = EnableNumberOfItemsRule("MaxLinesInValue", ValueLength); // FL0024
        maxLinesInFunction = EnableNumberOfItemsRule("MaxLinesInFunction", FunctionLength); // FL0025
        maxLinesInMember = EnableNumberOfItemsRule("MaxLinesInMember", MemberLength); // FL0026
        maxLinesInConstructor = EnableNumberOfItemsRule("MaxLinesInConstructor", ConstructorLength); // FL0027
        maxLinesInProperty = EnableNumberOfItemsRule("MaxLinesInProperty", PropertyLength); // FL0028
        maxLinesInModule = EnableNumberOfItemsRule("MaxLinesInModule", ModuleLength); // FL0029
        maxLinesInRecord = EnableNumberOfItemsRule("MaxLinesInRecord", RecordLength); // FL0030
        maxLinesInEnum = EnableNumberOfItemsRule("MaxLinesInEnum", EnumLength); // FL0031
        maxLinesInUnion = EnableNumberOfItemsRule("MaxLinesInUnion", UnionLength); // FL0032
        maxLinesInClass = EnableNumberOfItemsRule("MaxLinesInClass", ClassLength) // FL0033
    }
