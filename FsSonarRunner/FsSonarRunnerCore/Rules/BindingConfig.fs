module BindingConfig

open FSharpLint.Application.XmlConfiguration.Configuration

let SonarConfiguration(config : ConfHelper.InputConfigution.AnalysisInput) =
    Map.ofList
        [
            ("Binding",
                {
                    Rules = Map.ofList
                        [
                            ("FavourIgnoreOverLetWild",
                                {
                                    Settings = Map.ofList
                                        [ ("Enabled", ConfHelper.GetEnaFlagForRule(config, "RulesFavourIgnoreOverLetWildError")) ]
                                })
                            ("UselessBinding",
                                {
                                    Settings = Map.ofList
                                        [ ("Enabled", ConfHelper.GetEnaFlagForRule(config, "RulesUselessBindingError")) ]
                                })
                            ("WildcardNamedWithAsPattern",
                                {
                                    Settings = Map.ofList
                                        [ ("Enabled", ConfHelper.GetEnaFlagForRule(config, "RulesWildcardNamedWithAsPattern")) ]
                                })
                            ("TupleOfWildcards",
                                {
                                    Settings = Map.ofList
                                        [ ("Enabled", ConfHelper.GetEnaFlagForRule(config, "RulesTupleOfWildcardsError")) ]
                                })
                        ]
                    Settings = Map.ofList
                        [
                            ("Enabled", Enabled(true))
                        ]
                });
    ]