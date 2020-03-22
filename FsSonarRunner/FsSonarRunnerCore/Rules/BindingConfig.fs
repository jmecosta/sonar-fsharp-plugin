module BindingConfig

open FSharpLint.Framework.Configuration

let SonarConfiguration(config : ConfHelper.InputConfigution.AnalysisInput): BindingConfig option =
    Some {
        favourIgnoreOverLetWild = ConfHelper.EnableRuleIfExist(config, "RulesFavourIgnoreOverLetWildError") // FL0055
        wildcardNamedWithAsPattern = ConfHelper.EnableRuleIfExist(config, "RulesWildcardNamedWithAsPattern") // FL0056
        uselessBinding = ConfHelper.EnableRuleIfExist(config, "RulesUselessBindingError") // FL0057
        tupleOfWildcards = ConfHelper.EnableRuleIfExist(config, "RulesTupleOfWildcardsError") // FL0058
    }
