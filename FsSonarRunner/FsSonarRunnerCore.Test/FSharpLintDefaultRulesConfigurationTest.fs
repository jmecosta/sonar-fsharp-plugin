namespace FsSonarRunnerCore.Test

open NUnit.Framework
open FsSonarRunnerCore

(**
 * These tests may fail if the default configuration of FSharpLint changes
 *)
[<TestFixture>]
type FSharpLintDefaultRulesConfigurationTest() =
    let rules = SonarRules()
    let config = InputConfigHandler.CreateALintConfiguration("")

    [<Test>]
    member this.NumberOfIgnoreFiles() = 
        match config.ignoreFiles with
        | Some f -> Assert.AreEqual(1, f.Length)
        | None -> Assert.Fail("is None")

    [<Test>]
    member this.NumberOfFormattingConfig() = 
        match config.formatting with
        | Some c -> Assert.AreEqual(0, c.Flatten().Length)
        | _ -> Assert.Fail("is None")

    [<Test>]
    member this.NumberOfConventionsConfig() = 
        match config.conventions with
        | Some c -> Assert.AreEqual(26, c.Flatten().Length)
        | _ -> Assert.Fail("is None")

    [<Test>]
    member this.NumberOfTypographyConfig() = 
        match config.typography with
        | Some c -> Assert.AreEqual(1, c.Flatten().Length)
        | _ -> Assert.Fail("is None")

    [<Test>]
    member this.NumberOfHintConfig() = 
        match config.hints with
        | Some h -> 
            Assert.AreEqual(None, h.ignore, "hints.ignore should be None")
            match h.add with
            | Some a -> Assert.AreEqual(108, a.Length)
            | None -> Assert.Fail("hints.add is None")
        | _ -> Assert.Fail("hints is None")
