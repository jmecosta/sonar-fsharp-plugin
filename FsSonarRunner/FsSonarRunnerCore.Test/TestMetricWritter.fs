namespace FsSonarRunnerCore.Test

open System.IO
open NUnit.Framework
open FsSonarRunnerCore

[<TestFixture>]
type CreatesCorrectXml() =

    [<Test>]
    member this.ShouldCreateValidXml() =
        let content = """   [<AllowNullLiteral>] // does not count as source
                            type SonarModule() =
                                // the name
                                member this.Name(a)  = ""
                                member val ProjectName : string = "" with get, set
                                member val BaseDir : string = "" with get, set
                                member val Sources : string = "" with get, set
                                member val SubModules : SonarModule List = List.Empty with get, set
                                member val ParentModule : SonarModule = null with get, set """

        let metrics = new SQAnalyser()
        metrics.RunAnalyses("/file.fs", content, "")
        let tmpFile = Path.GetTempFileName()
        metrics.WriteXmlToDisk(tmpFile, false)

        let results = Helper.ResXml.Parse(File.ReadAllText(tmpFile))
        Assert.That(results.Files.[0].Path, Is.EqualTo("/file.fs"))
        Assert.That(results.Files.[0].Metrics.LinesOfCodes.Length, Is.EqualTo(7))
        Assert.That(results.Files.[0].Metrics.Classes, Is.EqualTo(1))
        Assert.That(results.Files.[0].Metrics.Accessors, Is.EqualTo(5))
        Assert.That(results.Files.[0].Metrics.Functions, Is.EqualTo(1))
        Assert.That(results.Files.[0].Metrics.Complexity, Is.EqualTo(0))
        Assert.That(results.Files.[0].Metrics.FileComplexityDistribution, Is.EqualTo("0=1;5=0;10=0;20=0;30=0;60=0;90=0"))
        File.Delete(tmpFile)

        ()
