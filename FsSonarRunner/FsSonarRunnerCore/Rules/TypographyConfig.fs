module TypographyConfig

open FSharpLint.Framework.Configuration
open FSharpLint.Rules

[<Literal>]
let MaxCharactersOnLine = 120 // FL0060

[<Literal>]
let NumberOfSpacesAllowed = 1 // FL0061
[<Literal>]
let OneSpaceAllowedAfterOperator = true
[<Literal>]
let IgnoreBlankLines = true

[<Literal>]
let MaxLinesInFile = 1000 // FL0062

(** Rules FL0059-FL0064 *)
let SonarConfiguration(config : ConfHelper.InputConfigution.AnalysisInput): TypographyConfig option =
    let maxCharactersOnLineConfig : RuleConfig<MaxCharactersOnLine.Config> option =
        let ruleId = "RulesTypographyLineLengthError"
        match ConfHelper.RuleExists(config, ruleId)  with
        | false -> None
        | true ->
            Some {
                enabled = true;
                config = Some {
                    maxCharactersOnLine = ConfHelper.GetValueForInt(config, ruleId, "Length", MaxCharactersOnLine);
                }
            }

    let trailingWhitespaceOnLineConfig: RuleConfig<TrailingWhitespaceOnLine.Config> option =
        let ruleId = "RulesTypographyTrailingWhitespaceError"
        match ConfHelper.RuleExists(config, ruleId)  with
        | false -> None
        | true ->
            Some {
                enabled = true;
                config = Some {
                    numberOfSpacesAllowed = ConfHelper.GetValueForInt(config, ruleId, "NumberOfSpacesAllowed", NumberOfSpacesAllowed);
                    oneSpaceAllowedAfterOperator = ConfHelper.GetValueForBool(config, ruleId, "OneSpaceAllowedAfterOperator", OneSpaceAllowedAfterOperator);
                    ignoreBlankLines = ConfHelper.GetValueForBool(config, ruleId, "IgnoreBlankLines", IgnoreBlankLines)
                }
            }

    let maxLinesInFileConfig : RuleConfig<MaxLinesInFile.Config> option =
        let ruleId = "RulesTypographyFileLengthError"
        match ConfHelper.RuleExists(config, ruleId)  with
        | false -> None
        | true ->
            Some {
                enabled = true;
                config = Some {maxLinesInFile = ConfHelper.GetValueForInt(config, ruleId, "Lines", MaxLinesInFile);}
            }

    Some {
       indentation = ConfHelper.EnableRule(config, "RulesTypographyIndentation") // FL0059
       maxCharactersOnLine = maxCharactersOnLineConfig // FL0060
       trailingWhitespaceOnLine = trailingWhitespaceOnLineConfig // FL0061
       maxLinesInFile = maxLinesInFileConfig // FL0062
       trailingNewLineInFile = ConfHelper.EnableRule(config, "RulesTypographyTrailingLineError") // FL0063
       noTabCharacters= ConfHelper.EnableRule(config, "RulesTypographyTabCharacterError") // FL0064
    }
