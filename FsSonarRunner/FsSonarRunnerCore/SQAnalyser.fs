namespace FsSonarRunnerCore

open Microsoft.FSharp.Compiler.Ast
open Microsoft.FSharp.Compiler.SourceCodeServices
open System.Text
open System.IO
open System.Xml
open System.Xml.Linq

type SonarResoureMetrics(path : string) = 
    member val ResourcePath : string = path with get
    member val Lines : int = -1 with get, set
    member val Classes : int = -1 with get, set
    member val LinesOfCode : Set<int> = Set.empty with get, set
    member val Accessors : int = -1 with get, set
    member val Functions : int = -1 with get, set
    member val Complexity : int = -1 with get, set
    member val FileComplexityDist : string = "" with get, set
    member val FunctionComplexityDist : string = "" with get, set
    member val Issues : SonarIssue List = List.empty with get, set
    member val CopyPastTokens : UntypedAstUtils.TokenData List = List.empty with get, set

type SQAnalyser() =
            
    let mutable resources : SonarResoureMetrics List = List.Empty
    let resourcesLocker = new System.Object()

    let GatherMetrics(path : string, input : string, resourceMetric : SonarResoureMetrics) =
        let parseTree = 
            let checker = FSharpChecker.Create()

            // Get compiler options for the 'project' implied by a single script file
            let (projOptions, _diagnostics) = 
                checker.GetProjectOptionsFromScript(path, input)
                |> Async.RunSynchronously

            // Run the first phase (untyped parsing) of the compiler
            let parseFileResults = 
                checker.ParseFileInProject(path, input, projOptions) 
                |> Async.RunSynchronously
            
            parseFileResults.ParseTree
       
        // generate metrics
        let loc, classnmb, autoProperties, functions, complexity, fileComplexityDist, functionComplexityDist
            = FsSonarRunnerCore.UntypedAstUtils.getCodeMetrics(parseTree)

        resourceMetric.LinesOfCode <- loc
        resourceMetric.Classes <- classnmb
        resourceMetric.Accessors <- autoProperties
        resourceMetric.Functions <- functions
        resourceMetric.Complexity <- complexity
        resourceMetric.FileComplexityDist <- fileComplexityDist
        resourceMetric.FunctionComplexityDist <- functionComplexityDist

        // generate lines
        resourceMetric.Lines <- FsSonarRunnerCore.UntypedAstUtils.GetLines(parseTree)

        // generate  copy past tokes
        let array = [|"\r\n"; "\n"|]
        let lines = input.Split(array, System.StringSplitOptions.None)
        resourceMetric.CopyPastTokens <- FsSonarRunnerCore.UntypedAstUtils.getDumpToken(lines)


    member this.GatherMetricsForResource(path : string, input : string) =
        let resourceMetric = new SonarResoureMetrics(path)
        GatherMetrics(path, input, resourceMetric)
        resourceMetric

    member this.RunAnalyses(path : string, input : string, inputXmlConfig : string) =  
        // gather metrics
        let resourceMetric = this.GatherMetricsForResource(path, input)
        // gather issues
        resourceMetric.Issues <- this.RunLint(path, input, inputXmlConfig)
        // ensure we are able to run multiple parsers at same time
        lock resourcesLocker (fun () -> resources <- resources @ [resourceMetric] )

    member this.RunLint(path : string, input : string, inputXmlConfig : string) = 
        // run lint
        try            
            let lintRunner = new FsLintRunner(path, new SonarRules(), InputConfigHandler.CreateALintConfiguration(inputXmlConfig))
            lintRunner.ExecuteAnalysis()
        with
        | ex -> printf "Lint Execution Failed %A" ex
                List.Empty

    member this.PrintIssues() = 
        for resource in resources do
            resource.Issues |> Seq.iter (fun diagnostic -> 
                    printf "%s : %s : %i : %s\r\n" resource.ResourcePath  diagnostic.Rule diagnostic.Line diagnostic.Message
                            )

    member this.WriteXmlToDisk(xmlOutPath : string) = 
        printf "Write ouput xml to file %s\r\n" xmlOutPath
        let xmlOutSettings = new XmlWriterSettings(Encoding = Encoding.UTF8, Indent = true, IndentChars = "  ")
        use xmlOut = XmlWriter.Create(xmlOutPath, xmlOutSettings)
        xmlOut.WriteStartElement("AnalysisOutput") // 1
        xmlOut.WriteStartElement("Files") // 2

        for resource in resources do
            xmlOut.WriteStartElement("File") // 3
            xmlOut.WriteElementString("Path", resource.ResourcePath)

            xmlOut.WriteStartElement("Metrics") // 4 
            xmlOut.WriteElementString("Classes", sprintf "%i" resource.Classes)
            xmlOut.WriteElementString("Accessors", sprintf "%i" resource.Accessors)
            xmlOut.WriteElementString("Functions", sprintf "%i" resource.Functions)
            xmlOut.WriteElementString("Complexity", sprintf "%i" resource.Complexity)
            xmlOut.WriteElementString("FileComplexityDistribution", sprintf "%s" resource.FileComplexityDist)
            xmlOut.WriteElementString("FunctionComplexityDistribution", sprintf "%s" resource.FunctionComplexityDist)

            xmlOut.WriteStartElement("LinesOfCode") // 5
            for line in resource.LinesOfCode do
                xmlOut.WriteElementString("Line", sprintf "%i" line)
                
            xmlOut.WriteEndElement() // 5

            // close element metrics
            xmlOut.WriteEndElement() // 4

            xmlOut.WriteStartElement("Issues") // 6 

            resource.Issues |> Seq.iter (fun diagnostic -> 
                xmlOut.WriteStartElement("Issue")
                xmlOut.WriteElementString("Id", sprintf "%s" diagnostic.Rule)
                xmlOut.WriteElementString("Line", sprintf "%i" diagnostic.Line)
                xmlOut.WriteElementString("Message", sprintf "%s" diagnostic.Message)
                xmlOut.WriteEndElement()
                )
                
            xmlOut.WriteEndElement()    // 6
            
            xmlOut.WriteStartElement("CopyPasteTokens");
            for token in resource.CopyPastTokens do
                xmlOut.WriteStartElement("Token");
                xmlOut.WriteElementString("Value", System.Convert.ToBase64String(System.Text.Encoding.UTF8.GetBytes(token.Content)))
                xmlOut.WriteElementString("Line", token.Line.ToString())
                xmlOut.WriteEndElement();

            xmlOut.WriteEndElement()
                                             
            xmlOut.WriteEndElement() // 3

        xmlOut.WriteEndElement() // 2
        xmlOut.WriteEndElement() // 1

        xmlOut.WriteEndDocument()
        xmlOut.Flush()
