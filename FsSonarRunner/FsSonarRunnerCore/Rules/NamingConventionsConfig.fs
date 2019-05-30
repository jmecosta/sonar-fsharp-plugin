module NamingConventionsConfig

open FSharpLint.Framework.Configuration

open FSharpLint.Rules.NameConventions

let SonarConfiguration(config : ConfHelper.InputConfigution.AnalysisInput) =
    let configdata =
        Map.ofList
            [
            (AnalyserName,
                {
                    Rules = Map.ofList
                        [
                            ("InterfaceNames",
                                {
                                    Settings = Map.ofList
                                        [
                                        ("Enabled", ConfHelper.GetEnaFlagForParam(config, "RulesNamingConventionsInterfaceError", "InterfaceNames"))
                                        ("Naming", Naming(enum<Naming>(ConfHelper.GetValueForInt(config, "RulesNamingConventionsInterfaceError", "Naming", 0))))
                                        ("Underscores", Underscores(enum<NamingUnderscores>(ConfHelper.GetValueForInt(config, "RulesNamingConventionsInterfaceError", "Underscores", 1))))
                                        ("Prefix", Prefix(Some(ConfHelper.GetValueForString(config, "RulesNamingConventionsInterfaceError", "Prefix", "I")))) ]
                                })
                            ("ExceptionNames",
                                {
                                    Settings = Map.ofList
                                        [
                                        ("Enabled", ConfHelper.GetEnaFlagForParam(config, "RulesNamingConventionsExceptionError", "ExceptionNames"))
                                        ("Naming", Naming(enum<Naming>(ConfHelper.GetValueForInt(config, "RulesNamingConventionsExceptionError", "Naming", 0))))
                                        ("Underscores", Underscores(enum<NamingUnderscores>(ConfHelper.GetValueForInt(config, "RulesNamingConventionsExceptionError", "Underscores", 0))))
                                        ("Prefix", Prefix(Some(ConfHelper.GetValueForString(config, "RulesNamingConventionsExceptionError", "Prefix", "I")))) ]
                                })
                            ("TypeNames",
                                {
                                    Settings = Map.ofList
                                        [
                                        ("Enabled", ConfHelper.GetEnaFlagForParam(config, "RulesNamingConventionsTypesError", "TypeNames"))
                                        ("Naming", Naming(enum<Naming>(ConfHelper.GetValueForInt(config, "RulesNamingConventionsTypesError", "Naming", 0))))
                                        ("Underscores", Underscores(enum<NamingUnderscores>(ConfHelper.GetValueForInt(config, "RulesNamingConventionsTypesError", "Underscores", 0)))) ]
                                })
                            ("RecordFieldNames",
                                {
                                    Settings = Map.ofList
                                        [
                                        ("Enabled", ConfHelper.GetEnaFlagForParam(config, "RulesNamingConventionsRecordsError", "RecordFieldNames"))
                                        ("Naming", Naming(enum<Naming>(ConfHelper.GetValueForInt(config, "RulesNamingConventionsRecordsError", "Naming", 0))))
                                        ("Underscores", Underscores(enum<NamingUnderscores>(ConfHelper.GetValueForInt(config, "RulesNamingConventionsRecordsError", "Underscores", 0)))) ]
                                })
                            ("EnumCasesNames",
                                {
                                    Settings = Map.ofList
                                        [
                                        ("Enabled", ConfHelper.GetEnaFlagForParam(config, "RulesNamingConventionsEnumError", "EnumCasesNames"))
                                        ("Naming", Naming(enum<Naming>(ConfHelper.GetValueForInt(config, "RulesNamingConventionsEnumError", "Naming", 0))))
                                        ("Underscores", Underscores(enum<NamingUnderscores>(ConfHelper.GetValueForInt(config, "RulesNamingConventionsEnumError", "Underscores", 0)))) ]
                                })
                            ("UnionCasesNames",
                                {
                                    Settings = Map.ofList
                                        [
                                        ("Enabled", ConfHelper.GetEnaFlagForParam(config, "RulesNamingConventionsUnionError", "UnionCasesNames"))
                                        ("Naming", Naming(enum<Naming>(ConfHelper.GetValueForInt(config, "RulesNamingConventionsUnionError", "Naming", 0))))
                                        ("Underscores", Underscores(enum<NamingUnderscores>(ConfHelper.GetValueForInt(config, "RulesNamingConventionsUnionError", "Underscores", 0)))) ]
                                })
                            ("ModuleNames",
                                {
                                    Settings = Map.ofList
                                        [
                                        ("Enabled", ConfHelper.GetEnaFlagForParam(config, "RulesNamingConventionsModuleError", "ModuleNames"))
                                        ("Naming", Naming(enum<Naming>(ConfHelper.GetValueForInt(config, "RulesNamingConventionsModuleError", "Naming", 0))))
                                        ("Underscores", Underscores(enum<NamingUnderscores>(ConfHelper.GetValueForInt(config, "RulesNamingConventionsModuleError", "Underscores", 0)))) ]
                                })
                            ("LiteralNames",
                                {
                                    Settings = Map.ofList
                                        [
                                        ("Enabled", ConfHelper.GetEnaFlagForParam(config, "RulesNamingConventionsLiteralError", "LiteralNames"))
                                        ("Naming", Naming(enum<Naming>(ConfHelper.GetValueForInt(config, "RulesNamingConventionsLiteralError", "Naming", 0))))
                                        ("Underscores", Underscores(enum<NamingUnderscores>(ConfHelper.GetValueForInt(config, "RulesNamingConventionsLiteralError", "Underscores", 0)))) ]
                                })
                            ("NamespaceNames",
                                {
                                    Settings = Map.ofList
                                        [
                                        ("Enabled", ConfHelper.GetEnaFlagForParam(config, "RulesNamingConventionsNamespaceError", "NamespaceNames"))
                                        ("Naming", Naming(enum<Naming>(ConfHelper.GetValueForInt(config, "RulesNamingConventionsNamespaceError", "Naming", 0))))
                                        ("Underscores", Underscores(enum<NamingUnderscores>(ConfHelper.GetValueForInt(config, "RulesNamingConventionsNamespaceError", "Underscores", 0)))) ]
                                })
                            ("MemberNames",
                                {
                                    Settings = Map.ofList
                                        [
                                        ("Enabled", ConfHelper.GetEnaFlagForParam(config, "RulesNamingConventionsMemberError", "NamespaceNames"))
                                        ("Naming", Naming(enum<Naming>(ConfHelper.GetValueForInt(config, "RulesNamingConventionsMemberError", "Naming", 0))))
                                        ("Underscores", Underscores(enum<NamingUnderscores>(ConfHelper.GetValueForInt(config, "RulesNamingConventionsMemberError", "Underscores", 0)))) ]
                                })
                            ("ParameterNames",
                                {
                                    Settings = Map.ofList
                                        [
                                        ("Enabled", ConfHelper.GetEnaFlagForParam(config, "RulesNamingConventionsParamsError", "ParameterNames"))
                                        ("Naming", Naming(enum<Naming>(ConfHelper.GetValueForInt(config, "RulesNamingConventionsParamsError", "Naming", 1))))
                                        ("Underscores", Underscores(enum<NamingUnderscores>(ConfHelper.GetValueForInt(config, "RulesNamingConventionsParamsError", "Underscores", 0)))) ]
                                })
                            ("MeasureTypeNames",
                                {
                                    Settings = Map.ofList
                                        [
                                        ("Enabled", ConfHelper.GetEnaFlagForParam(config, "RulesNamingConventionsMeasureError", "MeasureTypeNames"))
                                        ("Underscores", Underscores(enum<NamingUnderscores>(ConfHelper.GetValueForInt(config, "RulesNamingConventionsMeasureError", "Underscores", 0)))) ]
                                })
                            ("ActivePatternNames",
                                {
                                    Settings = Map.ofList
                                        [
                                        ("Enabled", ConfHelper.GetEnaFlagForParam(config, "RulesNamingConventionsActivePatternError", "ActivePatternNames"))
                                        ("Naming", Naming(enum<Naming>(ConfHelper.GetValueForInt(config, "RulesNamingConventionsActivePatternError", "Naming", 0))))
                                        ("Underscores", Underscores(enum<NamingUnderscores>(ConfHelper.GetValueForInt(config, "RulesNamingConventionsActivePatternError", "Underscores", 0)))) ]
                                })
                            ("PublicValuesNames",
                                {
                                    Settings = Map.ofList
                                        [
                                        ("Enabled", ConfHelper.GetEnaFlagForParam(config, "RulesNamingConventionsPublicValuesError", "PublicValuesNames"))
                                        ("Underscores", Underscores(enum<NamingUnderscores>(ConfHelper.GetValueForInt(config, "RulesNamingConventionsPublicValuesError", "Underscores", 0)))) ]
                                })
                            ("NonPublicValuesNames",
                                {
                                    Settings = Map.ofList
                                        [
                                        ("Enabled", ConfHelper.GetEnaFlagForParam(config, "RulesNamingConventionsNonPublicError", "NonPublicValuesNames"))
                                        ("Naming", Naming(enum<Naming>(ConfHelper.GetValueForInt(config, "RulesNamingConventionsNonPublicError", "Naming", 1))))
                                        ("Underscores", Underscores(enum<NamingUnderscores>(ConfHelper.GetValueForInt(config, "RulesNamingConventionsNonPublicError", "Underscores", 0)))) ]
                                })
                        ]
                    Settings = Map.ofList
                        [
                            ("Enabled", Enabled(true))
                        ]
                });
            ]

    configdata