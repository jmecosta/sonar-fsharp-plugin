namespace FsSonarRunnerCore.Test

open NUnit.Framework
open FsSonarRunnerCore

[<TestFixture>]
type TestTypes() =

    [<Test>]
    member this.ShouldHandleEnums() = 
        let content = """     type KeyLookUpType =
                                   // the name
                                   | Module = 0
                                   // the name
                                   | Flat = 1
                                   // the name
                                   | VSBootStrapper = 2 """

        let ast = Helper.getAstByContent("/file.fs", content)

        let loc, nmbclasses, autoprops, functions, complexity, fileComplexityDist, functionComplexityDist = FsSonarRunnerCore.UntypedAstUtils.getCodeMetrics(ast)
        Assert.That(loc.Count, Is.EqualTo(4))
        Assert.That(nmbclasses, Is.EqualTo(0))
        Assert.That(FsSonarRunnerCore.UntypedAstUtils.GetLines(ast), Is.EqualTo(7))


    [<Test>]
    member this.ShouldHandleAutoProperties() = 
        let content = """   [<AllowNullLiteral>] // does not count as source
                            type SonarModule(data) = 
                                // the name
                                member val Name : string =  data
                                member val ProjectName : string = "" with get, set
                                member val BaseDir : string = "" with get, set
                                member val Sources : string = "" with get, set
                                member val SubModules : SonarModule List = List.Empty with get, set
                                member val ParentModule : SonarModule = null with get, set """

        let ast = Helper.getAstByContent("/file.fs", content)

        let loc, nmbclasses, accessors, functions, complexity, fileComplexityDist, functionComplexityDist = FsSonarRunnerCore.UntypedAstUtils.getCodeMetrics(ast)
        Assert.That(loc.Count, Is.EqualTo(7))
        Assert.That(nmbclasses, Is.EqualTo(1))
        Assert.That(accessors, Is.EqualTo(6))
        Assert.That(FsSonarRunnerCore.UntypedAstUtils.GetLines(ast), Is.EqualTo(9))

    [<Test>]
    member this.ShouldExtractCorrectNumberOfClassTupledConstructor() = 
        let content = """   type SonarModule(tuple:int * int) = 
                                // the name
                                member val Name : string =  "" with get, set """

        let ast = Helper.getAstByContent("/file.fs", content)

        let loc, nmbclasses, autoprops, functions, complexity, fileComplexityDist, functionComplexityDist = FsSonarRunnerCore.UntypedAstUtils.getCodeMetrics(ast)
        Assert.That(loc.Count, Is.EqualTo(2))
        Assert.That(nmbclasses, Is.EqualTo(1))
        Assert.That(FsSonarRunnerCore.UntypedAstUtils.GetLines(ast), Is.EqualTo(3))

    [<Test>]
    member this.ShouldExtractCorrectNumberOfClassNonTupledConstructor() = 
        let content = """   type SonarModule(int, int) = 
                                // the name
                                member val Name : string =  "" with get, set """

        let ast = Helper.getAstByContent("/file.fs", content)

        let loc, nmbclasses, autoprops, functions, complexity, fileComplexityDist, functionComplexityDist = FsSonarRunnerCore.UntypedAstUtils.getCodeMetrics(ast)
        Assert.That(loc.Count, Is.EqualTo(2))
        Assert.That(nmbclasses, Is.EqualTo(1))
        Assert.That(FsSonarRunnerCore.UntypedAstUtils.GetLines(ast), Is.EqualTo(3))

    [<Test>]
    member this.ShouldExtractCorrectNumberOfClassMultipleConstructor() = 
        let content = """   type SonarModule(int, int) = 
                                new(param1) = 
                                    MultipleConstructors(param1,-1) 
                                // the name
                                member val Name : string =  "" with get, set """

        let ast = Helper.getAstByContent("/file.fs", content)

        let loc, nmbclasses, autoprops, functions, complexity, fileComplexityDist, functionComplexityDist = FsSonarRunnerCore.UntypedAstUtils.getCodeMetrics(ast)
        Assert.That(loc.Count, Is.EqualTo(4))
        Assert.That(nmbclasses, Is.EqualTo(1))
        Assert.That(FsSonarRunnerCore.UntypedAstUtils.GetLines(ast), Is.EqualTo(5))


    [<Test>]
    member this.ShouldExtractCorrectNumberOfFunctions() = 
        let content = """   
                            let dmmd = 1
                            let function1 arg = 
                                "sad" """


        let ast = Helper.getAstByContent("/file.fs", content)
               
        let loc, nmbclasses, autoprops, functions, complexity, fileComplexityDist, functionComplexityDist = FsSonarRunnerCore.UntypedAstUtils.getCodeMetrics(ast)
        Assert.That(functions, Is.EqualTo(2))


    [<Test>]
    member this.ShouldExtractCorrectNumberOfFunctionsAsMethods() = 
        let content = """   type SonarModule(a, b) = 
                                member this.SomeMethod(a, b, c) = (a + b + c) * a """

        let ast = Helper.getAstByContent("/file.fs", content)

        let loc, nmbclasses, autoprops, functions, complexity, fileComplexityDist, functionComplexityDist = FsSonarRunnerCore.UntypedAstUtils.getCodeMetrics(ast)
        Assert.That(functions, Is.EqualTo(1))
