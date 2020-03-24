module BindingConfig

open FSharpLint.Framework.Configuration

(** Rules FL0055-FL0058 *)
let SonarConfiguration(config : ConfHelper.InputConfigution.AnalysisInput): BindingConfig option =
    Some {
        favourIgnoreOverLetWild = ConfHelper.EnableRule(config, "RulesFavourIgnoreOverLetWildError") // FL0055
        wildcardNamedWithAsPattern = ConfHelper.EnableRule(config, "RulesWildcardNamedWithAsPattern") // FL0056
        uselessBinding = ConfHelper.EnableRule(config, "RulesUselessBindingError") // FL0057
        tupleOfWildcards = ConfHelper.EnableRule(config, "RulesTupleOfWildcardsError") // FL0058
    }
