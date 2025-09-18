namespace FsSonarRunnerCore.Test

open NUnit.Framework

(**
 * These tests may fail if the default configuration of FSharpLint changes
 *)
[<TestFixture>]
type FSharpLintDefaultRulesConfigurationTest() =
    let config = InputConfigHandler.CreateALintConfiguration("")

    [<Test>]
    member this.NumberOfIgnoreFiles() =
        match config.ignoreFiles with
        | Some f -> Assert.That(f.Length, Is.EqualTo(1))
        | None -> Assert.Fail("is None")

    [<Test>]
    member this.OldConfigIsNone() =
        Assert.That(config.formatting, Is.EqualTo(None))
        Assert.That(config.conventions, Is.EqualTo(None))
        Assert.That(config.typography, Is.EqualTo(None))

    [<Test>]
    member this.GlobalConfigTest() =
        Assert.That(Option.isSome(config.Global), Is.True)
        let globalConfig = config.Global.Value
        let numIndentationSpaces = Option.defaultValue 0 (globalConfig.numIndentationSpaces)
        Assert.That(numIndentationSpaces, Is.EqualTo(4))

    [<Test>]
    member this.NumberOfEnabledRules() =
        let enabledRules = FSharpLint.Framework.Configuration.flattenConfig config
        Assert.That(enabledRules.AstNodeRules.Length, Is.EqualTo(36))
        Assert.That(enabledRules.DeprecatedRules.Length, Is.EqualTo(1)) // Hints
        Assert.That(enabledRules.LineRules.GenericLineRules.Length, Is.EqualTo(0))
        Assert.That(enabledRules.LineRules.IndentationRule |> Option.toArray |> Array.length, Is.EqualTo(0))
        Assert.That(enabledRules.LineRules.NoTabCharactersRule |> Option.toArray |> Array.length, Is.EqualTo(1))

    [<Test>]
    member this.NumberOfHintConfig() =
        match config.Hints with
        | Some h ->
            match h.add with
            | Some a -> Assert.That(a.Length, Is.EqualTo(109))
            | None -> Assert.Fail("hints.add is None")
            match h.ignore with
            | Some i -> Assert.That(i.Length, Is.EqualTo(0))
            | None -> Assert.Pass("hints.ignore is None")
        | _ -> Assert.Fail("hints is None")
