module RaiseWithTooManyArgumentsConfig

open FSharpLint.Framework.Configuration

(** Rules FL0017-FL0021 *)
let SonarConfiguration(config : ConfHelper.InputConfigution.AnalysisInput) : RaiseWithTooManyArgsConfig option =
    Some {
        raiseWithSingleArgument = ConfHelper.EnableRuleIfExist(config, "RulesRaiseWithSingleArgument") // FL0017
        nullArgWithSingleArgument = ConfHelper.EnableRuleIfExist(config, "RulesNullArgWithSingleArgument") // FL0018
        invalidOpWithSingleArgument = ConfHelper.EnableRuleIfExist(config, "RulesInvalidOpWithSingleArgument") // FL0019
        invalidArgWithTwoArguments = ConfHelper.EnableRuleIfExist(config, "RulesInvalidArgWithTwoArguments") // FL0020
        failwithfWithArgumentsMatchingFormatString = ConfHelper.EnableRuleIfExist(config, "RulesFailwithfWithArgumentsMatchingFormatString") // FL0021
    }
