module RedundantNewKeywordConfig

open FSharpLint.Rules.RedundantNewKeyword
open FSharpLint.Framework.Configuration

let SonarConfiguration(config : ConfHelper.InputConfigution.AnalysisInput) =
    Map.ofList 
        [ 
            (AnalyserName, 
                { 
                    Rules = Map.ofList []
                    Settings = Map.ofList 
                        [ 
                            ("Enabled", ConfHelper.GetEnaFlagForRule(config, "RulesRedundantNewKeywordError"))
                        ]
                });
    ]