open System.IO
open FsSonarRunnerCore
open System.Reflection
open System.Diagnostics

let ProductVersion() =
    let versionInfo = Assembly.GetExecutingAssembly().Location
                    |> FileVersionInfo.GetVersionInfo
    versionInfo.ProductVersion

let ShowHelp() =
    let indent = "    "
    let optionsList = [
        "/I|/i:<input xml>";
        "/F|/f:<analyse single file>";
        "/O|/o:<output xml file>";
        "/D|/d:<directory to analyse>";
        "/displayrules";
        "/version" ]

    printfn "Syntax: FsSonarRunner [OPTIONS]"
    printfn "Collects results for Sonar Analsyis using FSharpLint"
    printfn ""
    printfn "Options:"
    optionsList |> List.iter (fun s -> printfn "%s%s" indent s)

[<EntryPoint>]
let main argv =
    printfn "%A" argv
    let arguments = XmlHelper.parseArgs(argv)

    let output =
        if arguments.ContainsKey("o") then
            arguments.["o"] |> Seq.head |> Some
        else None

    let writeMetrics (metrics : SQAnalyser) =
        match output with
        | Some xmlOutPath -> metrics.WriteXmlToDisk(xmlOutPath, false)
        | None -> metrics.PrintIssues()

    if arguments.ContainsKey("h") then
        ShowHelp()
    elif arguments.ContainsKey("displayrules") then
        let rules = SonarRules()
        rules.ShowRules()
    elif arguments.ContainsKey("version") then
        printfn "%s" (ProductVersion())
    elif arguments.ContainsKey("f") then
        let input = arguments.["f"] |> Seq.head
        try
            let metrics = SQAnalyser()
            metrics.RunAnalyses(input, File.ReadAllText(input), "")
            writeMetrics metrics
        with
        | ex -> printf "    Failed: %A" ex
    elif arguments.ContainsKey("i") then
        try
            let input = arguments.["i"] |> Seq.head
            let options = XmlHelper.InputXml.Parse(File.ReadAllText(input))

            let metrics = SQAnalyser()
            let handleFileToAnalyse(file) =
                if File.Exists(file) then
                    metrics.RunAnalyses(file, File.ReadAllText(file), input)
                else
                    printf "    [FsSonarRunner] [Error]: %s not found \r\n" file

            options.Files
            |> Seq.iter (fun file -> handleFileToAnalyse(file.Replace("file:///", "")))

            writeMetrics metrics
        with
        | ex -> printf "    Failed: %A" ex
    elif arguments.ContainsKey("d") then
        try
            let directory = arguments.["d"] |> Seq.head
            let fsfiles = Directory.GetFiles(directory, "*.fs", SearchOption.AllDirectories)
            let fsxfiles = Directory.GetFiles(directory, "*.fsx", SearchOption.AllDirectories)

            let metrics = SQAnalyser()

            Array.append fsfiles fsxfiles
            |> Seq.iter (fun c -> metrics.RunAnalyses(c, File.ReadAllText(c), ""))

            writeMetrics metrics
        with
        | ex -> printf "    Failed: %A" ex
    else
        ShowHelp()

    0
