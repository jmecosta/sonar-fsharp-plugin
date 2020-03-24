module ConventionsConfig

open FSharpLint.Framework.Configuration
open FSharpLint.Framework.Rules
open FSharpLint.Rules

[<Literal>]
let NestedStatementsDepth = 8

(** Rules FL0013-FL0058 *)
let SonarConfiguration(config : ConfHelper.InputConfigution.AnalysisInput): ConventionsConfig option =

    let nestedStatementsConfig : RuleConfig<NestedStatements.Config> option =
        let ruleId = "RulesNestedStatementsError"
        match ConfHelper.RuleExists(config, ruleId)  with
        | false -> None
        | true ->
            Some {
                enabled = true;
                config = Some { depth = ConfHelper.GetValueForInt(config, ruleId, "Depth", NestedStatementsDepth) }
            }

    let pascalCase = Some NamingCase.PascalCase
    let camelCase = Some NamingCase.CamelCase

    let noUnderscores = Some NamingUnderscores.None
    let allowPrefix = Some NamingUnderscores.AllowAny

    let namesConfig : NamesConfig option =
        let enableNumberOfItemsRule(ruleId: string, naming: NamingCase option, underscores: NamingUnderscores option, prefix: string option, suffix: string option) : RuleConfig<NamingConfig> option =
            match ConfHelper.RuleExists(config, ruleId)  with
            | false -> None
            | true ->
                Some {
                    enabled = true;
                    config = Some {
                        naming = ConfHelper.GetValueForEnumOption(config, ruleId, "Naming") |> Option.orElse naming;
                        underscores = ConfHelper.GetValueForEnumOption(config, ruleId, "Underscores") |> Option.orElse underscores;
                        prefix = ConfHelper.GetValueForStringOption(config, ruleId, "Prefix") |> Option.orElse prefix
                        suffix = ConfHelper.GetValueForStringOption(config, ruleId, "Suffix") |> Option.orElse suffix
                    }
                }

        Some {
            interfaceNames = enableNumberOfItemsRule("InterfaceNames", pascalCase, noUnderscores, Some "I", None); // FL0036
            exceptionNames = enableNumberOfItemsRule("ExceptionNames", pascalCase, noUnderscores, None, Some "Exception"); // FL0037
            typeNames = enableNumberOfItemsRule("TypeNames", pascalCase, noUnderscores, None, None); // FL0038
            recordFieldNames = enableNumberOfItemsRule("RecordFieldNames", pascalCase, noUnderscores, None, None); // FL0039
            enumCasesNames = enableNumberOfItemsRule("EnumCasesNames", pascalCase, noUnderscores, None, None); // FL0040
            unionCasesNames = enableNumberOfItemsRule("UnionCasesNames", pascalCase, noUnderscores, None, None); // FL0041
            moduleNames = enableNumberOfItemsRule("ModuleNames", pascalCase, noUnderscores, None, None); // FL0042
            literalNames = enableNumberOfItemsRule("LiteralNames", pascalCase, noUnderscores, None, None); // FL0043
            namespaceNames = enableNumberOfItemsRule("NamespaceNames", pascalCase, noUnderscores, None, None); // FL0044
            memberNames = enableNumberOfItemsRule("MemberNames", pascalCase, allowPrefix, None, None); // FL0045
            parameterNames = enableNumberOfItemsRule("ParameterNames", camelCase, allowPrefix, None, None); // FL0046
            measureTypeNames = enableNumberOfItemsRule("MeasureTypeNames", None, noUnderscores, None, None); // FL0047
            activePatternNames = enableNumberOfItemsRule("ActivePatternNames", pascalCase, noUnderscores, None, None); // FL0048
            publicValuesNames = enableNumberOfItemsRule("PublicValuesNames", None, allowPrefix, None, None); // FL0049
            nonPublicValuesNames = enableNumberOfItemsRule("NonPublicValuesNames", camelCase, allowPrefix, None, None); // FL0050
        }

    Some {
        recursiveAsyncFunction = ConfHelper.EnableRule(config, "RulesConventionsRecursiveAsyncFunctionError") // FL0013
        redundantNewKeyword = ConfHelper.EnableRule(config, "RulesRedundantNewKeyword") // FL0014
        nestedStatements = nestedStatementsConfig // FL015
        reimplementsFunction = ConfHelper.EnableRule(config, "RulesReimplementsFunction") // FL0034
        canBeReplacedWithComposition = ConfHelper.EnableRule(config, "RulesCanBeReplacedWithComposition") // FL0035
        raiseWithTooManyArgs = RaiseWithTooManyArgumentsConfig.SonarConfiguration(config) // FL0017-FL0021
        sourceLength = SourceLengthConfig.SonarConfiguration(config) // FL0022-FL0033
        naming = namesConfig // FL0036-FL050
        numberOfItems = NumberOfItemsConfig.SonarConfiguration(config) // FL0051-FL0054
        binding = BindingConfig.SonarConfiguration(config) // FL0055-FL058

        // TODO missing in FSharpLint 0.13.3, see https://github.com/fsprojects/FSharpLint/issues/393
        // failwithWithSingleArgument = ConfHelper.EnableRule(config, "RulesFailwithWithSingleArgument") // FL0016
    }
