namespace FsSonarRunnerCore.Test

open System.IO
open NUnit.Framework
open FsSonarRunnerCore
open System.Reflection

[<TestFixture>]
type TestLintRunner() =

    let rec solutionDirectory =
        function
        | Some path ->
            if Path.Combine(path, "FsSonarRunner.sln") |> File.Exists
            then path
            else Path.GetDirectoryName(path) |> Option.ofObj |> solutionDirectory
        | _ ->
            failwith "Solution directory not found"

    let runningPath =
        Assembly.GetExecutingAssembly().Location.Replace("file:///", "")
        |> Some
        |> solutionDirectory
    let fileToAnalyse = Path.Combine(runningPath, "FsSonarRunnerCore.Test", "Resources", "LintTestFile.fs")

    [<Test>]
    member this.RunLintInSource() =
        let fileToAnalyseExists = File.Exists fileToAnalyse
        Assume.That(fileToAnalyseExists, "File to analyze not found")
        let lintRunner = FsLintRunner(fileToAnalyse, SonarRules(), FSharpLint.Framework.Configuration.defaultConfiguration)
        let issues = lintRunner.ExecuteAnalysis()
        issues |> List.iter (fun m -> printfn "%O" m)
        Assert.That(issues.Length, Is.EqualTo(7))
