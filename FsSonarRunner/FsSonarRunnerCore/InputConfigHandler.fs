module InputConfigHandler

open System
open System.IO
open System.Text
open System.Text.RegularExpressions
open FsSonarRunnerCore
open System.Xml.Linq
open FSharp.Data

open FSharpLint.Rules.Binding
open FSharpLint.Framework.Configuration

type InputConfigution = XmlProvider<""" 
<AnalysisInput>
  <Settings>
    <Setting>
      <Key>sonar.cs.ignoreHeaderComments</Key>
      <Value>true</Value>
    </Setting>
  </Settings>
  <Rules>
    <Rule>
      <Key>FileLoc</Key>
      <Parameters>
        <Parameter>
          <Key>maximumFileLocThreshold</Key>
          <Value>1500</Value>
        </Parameter>
        <Parameter>
          <Key>maximumFileLocThreshold</Key>
          <Value>sdfsdfs</Value>
        </Parameter>
      </Parameters>
    </Rule>
    <Rule>
      <Key>SwitchWithoutDefault</Key>
    </Rule>
    <Rule>
      <Key>LineLength</Key>
      <Parameters>
        <Parameter>
          <Key>djhda</Key>
          <Value>true</Value>
        </Parameter>
      </Parameters>
    </Rule>
  </Rules>
  <Files>
    <File>E:\file.fs</File>
    <File>E:\file2.fs</File>
  </Files>
</AnalysisInput>
""">

let GetEnaFlagForParam(config : InputConfigution.AnalysisInput, ruleId : string, paramName : string) =
    try
        let rule = config.Rules |> Seq.find (fun c -> c.Key.Equals(ruleId))

        let enabledis = rule.Parameters.Value.Parameters |> Seq.find (fun c -> c.Key.Equals(paramName))

        if enabledis.Value.Number.Value = 0 then
            Enabled(false)
        else
            Enabled(true)
    with
    | ex -> Enabled(false)

let GetEnaFlagForRule(config : InputConfigution.AnalysisInput, ruleId : string) =
    try
        let rule = config.Rules |> Seq.find (fun c -> c.Key.Equals(ruleId))
        Enabled(true)
    with
    | ex -> Enabled(false)

let GetValueForInt(config : InputConfigution.AnalysisInput, ruleId : string, paramName : string, defaultValue : int) =
    try
        let rule = config.Rules |> Seq.find (fun c -> c.Key.Equals(ruleId))
        let enabledis = rule.Parameters.Value.Parameters |> Seq.find (fun c -> c.Key.Equals(paramName))
        enabledis.Value.Number.Value
    with
    | ex -> defaultValue

let GetValueForStringList(config : InputConfigution.AnalysisInput, ruleId : string, paramName : string, defaultValue : string List) =
    try
        let rule = config.Rules |> Seq.find (fun c -> c.Key.Equals(ruleId))
        let enabledis = rule.Parameters.Value.Parameters |> Seq.find (fun c -> c.Key.Equals(paramName))
        enabledis.Value.String.Value.Split(';') |> Array.toList
    with
    | ex -> defaultValue

let GetValueForBool(config : InputConfigution.AnalysisInput, ruleId : string, paramName : string, defaultValue : bool) =
    try
        let rule = config.Rules |> Seq.find (fun c -> c.Key.Equals(ruleId))
        let enabledis = rule.Parameters.Value.Parameters |> Seq.find (fun c -> c.Key.Equals(paramName))
        enabledis.Value.Boolean.Value
    with
    | ex -> defaultValue

let sonarConfiguration(config : InputConfigution.AnalysisInput) = 
    Map.ofList 
        [ 
            ("SourceLength", 
                { 
                    Rules = Map.ofList 
                        [ 
                            ("MaxLinesInFunction", 
                                { 
                                    Settings = Map.ofList 
                                        [ 
                                            ("Enabled", GetEnaFlagForParam(config, "RulesSourceLengthError", "MaxLinesInFunction"))
                                            ("Lines", Lines(GetValueForInt(config, "RulesSourceLengthError", "MaxLinesInFunction", 300))) 
                                        ] 
                                }) 
                            ("MaxLinesInLambdaFunction", 
                                { 
                                    Settings = Map.ofList 
                                        [ 
                                            ("Enabled", GetEnaFlagForParam(config, "RulesSourceLengthError", "MaxLinesInLambdaFunction"))
                                            ("Lines", Lines(GetValueForInt(config, "RulesSourceLengthError", "MaxLinesInLambdaFunction", 10)))
                                        ] 
                                }) 
                            ("MaxLinesInMatchLambdaFunction", 
                                { 
                                    Settings = Map.ofList 
                                        [ 
                                            ("Enabled", GetEnaFlagForParam(config, "RulesSourceLengthError", "MaxLinesInMatchLambdaFunction"))
                                            ("Lines", Lines(GetValueForInt(config, "RulesSourceLengthError", "MaxLinesInMatchLambdaFunction", 10)))
                                        ] 
                                }) 
                            ("MaxLinesInValue", 
                                { 
                                    Settings = Map.ofList 
                                        [ 
                                            ("Enabled", GetEnaFlagForParam(config, "RulesSourceLengthError", "MaxLinesInValue"))
                                            ("Lines", Lines(GetValueForInt(config, "RulesSourceLengthError", "MaxLinesInValue", 5)))
                                        ] 
                                }) 
                            ("MaxLinesInConstructor", 
                                { 
                                    Settings = Map.ofList 
                                        [ 
                                            ("Enabled", GetEnaFlagForParam(config, "RulesSourceLengthError", "MaxLinesInConstructor"))
                                            ("Lines", Lines(GetValueForInt(config, "RulesSourceLengthError", "MaxLinesInConstructor", 5)))
                                        ] 
                                }) 
                            ("MaxLinesInMember", 
                                { 
                                    Settings = Map.ofList 
                                        [ 
                                            ("Enabled", GetEnaFlagForParam(config, "RulesSourceLengthError", "MaxLinesInMember"))
                                            ("Lines", Lines(GetValueForInt(config, "RulesSourceLengthError", "MaxLinesInMember", 10))) 
                                        ] 
                                }) 
                            ("MaxLinesInProperty", 
                                { 
                                    Settings = Map.ofList 
                                        [ 
                                            ("Enabled", GetEnaFlagForParam(config, "RulesSourceLengthError", "MaxLinesInProperty"))
                                            ("Lines", Lines(GetValueForInt(config, "RulesSourceLengthError", "MaxLinesInProperty", 3))) 
                                        ] 
                                }) 
                            ("MaxLinesInClass", 
                                { 
                                    Settings = Map.ofList 
                                        [ 
                                            ("Enabled", GetEnaFlagForParam(config, "RulesSourceLengthError", "MaxLinesInClass"))
                                            ("Lines", Lines(GetValueForInt(config, "RulesSourceLengthError", "MaxLinesInClass", 500))) 
                                        ] 
                                }) 
                            ("MaxLinesInEnum", 
                                { 
                                    Settings = Map.ofList 
                                        [ 
                                            ("Enabled", GetEnaFlagForParam(config, "RulesSourceLengthError", "MaxLinesInEnum"))
                                            ("Lines", Lines(GetValueForInt(config, "RulesSourceLengthError", "MaxLinesInEnum", 20))) 
                                        ] 
                                }) 
                            ("MaxLinesInUnion", 
                                { 
                                    Settings = Map.ofList 
                                        [ 
                                            ("Enabled", GetEnaFlagForParam(config, "RulesSourceLengthError", "MaxLinesInUnion"))
                                            ("Lines", Lines(GetValueForInt(config, "RulesSourceLengthError", "MaxLinesInUnion", 20))) 
                                        ] 
                                }) 
                            ("MaxLinesInRecord", 
                                { 
                                    Settings = Map.ofList 
                                        [ 
                                            ("Enabled", GetEnaFlagForParam(config, "RulesSourceLengthError", "MaxLinesInRecord"))
                                            ("Lines", Lines(GetValueForInt(config, "RulesSourceLengthError", "MaxLinesInRecord", 20))) 
                                        ] 
                                }) 
                            ("MaxLinesInModule", 
                                { 
                                    Settings = Map.ofList 
                                        [ 
                                            ("Enabled", GetEnaFlagForParam(config, "RulesSourceLengthError", "MaxLinesInModule"))
                                            ("Lines", Lines(GetValueForInt(config, "RulesSourceLengthError", "MaxLinesInModule", 1000)))
                                        ] 
                                }) 
                        ] 
                    Settings = Map.ofList []
                });
                ("Binding", 
                    { 
                        Rules = Map.ofList 
                            [
                                ("FavourIgnoreOverLetWild", 
                                    { 
                                        Settings = Map.ofList 
                                            [ ("Enabled", GetEnaFlagForRule(config, "RulesFavourIgnoreOverLetWildError")) ] 
                                    }) 
                                ("UselessBinding", 
                                    { 
                                        Settings = Map.ofList 
                                            [ ("Enabled", GetEnaFlagForRule(config, "RulesUselessBindingError")) ]
                                    }) 
                                ("WildcardNamedWithAsPattern", 
                                    { 
                                        Settings = Map.ofList 
                                            [ ("Enabled", GetEnaFlagForRule(config, "RulesWildcardNamedWithAsPattern")) ] 
                                    }) 
                                ("TupleOfWildcards", 
                                    { 
                                        Settings = Map.ofList 
                                            [ ("Enabled", GetEnaFlagForRule(config, "RulesTupleOfWildcardsError")) ]
                                    }) 
                            ]
                        Settings = Map.ofList 
                            [
                                ("Enabled", Enabled(true))
                            ]
                    });
            ("NameConventions", 
                { 
                    Rules = Map.ofList 
                        [ 
                            ("IdentifiersMustNotContainUnderscores", 
                                { 
                                    Settings = Map.ofList 
                                        [ ("Enabled", GetEnaFlagForRule(config, "RulesNamingConventionsUnderscoreError")) ]
                                }) 
                            ("InterfaceNamesMustBeginWithI", 
                                { 
                                    Settings = Map.ofList 
                                        [ ("Enabled", GetEnaFlagForRule(config, "RulesNamingConventionsInterfaceError")) ] 
                                }) 
                            ("ExceptionNamesMustEndWithException", 
                                { 
                                    Settings = Map.ofList 
                                        [ ("Enabled", GetEnaFlagForRule(config, "RulesNamingConventionsExceptionError")) ] 
                                }) 
                            ("TypeNamesMustBePascalCase", 
                                { 
                                    Settings = Map.ofList 
                                        [ ("Enabled", GetEnaFlagForRule(config, "RulesNamingConventionsPascalCaseError")) ] 
                                }) 
                            ("ParameterMustBeCamelCase", 
                                { 
                                    Settings = Map.ofList 
                                       [ ("Enabled", GetEnaFlagForRule(config, "RulesNamingConventionsCamelCaseError")) ]
                                }) 
                            ("RecordFieldNamesMustBePascalCase", 
                                { 
                                    Settings = Map.ofList 
                                        [ ("Enabled", GetEnaFlagForRule(config, "RulesNamingConventionsPascalCaseError")) ]
                                }) 
                            ("EnumCasesMustBePascalCase", 
                                { 
                                    Settings = Map.ofList 
                                        [ ("Enabled", GetEnaFlagForRule(config, "RulesNamingConventionsPascalCaseError")) ]
                                }) 
                            ("ModuleNamesMustBePascalCase", 
                                { 
                                    Settings = Map.ofList 
                                        [ ("Enabled", GetEnaFlagForRule(config, "RulesNamingConventionsPascalCaseError")) ]
                                }) 
                            ("LiteralNamesMustBePascalCase", 
                                { 
                                    Settings = Map.ofList 
                                        [ ("Enabled", GetEnaFlagForRule(config, "RulesNamingConventionsPascalCaseError")) ]
                                }) 
                            ("NamespaceNamesMustBePascalCase", 
                                { 
                                    Settings = Map.ofList 
                                        [ ("Enabled", GetEnaFlagForRule(config, "RulesNamingConventionsPascalCaseError")) ] 
                                }) 
                            ("MemberNamesMustBePascalCase", 
                                { 
                                    Settings = Map.ofList 
                                        [ ("Enabled", GetEnaFlagForRule(config, "RulesNamingConventionsPascalCaseError")) ]
                                }) 
                            ("PublicValuesPascalOrCamelCase", 
                                { 
                                    Settings = Map.ofList 
                                        [ ("Enabled", GetEnaFlagForRule(config, "RulesNamingConventionsPascalCaseError")) ]
                                }) 
                            ("NonPublicValuesCamelCase", 
                                { 
                                    Settings = Map.ofList 
                                        [ ("Enabled", GetEnaFlagForRule(config, "RulesNamingConventionsPascalCaseError")) ] 
                                }) 
                        ] 
                    Settings = Map.ofList []
                });
            ("NumberOfItems", 
                { 
                    Rules = Map.ofList 
                        [ 
                            ("MaxNumberOfFunctionParameters", 
                                { 
                                    Settings = Map.ofList 
                                        [ 
                                            ("Enabled", GetEnaFlagForRule(config, "RulesNumberOfItemsFunctionError"))
                                            ("MaxItems", MaxItems(GetValueForInt(config, "RulesNumberOfItemsFunctionError", "MaxItems", 5)))
                                        ] 
                                }) 
                            ("MaxNumberOfItemsInTuple", 
                                { 
                                    Settings = Map.ofList 
                                        [ 
                                            ("Enabled", GetEnaFlagForRule(config, "RulesNumberOfItemsTupleError")) 
                                            ("MaxItems", MaxItems(GetValueForInt(config, "RulesNumberOfItemsTupleError", "MaxItems", 5)))
                                        ] 
                                }) 
                            ("MaxNumberOfMembers", 
                                { 
                                    Settings = Map.ofList 
                                        [ 
                                            ("Enabled", GetEnaFlagForRule(config, "RulesNumberOfItemsClassMembersError"))
                                            ("MaxItems", MaxItems(GetValueForInt(config, "RulesNumberOfItemsClassMembersError", "MaxItems", 5)))
                                        ] 
                                }) 
                            ("MaxNumberOfBooleanOperatorsInCondition", 
                                { 
                                    Settings = Map.ofList 
                                        [ 
                                            ("Enabled", GetEnaFlagForRule(config, "RulesNumberOfItemsBooleanConditionsError")) 
                                            ("MaxItems", MaxItems(GetValueForInt(config, "RulesNumberOfItemsClassMembersError", "MaxItems", 4)))
                                        ] 
                                }) 
                        ]
                    Settings = Map.ofList []
                });
            ("XmlDocumentation", 
                { 
                    Rules = Map.ofList 
                        [ 
                            ("ExceptionDefinitionHeader", 
                                { 
                                    Settings = Map.ofList 
                                        [ 
                                            ("Enabled", GetEnaFlagForRule(config, "RulesXmlDocumentationExceptionError"))
                                        ] 
                                }) 
                        ]
                    Settings = Map.ofList []
                });
            ("NestedStatements", 
                { 
                    Rules = Map.ofList []
                    Settings = Map.ofList 
                        [ 
                            ("Enabled", GetEnaFlagForRule(config, "RulesNestedStatementsError"))
                            ("Depth", Depth(GetValueForInt(config, "RulesNumberOfItemsClassMembersError", "Depth", 5)))
                        ]
                });
            ("Typography", 
                { 
                    Rules = Map.ofList 
                        [ 
                            ("MaxLinesInFile", 
                                { 
                                    Settings = Map.ofList 
                                        [ 
                                            ("Enabled", GetEnaFlagForRule(config, "RulesTypographyFileLengthError"))
                                            ("Lines", Lines(GetValueForInt(config, "RulesTypographyFileLengthError", "Lines", 1000)))
                                        ] 
                                }) 
                            ("MaxCharactersOnLine", 
                                { 
                                    Settings = Map.ofList
                                        [ 
                                            ("Enabled", GetEnaFlagForRule(config, "RulesTypographyLineLengthError"))
                                            ("Length", Lines(GetValueForInt(config, "RulesTypographyLineLengthError", "Length", 200)))
                                        ] 
                                }) 
                            ("NoTabCharacters", 
                                { 
                                    Settings = Map.ofList 
                                        [ 
                                            ("Enabled", GetEnaFlagForRule(config, "RulesTypographyTabCharacterError"))
                                        ] 
                                }) 
                            ("TrailingNewLineInFile", 
                                { 
                                    Settings = Map.ofList 
                                        [ 
                                            ("Enabled", GetEnaFlagForRule(config, "RulesTypographyTrailingLineError"))
                                        ] 
                                }) 
                            ("TrailingWhitespaceOnLine", 
                                { 
                                    Settings = Map.ofList 
                                        [ 
                                            ("Enabled", GetEnaFlagForRule(config, "RulesTypographyTrailingWhitespaceError"))
                                            ("NumberOfSpacesAllowed", NumberOfSpacesAllowed(GetValueForInt(config, "RulesTypographyTrailingWhitespaceError", "NumberOfSpacesAllowed", 4)))
                                            ("OneSpaceAllowedAfterOperator", OneSpaceAllowedAfterOperator(GetValueForBool(config, "RulesTypographyTrailingWhitespaceError", "OneSpaceAllowedAfterOperator", true)))
                                            ("IgnoreBlankLines", IgnoreBlankLines(GetValueForBool(config, "RulesTypographyTrailingWhitespaceError", "IgnoreBlankLines", true)))
                                        ] 
                                }) 
                        ]
                    Settings = Map.ofList []
                });
            ("FunctionReimplementation", 
                { 
                    Rules = Map.ofList 
                        [
                            ("CanBeReplacedWithComposition", 
                                { 
                                    Settings = Map.ofList 
                                        [ 
                                            ("Enabled", GetEnaFlagForRule(config, "RulesCanBeReplacedWithComposition"))
                                        ] 
                                }) 
                            ("ReimplementsFunction", 
                                { 
                                    Settings = Map.ofList 
                                        [ 
                                            ("Enabled", GetEnaFlagForRule(config, "RulesReimplementsFunction"))
                                        ] 
                                }) 
                        ]
                    Settings = Map.ofList 
                        [ 
                            ("Enabled", Enabled(true))
                        ]
                });
            ("CyclomaticComplexity", 
                { 
                    Rules = Map.ofList []
                    Settings = Map.ofList 
                        [ 
                            ("Enabled", GetEnaFlagForRule(config, "RulesCyclomaticComplexityError"))
                            ("MaxCyclomaticComplexity", MaxCyclomaticComplexity(GetValueForInt(config, "RulesCyclomaticComplexityError", "MaxCyclomaticComplexity", 10)))
                            ("IncludeMatchStatements", IncludeMatchStatements(GetValueForBool(config, "RulesCyclomaticComplexityError", "IncludeMatchStatements", true)))
                        ]
                });
            ("Hints", 
                { 
                    Rules = Map.ofList [] 
                    Settings = Map.ofList
                        [
                            ("Hints", Hints(GetValueForStringList(config, "RulesHintRefactor", "Hints", List.Empty)))
                        ]
                });
            ("RaiseWithTooManyArguments", 
                { 
                    Rules = Map.ofList 
                        [
                            ("FailwithWithSingleArgument", 
                                { 
                                    Settings = Map.ofList 
                                        [ ("Enabled", GetEnaFlagForRule(config, "RulesFailwithWithSingleArgument")) ] 
                                }) 
                            ("RaiseWithSingleArgument", 
                                { 
                                    Settings = Map.ofList 
                                        [ ("Enabled", GetEnaFlagForRule(config, "RulesRaiseWithSingleArgument")) ]
                                }) 
                            ("NullArgWithSingleArgument", 
                                { 
                                    Settings = Map.ofList 
                                        [ ("Enabled", GetEnaFlagForRule(config, "RulesNullArgWithSingleArgument")) ]
                                }) 
                            ("InvalidOpWithSingleArgument", 
                                { 
                                    Settings = Map.ofList 
                                        [ ("Enabled", GetEnaFlagForRule(config, "RulesInvalidOpWithSingleArgument")) ]
                                }) 
                            ("InvalidArgWithTwoArguments", 
                                { 
                                    Settings = Map.ofList 
                                        [ ("Enabled", GetEnaFlagForRule(config, "RulesInvalidArgWithTwoArguments")) ]
                                }) 
                            ("FailwithfWithArgumentsMatchingFormatString", 
                                { 
                                    Settings = Map.ofList 
                                        [ ("Enabled", GetEnaFlagForRule(config, "RulesFailwithfWithArgumentsMatchingFormatString")) ]
                                }) 
                        ]
                    Settings = Map.ofList 
                        [
                            ("Enabled", Enabled(true))
                        ]
                }) 


            ]


let CreateALintConfiguration(path : string) =
    let sonarConfig = InputConfigution.Parse(File.ReadAllText(path))

    

    let configdata = ()

    {
        Configuration.UseTypeChecker = true

        Configuration.IgnoreFiles = { Update = IgnoreFiles.Add; Files = [] }

        Analysers = sonarConfiguration(sonarConfig)
    }

