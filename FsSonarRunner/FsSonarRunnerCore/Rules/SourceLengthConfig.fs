module SourceLengthConfig

open FSharpLint.Framework.Configuration
open FSharpLint.Rules

[<Literal>]
let FunctionLength = 100

[<Literal>]
let LambdaFunctionLength = 7

[<Literal>]
let MatchLambdaFunctionLength = 100

[<Literal>]
let ValueLength = 100

[<Literal>]
let ConstructorLength = 100

[<Literal>]
let MemberLength = 100

[<Literal>]
let PropertyLength = 100

[<Literal>]
let ClassLength = 500

[<Literal>]
let UnionLength = 500

[<Literal>]
let RecordLength = 500

[<Literal>]
let EnumLength = 500

[<Literal>]
let ModuleLength = 1000

(** Rules FL0022-FL0033 *)
let SonarConfiguration(config : ConfHelper.InputConfigution.AnalysisInput) : SourceLengthConfig option =
    let ruleId = "RulesSourceLengthError"
    let EnableNumberOfItemsRule (paramName : string, defaultValue: int): RuleConfig<Helper.SourceLength.Config> option =
        match ConfHelper.GetEnaFlagForRule(config, ruleId)  with
        | false-> None
        | true ->            
            Some {
                enabled = true; // TODO nur wenn Parameter <> 0
                config = Some { maxLines = ConfHelper.GetValueForInt(config, ruleId, "MaxLines", defaultValue) } }
    Some { 
        maxLinesInLambdaFunction = EnableNumberOfItemsRule("MaxLinesInLambdaFunction", LambdaFunctionLength); // FL0022
        maxLinesInMatchLambdaFunction = EnableNumberOfItemsRule("MaxLinesInMatchLambdaFunction", MatchLambdaFunctionLength); // FL0023
        maxLinesInValue = EnableNumberOfItemsRule("MaxLinesInValue", ValueLength); // FL0024
        maxLinesInFunction = EnableNumberOfItemsRule("MaxLinesInFunction", FunctionLength); // FL0025
        maxLinesInMember = EnableNumberOfItemsRule("MaxLinesInMember", MemberLength); // FL0026
        maxLinesInConstructor = EnableNumberOfItemsRule("MaxLinesInMatchLambdaFunction", MatchLambdaFunctionLength); // FL0027
        maxLinesInProperty = EnableNumberOfItemsRule("MaxLinesInProperty", PropertyLength); // FL0028
        maxLinesInModule = EnableNumberOfItemsRule("MaxLinesInModule", ModuleLength); // FL0029
        maxLinesInRecord = EnableNumberOfItemsRule("MaxLinesInRecord", RecordLength); // FL0030
        maxLinesInEnum = EnableNumberOfItemsRule("MaxLinesInEnum", EnumLength); // FL0031
        maxLinesInUnion = EnableNumberOfItemsRule("MaxLinesInUnion", UnionLength); // FL0032
        maxLinesInClass = EnableNumberOfItemsRule("MaxLinesInClass", ClassLength) // FL0033
    }
