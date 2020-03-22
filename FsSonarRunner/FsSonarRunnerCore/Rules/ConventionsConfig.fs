module ConventionsConfig

open FSharpLint.Framework.Configuration

let SonarConfiguration(config : ConfHelper.InputConfigution.AnalysisInput): ConventionsConfig option =
    Some {
        recursiveAsyncFunction = ConfHelper.EnableRuleIfExist(config, "RulesConventionsRecursiveAsyncFunctionError") // FL0013
        redundantNewKeyword = ConfHelper.EnableRuleIfExist(config, "RulesRedundantNewKeyword") // FL0014
        nestedStatements = None
        reimplementsFunction = ConfHelper.EnableRuleIfExist(config, "RulesReimplementsFunction") // FL0034
        canBeReplacedWithComposition = ConfHelper.EnableRuleIfExist(config, "RulesCanBeReplacedWithComposition") // FL0035
        raiseWithTooManyArgs = RaiseWithTooManyArgumentsConfig.SonarConfiguration(config)
        sourceLength = None
        naming = None
        numberOfItems = NumberOfItemsConfig.SonarConfiguration(config)
        binding = BindingConfig.SonarConfiguration(config)
    }
