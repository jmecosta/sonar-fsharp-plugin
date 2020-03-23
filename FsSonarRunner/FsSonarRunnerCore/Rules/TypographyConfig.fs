module TypographyConfig

open FSharpLint.Framework.Configuration
open FSharpLint.Rules

(** Rules FL0059-FL0064 *)
let SonarConfiguration(config : ConfHelper.InputConfigution.AnalysisInput): TypographyConfig option =
    let maxCharactersOnLineConfig : RuleConfig<MaxCharactersOnLine.Config> option =
        let ruleId = "RulesTypographyLineLengthError"
        match ConfHelper.GetEnaFlagForRule(config, ruleId)  with
        | false -> None
        | true ->
            Some {
                enabled = true;
                config = Some {
                    maxCharactersOnLine = ConfHelper.GetValueForInt(config, ruleId, "Length", 200);
                }
            }

    let trailingWhitespaceOnLineConfig: RuleConfig<TrailingWhitespaceOnLine.Config> option =
        let ruleId = "RulesTypographyTrailingWhitespaceError"
        match ConfHelper.GetEnaFlagForRule(config, ruleId)  with
        | false -> None
        | true ->
            Some {
                enabled = true;
                config = Some {
                    numberOfSpacesAllowed = ConfHelper.GetValueForInt(config, ruleId, "NumberOfSpacesAllowed", 4);
                    oneSpaceAllowedAfterOperator = ConfHelper.GetValueForBool(config, ruleId, "OneSpaceAllowedAfterOperator", true);
                    ignoreBlankLines = ConfHelper.GetValueForBool(config, ruleId, "IgnoreBlankLines", true)
                }
            }

    let maxLinesInFileConfig : RuleConfig<MaxLinesInFile.Config> option =
        let ruleId = "RulesTypographyFileLengthError"
        match ConfHelper.GetEnaFlagForRule(config, ruleId)  with
        | false -> None
        | true ->
            Some {
                enabled = true;
                config = Some {maxLinesInFile = ConfHelper.GetValueForInt(config, ruleId, "Lines", 1000);}
            }

    Some {
       indentation = ConfHelper.EnableRuleIfExist(config, "RulesTypographyIndentation") // FL0059
       maxCharactersOnLine = maxCharactersOnLineConfig // FL0060
       trailingWhitespaceOnLine = trailingWhitespaceOnLineConfig // FL0061
       maxLinesInFile = maxLinesInFileConfig // FL0062
       trailingNewLineInFile = ConfHelper.EnableRuleIfExist(config, "RulesTypographyTrailingLineError") // FL0063
       noTabCharacters= ConfHelper.EnableRuleIfExist(config, "RulesTypographyTabCharacterError") // FL0064
    }
