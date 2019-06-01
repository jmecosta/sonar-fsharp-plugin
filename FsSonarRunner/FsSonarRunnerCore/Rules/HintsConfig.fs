module HintsConfig

open FSharpLint.Rules.HintMatcher
open FSharpLint.Application.XmlConfiguration.Configuration

let SonarConfiguration(config : ConfHelper.InputConfigution.AnalysisInput) =

    let hints =
        let strintData = System.Net.WebUtility.HtmlDecode(ConfHelper.GetValueForString(config, "RulesHintRefactor", "Hints", ""))
        if strintData <> "" then
            ConfHelper.GetValueForStringList(config, "RulesHintRefactor", "Hints", List.Empty)
        else
            List.Empty

    Map.ofList
        [
            (AnalyserName,
                {
                    Rules = Map.ofList []
                    Settings = Map.ofList
                        [
                            ("Enabled", ConfHelper.GetEnaFlagForRule(config, "RulesHintRefactor"))
                            ("Hints", Hints(ConfHelper.parseHints hints))
                        ]
                });
    ]