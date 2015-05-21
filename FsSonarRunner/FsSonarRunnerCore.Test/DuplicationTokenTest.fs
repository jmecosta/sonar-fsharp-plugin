namespace FsSonarRunnerCore.Test

open System.IO
open NUnit.Framework
open FsSonarRunnerCore
open System.Reflection

[<TestFixture>]
type DuptokenTest() =
    
    let runningPath = Directory.GetParent(Assembly.GetExecutingAssembly().CodeBase.Replace("file:///", "")).ToString()
    let fileToAnalyse = Path.Combine(runningPath, "file.fs")

    [<Test>]
    member this.ShouldHandleTokens() = 
        let lines = """       type data = "akjdkas" """.Split('\r','\n')

        let duptokens = FsSonarRunnerCore.UntypedAstUtils.getDumpToken(lines)
        Assert.That(duptokens.Length, Is.EqualTo(4))


    [<Test>]
    member this.ShouldHandleHelloWorldTokens() = 
        let lines = """
          // Hello world
          let hello() =
             printfn "Hello world!" """.Split('\n')

        let duptokens = FsSonarRunnerCore.UntypedAstUtils.getDumpToken(lines)
        Assert.That(duptokens.Length, Is.EqualTo(7))
        Assert.That(duptokens.[0].Line, Is.EqualTo(1))
        Assert.That(duptokens.[0].Content, Is.EqualTo("let"))
        Assert.That(duptokens.[1].Line, Is.EqualTo(1))
        Assert.That(duptokens.[1].Content, Is.EqualTo("hello"))
        Assert.That(duptokens.[6].Line, Is.EqualTo(2))
        Assert.That(duptokens.[6].Content, Is.EqualTo("\"\""))