namespace FsSonarRunnerCore.Test

open NUnit.Framework
open FsSonarRunnerCore

(**
 * These tests may fail if the default configuration of FSharpLint changes
 *)
[<TestFixture>]
type FSharpLintDefaultRulesConfigurationTest() =
    let config = InputConfigHandler.CreateALintConfiguration("")

    [<Test>]
    member this.NumberOfIgnoreFiles() =
        match config.ignoreFiles with
        | Some f -> Assert.AreEqual(1, f.Length)
        | None -> Assert.Fail("is None")

    [<Test>]
    member this.OldConfigIsNone() =
        Assert.AreEqual(None, config.formatting, "formatting")
        Assert.AreEqual(None, config.conventions, "conventions")
        Assert.AreEqual(None, config.typography, "typography")

    [<Test>]
    member this.GlobalConfigTest() =
        Assert.IsTrue(Option.isSome(config.Global))
        let globalConfig = config.Global.Value
        let numIndentationSpaces = Option.defaultValue 0 (globalConfig.numIndentationSpaces)
        Assert.AreEqual(4, numIndentationSpaces)

    [<Test>]
    member this.NumberOfEnabledRules() =
        let enabledRules = FSharpLint.Framework.Configuration.flattenConfig config
        Assert.AreEqual(28, enabledRules.AstNodeRules.Length, "AstNodeRules")
        Assert.AreEqual(1, enabledRules.DeprecatedRules.Length, "DeprecatedRules") // Hints
        Assert.AreEqual(0, enabledRules.LineRules.GenericLineRules.Length, "GenericLineRules")
        Assert.AreEqual(0, enabledRules.LineRules.IndentationRule |> Option.toArray |> Array.length, "IndentationRule")
        Assert.AreEqual(1, enabledRules.LineRules.NoTabCharactersRule|> Option.toArray |> Array.length, "NoTabCharactersRule")

    [<Test>]
    member this.NumberOfHintConfig() =
        match config.Hints with
        | Some h ->
            match h.add with
            | Some a -> Assert.AreEqual(108, a.Length)
            | None -> Assert.Fail("hints.add is None")
            match h.ignore with
            | Some i -> Assert.AreEqual(0, i.Length)
            | None -> Assert.Pass("hints.ignore is None")
        | _ -> Assert.Fail("hints is None")
