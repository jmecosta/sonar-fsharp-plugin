namespace FsSonarRunnerCore.Test

open System.IO
open NUnit.Framework
open FsSonarRunnerCore

[<TestFixture>]
type ComplexityTests() =

    [<Test>]
    member this.ComplextiyTestIfThenElse() = 
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
        let ast = Helper.getAstByContent("/file.fs", content)

        let loc, nmbclasses, autoprops, functions, complexity, fileComplexityDist, functionComplexityDist = FsSonarRunnerCore.UntypedAstUtils.getCodeMetrics(ast)
        Assert.That(complexity, Is.EqualTo(3))
        Assert.That(fileComplexityDist, Is.EqualTo("0=1;5=0;10=0;20=0;30=0;60=0;90=0"))
        ()

    [<Test>]
    member this.ComplextiyWithMatch() = 
        let content = """
module Program

let x y =
    if true then
        if true then
            ()
        else
            match y with
                | Some(y) when y > 0 -> ()
                | Some(_) -> ()
                | None -> ()
    else
        for i in [] do
            match y with
                | Some(y) when y > 0 -> ()
                | Some(_) -> ()
                | None -> ()
"""
        let ast = Helper.getAstByContent("/file.fs", content)

        let loc, nmbclasses, autoprops, functions, complexity, fileComplexityDist, functionComplexityDist = FsSonarRunnerCore.UntypedAstUtils.getCodeMetrics(ast)
        Assert.That(complexity, Is.EqualTo(7))
        Assert.That(fileComplexityDist, Is.EqualTo("0=0;5=1;10=0;20=0;30=0;60=0;90=0"))

        ()

    [<Test>]
    member this.ComplextiyTestFunctionDistribution() = 
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

let x y =
    if true then
        if true then
            ()
        else
            match y with
                | Some(y) when y > 0 -> ()
                | Some(_) -> ()
                | None -> ()
    else
        for i in [] do
            match y with
                | Some(y) when y > 0 -> ()
                | Some(_) -> ()
                | None -> ()

"""
        let ast = Helper.getAstByContent("/file.fs", content)

        let loc, nmbclasses, autoprops, functions, complexity, fileComplexityDist, functionComplexityDist = FsSonarRunnerCore.UntypedAstUtils.getCodeMetrics(ast)
        Assert.That(complexity, Is.EqualTo(10))
        Assert.That(fileComplexityDist, Is.EqualTo("0=0;5=0;10=1;20=0;30=0;60=0;90=0"))
        Assert.That(functionComplexityDist, Is.EqualTo("1=0;2=1;4=0;6=1;8=0;10=0;12=0"))
        ()