module RedundantNewKeywordConfig

open FSharpLint.Application.XmlConfiguration.Configuration

let SonarConfiguration(config : ConfHelper.InputConfigution.AnalysisInput) =
    Map.ofList
        [
            ("RedundantNewKeyword",
                {
                    Rules = Map.ofList []
                    Settings = Map.ofList
                        [
                            ("Enabled", ConfHelper.GetEnaFlagForRule(config, "RulesRedundantNewKeyword"))
                        ]
                });
    ]