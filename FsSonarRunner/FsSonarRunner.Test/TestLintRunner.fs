namespace FsSonarRunnerCore.Test

open System.IO
open NUnit.Framework
open FsSonarRunnerCore
open System.Reflection

[<TestFixture>]
type TestLintRunner() =
    
    let runningPath = Directory.GetParent(Assembly.GetExecutingAssembly().CodeBase.Replace("file:///", "")).ToString()
    let fileToAnalyse = Path.Combine(runningPath, "file.fs")

    [<Test>]
    member this.RunLintInSource() = 
        let content = """
module Program

let x () =
    if true then
        if true then
            ()
        else
            ()
    else
        for i in [] do
            ()
"""
        File.WriteAllText(fileToAnalyse, content)

        let lintRunner = new FsLintRunner(fileToAnalyse, new SonarRules(), FSharpLint.Framework.Configuration.defaultConfiguration)
        Assert.That(lintRunner.ExecuteAnalysis().Length, Is.EqualTo(1))