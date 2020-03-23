module ConventionsConfig

open FSharpLint.Framework.Configuration
open FSharpLint.Framework.Rules
open FSharpLint.Rules

(** Rules FL0013-FL0058 *)
let SonarConfiguration(config : ConfHelper.InputConfigution.AnalysisInput): ConventionsConfig option =
    let nestedStatementsConfig : RuleConfig<NestedStatements.Config> option =
        let ruleId = "RulesNestedStatementsError"
        match ConfHelper.GetEnaFlagForRule(config, ruleId)  with
        | false -> None
        | true ->
            Some {
                enabled = true;
                config = Some { depth = ConfHelper.GetValueForInt(config, ruleId, "Depth", 5) }
            }

    let namesConfig : NamesConfig option = 
        // TODO
        let EnableNumberOfItemsRule() : RuleConfig<NamingConfig> option = None
        Some { 
            interfaceNames = EnableNumberOfItemsRule(); // FL0036
            exceptionNames = EnableNumberOfItemsRule(); // FL0037
            typeNames = EnableNumberOfItemsRule(); // FL0038
            recordFieldNames = EnableNumberOfItemsRule(); // FL0039
            enumCasesNames = EnableNumberOfItemsRule(); // FL0040
            unionCasesNames = EnableNumberOfItemsRule(); // FL0041
            moduleNames = EnableNumberOfItemsRule(); // FL0042
            literalNames = EnableNumberOfItemsRule(); // FL0043
            namespaceNames = EnableNumberOfItemsRule(); // FL0044
            memberNames = EnableNumberOfItemsRule(); // FL0045
            parameterNames = EnableNumberOfItemsRule(); // FL0046
            measureTypeNames = EnableNumberOfItemsRule(); // FL0047
            activePatternNames = EnableNumberOfItemsRule(); // FL0048
            publicValuesNames = EnableNumberOfItemsRule(); // FL0049
            nonPublicValuesNames = EnableNumberOfItemsRule(); // FL0050
        }

    Some {
        recursiveAsyncFunction = ConfHelper.EnableRuleIfExist(config, "RulesConventionsRecursiveAsyncFunctionError") // FL0013
        redundantNewKeyword = ConfHelper.EnableRuleIfExist(config, "RulesRedundantNewKeyword") // FL0014
        nestedStatements = nestedStatementsConfig // FL015
        reimplementsFunction = ConfHelper.EnableRuleIfExist(config, "RulesReimplementsFunction") // FL0034
        canBeReplacedWithComposition = ConfHelper.EnableRuleIfExist(config, "RulesCanBeReplacedWithComposition") // FL0035
        raiseWithTooManyArgs = RaiseWithTooManyArgumentsConfig.SonarConfiguration(config) // FL0017-FL0021
        sourceLength = SourceLengthConfig.SonarConfiguration(config) // FL0022-FL0033
        naming = namesConfig // FL0036-FL050
        numberOfItems = NumberOfItemsConfig.SonarConfiguration(config) // FL0051-FL0054
        binding = BindingConfig.SonarConfiguration(config) // FL0055-FL058
    }
