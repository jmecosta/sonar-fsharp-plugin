module NumberOfItemsConfig

open FSharpLint.Rules.NumberOfItems
open FSharpLint.Framework.Configuration


let SonarConfiguration(config : ConfHelper.InputConfigution.AnalysisInput) =
    Map.ofList 
        [ 
            (AnalyserName, 
                { 
                    Rules = Map.ofList 
                        [ 
                            ("MaxNumberOfFunctionParameters", 
                                { 
                                    Settings = Map.ofList 
                                        [ 
                                            ("Enabled", ConfHelper.GetEnaFlagForRule(config, "RulesNumberOfItemsFunctionError"))
                                            ("MaxItems", MaxItems(ConfHelper.GetValueForInt(config, "RulesNumberOfItemsFunctionError", "MaxItems", 5)))
                                        ] 
                                }) 
                            ("MaxNumberOfItemsInTuple", 
                                { 
                                    Settings = Map.ofList 
                                        [ 
                                            ("Enabled", ConfHelper.GetEnaFlagForRule(config, "RulesNumberOfItemsTupleError")) 
                                            ("MaxItems", MaxItems(ConfHelper.GetValueForInt(config, "RulesNumberOfItemsTupleError", "MaxItems", 5)))
                                        ] 
                                }) 
                            ("MaxNumberOfMembers", 
                                { 
                                    Settings = Map.ofList 
                                        [ 
                                            ("Enabled", ConfHelper.GetEnaFlagForRule(config, "RulesNumberOfItemsClassMembersError"))
                                            ("MaxItems", MaxItems(ConfHelper.GetValueForInt(config, "RulesNumberOfItemsClassMembersError", "MaxItems", 5)))
                                        ] 
                                }) 
                            ("MaxNumberOfBooleanOperatorsInCondition", 
                                { 
                                    Settings = Map.ofList 
                                        [ 
                                            ("Enabled", ConfHelper.GetEnaFlagForRule(config, "RulesNumberOfItemsBooleanConditionsError")) 
                                            ("MaxItems", MaxItems(ConfHelper.GetValueForInt(config, "RulesNumberOfItemsBooleanConditionsError", "MaxItems", 4)))
                                        ] 
                                }) 
                        ]
                    Settings = Map.ofList 
                        [
                            ("Enabled", Enabled(true))
                        ]
                });
    ]