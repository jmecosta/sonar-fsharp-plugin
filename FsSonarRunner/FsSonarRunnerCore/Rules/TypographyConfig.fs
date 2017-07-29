module TypographyConfig

open FSharpLint.Rules.Typography
open FSharpLint.Framework.Configuration

let SonarConfiguration(config : ConfHelper.InputConfigution.AnalysisInput) =
    Map.ofList 
        [ 
            (AnalyserName, 
                { 
                    Rules = Map.ofList 
                        [ 
                            ("MaxLinesInFile", 
                                { 
                                    Settings = Map.ofList 
                                        [ 
                                            ("Enabled", ConfHelper.GetEnaFlagForRule(config, "RulesTypographyFileLengthError"))
                                            ("Lines", Lines(ConfHelper.GetValueForInt(config, "RulesTypographyFileLengthError", "Lines", 1000)))
                                        ] 
                                }) 
                            ("MaxCharactersOnLine", 
                                { 
                                    Settings = Map.ofList
                                        [ 
                                            ("Enabled", ConfHelper.GetEnaFlagForRule(config, "RulesTypographyLineLengthError"))
                                            ("Length", Lines(ConfHelper.GetValueForInt(config, "RulesTypographyLineLengthError", "Length", 200)))
                                        ] 
                                }) 
                            ("NoTabCharacters", 
                                { 
                                    Settings = Map.ofList 
                                        [ 
                                            ("Enabled", ConfHelper.GetEnaFlagForRule(config, "RulesTypographyTabCharacterError"))
                                        ] 
                                }) 
                            ("TrailingNewLineInFile", 
                                { 
                                    Settings = Map.ofList 
                                        [ 
                                            ("Enabled", ConfHelper.GetEnaFlagForRule(config, "RulesTypographyTrailingLineError"))
                                        ] 
                                }) 
                            ("TrailingWhitespaceOnLine", 
                                { 
                                    Settings = Map.ofList 
                                        [ 
                                            ("Enabled", ConfHelper.GetEnaFlagForRule(config, "RulesTypographyTrailingWhitespaceError"))
                                            ("NumberOfSpacesAllowed", NumberOfSpacesAllowed(ConfHelper.GetValueForInt(config, "RulesTypographyTrailingWhitespaceError", "NumberOfSpacesAllowed", 4)))
                                            ("OneSpaceAllowedAfterOperator", OneSpaceAllowedAfterOperator(ConfHelper.GetValueForBool(config, "RulesTypographyTrailingWhitespaceError", "OneSpaceAllowedAfterOperator", true)))
                                            ("IgnoreBlankLines", IgnoreBlankLines(ConfHelper.GetValueForBool(config, "RulesTypographyTrailingWhitespaceError", "IgnoreBlankLines", true)))
                                        ] 
                                }) 
                        ]
                    Settings = Map.ofList 
                        [ 
                            ("Enabled", Enabled(true))
                        ]
                });
    ]