module HintsConfig

open FSharpLint.Framework.Configuration

// TODO - test addHints and extend with ignoreHints
let SonarConfiguration(config : ConfHelper.InputConfigution.AnalysisInput): HintConfig option =
    let separator = [|','|]
    let addHints = ConfHelper.GetValueForStringArray(config, "RulesHintRefactor", "Hints", separator)
    let ignoreHints : string[] = [||]

    Some  {
        add = Some addHints
        ignore = Some ignoreHints
    }
