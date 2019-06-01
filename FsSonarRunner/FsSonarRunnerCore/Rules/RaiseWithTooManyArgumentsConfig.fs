module RaiseWithTooManyArgumentsConfig

open FSharpLint.Application.XmlConfiguration.Configuration

let SonarConfiguration(config : ConfHelper.InputConfigution.AnalysisInput) =
    Map.ofList
        [
            ("RaiseWithTooManyArguments",
                {
                    Rules = Map.ofList
                        [
                            ("FailwithWithSingleArgument",
                                {
                                    Settings = Map.ofList
                                        [ ("Enabled", ConfHelper.GetEnaFlagForRule(config, "RulesFailwithWithSingleArgument")) ]
                                })
                            ("RaiseWithSingleArgument",
                                {
                                    Settings = Map.ofList
                                        [ ("Enabled", ConfHelper.GetEnaFlagForRule(config, "RulesRaiseWithSingleArgument")) ]
                                })
                            ("NullArgWithSingleArgument",
                                {
                                    Settings = Map.ofList
                                        [ ("Enabled", ConfHelper.GetEnaFlagForRule(config, "RulesNullArgWithSingleArgument")) ]
                                })
                            ("InvalidOpWithSingleArgument",
                                {
                                    Settings = Map.ofList
                                        [ ("Enabled", ConfHelper.GetEnaFlagForRule(config, "RulesInvalidOpWithSingleArgument")) ]
                                })
                            ("InvalidArgWithTwoArguments",
                                {
                                    Settings = Map.ofList
                                        [ ("Enabled", ConfHelper.GetEnaFlagForRule(config, "RulesInvalidArgWithTwoArguments")) ]
                                })
                            ("FailwithfWithArgumentsMatchingFormatString",
                                {
                                    Settings = Map.ofList
                                        [ ("Enabled", ConfHelper.GetEnaFlagForRule(config, "RulesFailwithfWithArgumentsMatchingFormatString")) ]
                                })
                        ]
                    Settings = Map.ofList
                        [
                            ("Enabled", Enabled(true))
                        ]
                });
    ]