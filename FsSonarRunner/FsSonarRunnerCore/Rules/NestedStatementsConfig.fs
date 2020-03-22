module NestedStatementsConfig

open FSharpLint.Framework.Configuration

let SonarConfiguration(config : ConfHelper.InputConfigution.AnalysisInput) =
    None
    //Map.ofList
    //    [
    //        ("NestedStatements",
    //            {
    //                Rules = Map.ofList []
    //                Settings = Map.ofList
    //                    [
    //                        ("Enabled", ConfHelper.GetEnaFlagForRule(config, "RulesNestedStatementsError"))
    //                        ("Depth", Depth(ConfHelper.GetValueForInt(config, "RulesNestedStatementsError", "Depth", 5)))
    //                    ]
    //            });
    //]