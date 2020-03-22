module SourceLengthConfig

open FSharpLint.Framework.Configuration

[<Literal>]
let FunctionLength = 100

[<Literal>]
let LambdaFunctionLength = 7

[<Literal>]
let MatchLambdaFunctionLength = 100

[<Literal>]
let ValueLength = 100

[<Literal>]
let ConstructorLength = 100

[<Literal>]
let MemberLength = 100

[<Literal>]
let PropertyLength = 100

[<Literal>]
let ClassLength = 500

[<Literal>]
let UnionLength = 500

[<Literal>]
let RecordLength = 500

[<Literal>]
let EnumLength = 500

[<Literal>]
let ModuleLength = 1000

let SonarConfiguration(config : ConfHelper.InputConfigution.AnalysisInput) =
    None
    //Map.ofList
    //    [
    //        ("SourceLength",
    //            {
    //                Rules = Map.ofList
    //                    [
    //                        ("MaxLinesInFunction",
    //                            {
    //                                Settings = Map.ofList
    //                                    [
    //                                        ("Enabled", ConfHelper.GetEnaFlagForParam(config, "RulesSourceLengthError", "MaxLinesInFunction"))
    //                                        ("Lines", Lines(ConfHelper.GetValueForInt(config, "RulesSourceLengthError", "MaxLinesInFunction", FunctionLength)))
    //                                    ]
    //                            })
    //                        ("MaxLinesInLambdaFunction",
    //                            {
    //                                Settings = Map.ofList
    //                                    [
    //                                        ("Enabled", ConfHelper.GetEnaFlagForParam(config, "RulesSourceLengthError", "MaxLinesInLambdaFunction"))
    //                                        ("Lines", Lines(ConfHelper.GetValueForInt(config, "RulesSourceLengthError", "MaxLinesInLambdaFunction", LambdaFunctionLength)))
    //                                    ]
    //                            })
    //                        ("MaxLinesInMatchLambdaFunction",
    //                            {
    //                                Settings = Map.ofList
    //                                    [
    //                                        ("Enabled", ConfHelper.GetEnaFlagForParam(config, "RulesSourceLengthError", "MaxLinesInMatchLambdaFunction"))
    //                                        ("Lines", Lines(ConfHelper.GetValueForInt(config, "RulesSourceLengthError", "MaxLinesInMatchLambdaFunction", MatchLambdaFunctionLength)))
    //                                    ]
    //                            })
    //                        ("MaxLinesInValue",
    //                            {
    //                                Settings = Map.ofList
    //                                    [
    //                                        ("Enabled", ConfHelper.GetEnaFlagForParam(config, "RulesSourceLengthError", "MaxLinesInValue"))
    //                                        ("Lines", Lines(ConfHelper.GetValueForInt(config, "RulesSourceLengthError", "MaxLinesInValue", ValueLength)))
    //                                    ]
    //                            })
    //                        ("MaxLinesInConstructor",
    //                            {
    //                                Settings = Map.ofList
    //                                    [
    //                                        ("Enabled", ConfHelper.GetEnaFlagForParam(config, "RulesSourceLengthError", "MaxLinesInConstructor"))
    //                                        ("Lines", Lines(ConfHelper.GetValueForInt(config, "RulesSourceLengthError", "MaxLinesInConstructor", ConstructorLength)))
    //                                    ]
    //                            })
    //                        ("MaxLinesInMember",
    //                            {
    //                                Settings = Map.ofList
    //                                    [
    //                                        ("Enabled", ConfHelper.GetEnaFlagForParam(config, "RulesSourceLengthError", "MaxLinesInMember"))
    //                                        ("Lines", Lines(ConfHelper.GetValueForInt(config, "RulesSourceLengthError", "MaxLinesInMember", MemberLength)))
    //                                    ]
    //                            })
    //                        ("MaxLinesInProperty",
    //                            {
    //                                Settings = Map.ofList
    //                                    [
    //                                        ("Enabled", ConfHelper.GetEnaFlagForParam(config, "RulesSourceLengthError", "MaxLinesInProperty"))
    //                                        ("Lines", Lines(ConfHelper.GetValueForInt(config, "RulesSourceLengthError", "MaxLinesInProperty", PropertyLength)))
    //                                    ]
    //                            })
    //                        ("MaxLinesInClass",
    //                            {
    //                                Settings = Map.ofList
    //                                    [
    //                                        ("Enabled", ConfHelper.GetEnaFlagForParam(config, "RulesSourceLengthError", "MaxLinesInClass"))
    //                                        ("Lines", Lines(ConfHelper.GetValueForInt(config, "RulesSourceLengthError", "MaxLinesInClass", ClassLength)))
    //                                    ]
    //                            })
    //                        ("MaxLinesInEnum",
    //                            {
    //                                Settings = Map.ofList
    //                                    [
    //                                        ("Enabled", ConfHelper.GetEnaFlagForParam(config, "RulesSourceLengthError", "MaxLinesInEnum"))
    //                                        ("Lines", Lines(ConfHelper.GetValueForInt(config, "RulesSourceLengthError", "MaxLinesInEnum", EnumLength)))
    //                                    ]
    //                            })
    //                        ("MaxLinesInUnion",
    //                            {
    //                                Settings = Map.ofList
    //                                    [
    //                                        ("Enabled", ConfHelper.GetEnaFlagForParam(config, "RulesSourceLengthError", "MaxLinesInUnion"))
    //                                        ("Lines", Lines(ConfHelper.GetValueForInt(config, "RulesSourceLengthError", "MaxLinesInUnion", UnionLength)))
    //                                    ]
    //                            })
    //                        ("MaxLinesInRecord",
    //                            {
    //                                Settings = Map.ofList
    //                                    [
    //                                        ("Enabled", ConfHelper.GetEnaFlagForParam(config, "RulesSourceLengthError", "MaxLinesInRecord"))
    //                                        ("Lines", Lines(ConfHelper.GetValueForInt(config, "RulesSourceLengthError", "MaxLinesInRecord", RecordLength)))
    //                                    ]
    //                            })
    //                        ("MaxLinesInModule",
    //                            {
    //                                Settings = Map.ofList
    //                                    [
    //                                        ("Enabled", ConfHelper.GetEnaFlagForParam(config, "RulesSourceLengthError", "MaxLinesInModule"))
    //                                        ("Lines", Lines(ConfHelper.GetValueForInt(config, "RulesSourceLengthError", "MaxLinesInModule", ModuleLength)))
    //                                    ]
    //                            })
    //                    ]
    //                Settings = Map.ofList
    //                    [
    //                        ("Enabled", Enabled(true))
    //                    ]
    //            })
    //    ]