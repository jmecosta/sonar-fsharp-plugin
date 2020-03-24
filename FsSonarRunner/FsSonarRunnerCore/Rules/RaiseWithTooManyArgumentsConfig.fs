module RaiseWithTooManyArgumentsConfig

open FSharpLint.Framework.Configuration

(** Rules FL0017-FL0021 *)
let SonarConfiguration(config : ConfHelper.InputConfigution.AnalysisInput) : RaiseWithTooManyArgsConfig option =
    Some {
        raiseWithSingleArgument = ConfHelper.EnableRule(config, "RulesRaiseWithSingleArgument") // FL0017
        nullArgWithSingleArgument = ConfHelper.EnableRule(config, "RulesNullArgWithSingleArgument") // FL0018
        invalidOpWithSingleArgument = ConfHelper.EnableRule(config, "RulesInvalidOpWithSingleArgument") // FL0019
        invalidArgWithTwoArguments = ConfHelper.EnableRule(config, "RulesInvalidArgWithTwoArguments") // FL0020
        failwithfWithArgumentsMatchingFormatString = ConfHelper.EnableRule(config, "RulesFailwithfWithArgumentsMatchingFormatString") // FL0021
    }
