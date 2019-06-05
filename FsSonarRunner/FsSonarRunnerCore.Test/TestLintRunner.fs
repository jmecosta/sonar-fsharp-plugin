namespace FsSonarRunnerCore.Test

open System.IO
open NUnit.Framework
open FsSonarRunnerCore
open System.Reflection

[<TestFixture>]
type TestLintRunner() =

    let rec SolutionDirectory path =
        if Path.Combine(path, "FsSonarRunner.sln") |> File.Exists
        then path
        else Directory.GetParent(path).ToString() |> SolutionDirectory
    let runningPath = Assembly.GetExecutingAssembly().CodeBase.Replace("file:///", "") |> SolutionDirectory
    let fileToAnalyse = Path.Combine(runningPath, "FsSonarRunnerCore.Test", "Resources", "LintTestFile.fs")

    [<Test>]
    member this.RunLintInSource() =
        let fileToAnalyseExists = File.Exists fileToAnalyse
        Assume.That(fileToAnalyseExists, "File to analyze not found")
        let lintRunner = new FsLintRunner(fileToAnalyse, new SonarRules(), FSharpLint.Framework.Configuration.defaultConfiguration)
        let issues = lintRunner.ExecuteAnalysis()
        issues |> List.iter (fun m -> printfn "%O" m)
        Assert.That(lintRunner.ExecuteAnalysis().Length, Is.EqualTo(7))
