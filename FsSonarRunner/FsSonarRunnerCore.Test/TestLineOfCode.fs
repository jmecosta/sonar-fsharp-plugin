namespace FsSonarRunnerCore.Test

open NUnit.Framework

[<TestFixture>]
type CorrectlyExtracsLineInformation() =

    [<Test>]
    member this.ShouldCountOpens() =
        let content = """ open System.IO """
        let ast = Helper.getAstByContent("/file.fs", content)
        let loc, classnmb, autoprops, functions, complexity, fileComplexityDist, functionComplexityDist = FsSonarRunnerCore.UntypedAstUtils.getCodeMetrics(ast)
        Assert.That(loc.Count, Is.EqualTo(1))
        Assert.That(classnmb, Is.EqualTo(0))
        Assert.That(FsSonarRunnerCore.UntypedAstUtils.GetLines(ast), Is.EqualTo(1))

    [<Test>]
    member this.ShouldCountExceptions() =
        let content = """ exception MyError of string """
        let ast = Helper.getAstByContent("/file.fs", content)

        let loc, nmbclasses, autoprops, functions, complexity, fileComplexityDist, functionComplexityDist = FsSonarRunnerCore.UntypedAstUtils.getCodeMetrics(ast)
        Assert.That(loc.Count, Is.EqualTo(1))
        Assert.That(nmbclasses, Is.EqualTo(0))

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
    member this.ShouldHandleTypesWithMembers() =
        let content = """   [<AllowNullLiteral>] // does not count as source
                            type SonarModule() =
                                // the name
                                member val Name : string =  "" with get, set
                                member val ProjectName : string = "" with get, set
                                member val BaseDir : string = "" with get, set
                                member val Sources : string = "" with get, set
                                member val SubModules : SonarModule List = List.Empty with get, set
                                member val ParentModule : SonarModule = null with get, set """

        let ast = Helper.getAstByContent("/file.fs", content)

        let loc, nmbclasses, autoprops, functions, complexity, fileComplexityDist, functionComplexityDist = FsSonarRunnerCore.UntypedAstUtils.getCodeMetrics(ast)
        Assert.That(loc.Count, Is.EqualTo(7))
        Assert.That(nmbclasses, Is.EqualTo(1))
        Assert.That(FsSonarRunnerCore.UntypedAstUtils.GetLines(ast), Is.EqualTo(9))

    [<Test>]
    member this.ShouldHandleDoExpression() =
        let content = """     if ((do ());

                                // comment
                                true) then () """

        let ast = Helper.getAstByContent("/file.fs", content)

        let loc, nmbclasses, autoprops, functions, complexity, fileComplexityDist, functionComplexityDist = FsSonarRunnerCore.UntypedAstUtils.getCodeMetrics(ast)
        Assert.That(loc.Count, Is.EqualTo(2))
        Assert.That(nmbclasses, Is.EqualTo(0))
        Assert.That(FsSonarRunnerCore.UntypedAstUtils.GetLines(ast), Is.EqualTo(4))

    [<Test>]
    member this.ShouldCountParseDirectives() =
        let content = """ #if XXX
                          open System.IO
                          #else
                          open System
                          #endif """
        let ast = Helper.getAstByContent("/file.fs", content)
        let loc, nmbclasses, autoprops, functions, complexity, fileComplexityDist, functionComplexityDist = FsSonarRunnerCore.UntypedAstUtils.getCodeMetrics(ast)
        Assert.That(loc.Count, Is.EqualTo(1))
        Assert.That(nmbclasses, Is.EqualTo(0))
        Assert.That(FsSonarRunnerCore.UntypedAstUtils.GetLines(ast), Is.EqualTo(1))

    [<Test>]
    member this.ShouldCountAllLinesInBigFile() =
        let content = """ namespace FsSonarRunnerCore

open FSharp.Compiler.Ast
open FSharp.Compiler.SourceCodeServices
open System.Text
open System.Xml
open System.Xml.Linq

type SonarResoureMetrics(path : string) =
    member val GetResourcePath : string = path with get
    member val GetLines : int = -1 with get, set
    member val GetLinesOfCode : Set<int> = Set.empty with get, set

type Metrics() =

    let mutable resources : SonarResoureMetrics List = List.Empty
    let resourcesLocker = new System.Object()

    member this.GatherMetrics(path : string, input : string) =
        let parseTree =
            let checker = FSharpChecker.Create()

            // Get compiler options for the 'project' implied by a single script file
            let projOptions =
                checker.GetProjectOptionsFromScript(path, input)
                |> Async.RunSynchronously

            // Run the first phase (untyped parsing) of the compiler
            let parseFileResults =
                checker.ParseFileInProject(path, input, projOptions)
                |> Async.RunSynchronously

            parseFileResults.ParseTree

        let resourceMetric = new SonarResoureMetrics(path)

        // generate lines of code
        resourceMetric.GetLinesOfCode <- FsSonarRunnerCore.UntypedAstUtils.getCodeLineRanges(parseTree)
        // generate lines
        resourceMetric.GetLines <- FsSonarRunnerCore.UntypedAstUtils.GetLines(parseTree)


        // ensure we are able to run multiple parsers at same time
        lock resourcesLocker (fun () -> resources <- resources @ [resourceMetric] )

    member this.WriteXmlToDisk(xmlOut : string) =
        let xmlOutSettings = new XmlWriterSettings(Encoding = Encoding.UTF8, Indent = true, IndentChars = "  ")
        use xmlOut = XmlWriter.Create(xmlOut, xmlOutSettings)
        xmlOut.WriteStartElement("AnalysisOutput") // 1
        xmlOut.WriteStartElement("Files") // 2

        for resource in resources do
            xmlOut.WriteStartElement("File") // 3
            xmlOut.WriteElementString("Path", resource.GetResourcePath)

            xmlOut.WriteStartElement("Metrics") // 4
            xmlOut.WriteElementString("Lines", sprintf "%i" resource.GetLines)

            xmlOut.WriteStartElement("LinesOfCode") // 5
            for line in resource.GetLinesOfCode do
                xmlOut.WriteElementString("Line", sprintf "%i" line)
            xmlOut.WriteEndElement() // 5


            // close element metrics
            xmlOut.WriteEndElement() // 4

            xmlOut.WriteEndElement() // 3



        xmlOut.WriteEndElement() // 2
        xmlOut.WriteEndElement() // 1

        xmlOut.WriteEndDocument()
        xmlOut.Flush()

  """
        let ast = Helper.getAstByContent("/file.fs", content)
        Assert.That(FsSonarRunnerCore.UntypedAstUtils.GetLines(ast), Is.EqualTo(76))
        let loc, nmbclasses, autoprops, functions, complexity, fileComplexityDist, functionComplexityDist = FsSonarRunnerCore.UntypedAstUtils.getCodeMetrics(ast)
        Assert.That(loc.Count, Is.EqualTo(46))
        Assert.That(nmbclasses, Is.EqualTo(2))
        Assert.That(autoprops, Is.EqualTo(3))
