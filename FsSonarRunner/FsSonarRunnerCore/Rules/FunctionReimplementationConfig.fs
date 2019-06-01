module FunctionReimplementationConfig

open FSharpLint.Rules.FunctionReimplementation
open FSharpLint.Application.XmlConfiguration.Configuration

let SonarConfiguration(config : ConfHelper.InputConfigution.AnalysisInput) =
    Map.ofList
        [
            (AnalyserName,
                {
                    Rules = Map.ofList
                        [
                            ("CanBeReplacedWithComposition",
                                {
                                    Settings = Map.ofList
                                        [
                                            ("Enabled", ConfHelper.GetEnaFlagForRule(config, "RulesCanBeReplacedWithComposition"))
                                        ]
                                })
                            ("ReimplementsFunction",
                                {
                                    Settings = Map.ofList
                                        [
                                            ("Enabled", ConfHelper.GetEnaFlagForRule(config, "RulesReimplementsFunction"))
                                        ]
                                })
                        ]
                    Settings = Map.ofList
                        [
                            ("Enabled", Enabled(true))
                        ]
                });
    ]