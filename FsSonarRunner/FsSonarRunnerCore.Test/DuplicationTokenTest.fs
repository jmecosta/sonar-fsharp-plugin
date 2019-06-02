namespace FsSonarRunnerCore.Test

open NUnit.Framework

[<TestFixture>]
type DuptokenTest() =

    [<Test>]
    member this.ShouldHandleTokens() =
        let lines = """       type data = "akjdkas" """.Split('\r','\n')

        let duptokens = FsSonarRunnerCore.UntypedAstUtils.getDumpToken(lines)
        Assert.That(duptokens.Length, Is.EqualTo(6))

    [<Test>]
    member this.ShouldHandleHelloWorldTokens() =
        let lines = """
          // Hello world
          let hello() =
             printfn "Hello world!" """.Split('\n')

        let duptokens = FsSonarRunnerCore.UntypedAstUtils.getDumpToken(lines)

        for token in duptokens do
            let data = sprintf "==== %s %i %i %i %s ====" token.Content token.Line token.LeftColoumn token.RightColoumn token.Type
            System.Diagnostics.Debug.WriteLine(data);

        Assert.That(duptokens.Length, Is.EqualTo(12))
        Assert.That(duptokens.[0].Line, Is.EqualTo(3))
        Assert.That(duptokens.[0].Content, Is.EqualTo("let"))
        Assert.That(duptokens.[1].Line, Is.EqualTo(3))
        Assert.That(duptokens.[1].Content, Is.EqualTo("hello"))
        Assert.That(duptokens.[6].Line, Is.EqualTo(4))
        Assert.That(duptokens.[6].Content, Is.EqualTo("\"\""))