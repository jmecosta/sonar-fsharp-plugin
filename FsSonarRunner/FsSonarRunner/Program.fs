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
        try
            let metrics = SQAnalyser()

            arguments.["f"]
            |> Seq.iter (fun input ->
                metrics.RunAnalyses(input, File.ReadAllText(input), "")
            )

            writeMetrics metrics
        with
        | ex -> printf "    Failed: %A" ex
    elif arguments.ContainsKey("i") then
        try
            let metrics = SQAnalyser()

            arguments.["i"]
            |> Seq.iter (fun input ->
                let options = XmlHelper.InputXml.Parse(File.ReadAllText(input))

                let handleFileToAnalyse(file) =
                    if File.Exists(file) then
                        metrics.RunAnalyses(file, File.ReadAllText(file), input)
                    else
                        printf "    [FsSonarRunner] [Error]: %s not found \r\n" file

                options.Files
                |> Seq.iter handleFileToAnalyse
            )

            writeMetrics metrics
        with
        | ex -> printf "    Failed: %A" ex
    elif arguments.ContainsKey("d") then
        try
            let metrics = SQAnalyser()

            arguments.["d"]
            |> Seq.iter (fun directory ->
                let fsfiles = Directory.GetFiles(directory, "*.fs", SearchOption.AllDirectories)
                let fsxfiles = Directory.GetFiles(directory, "*.fsx", SearchOption.AllDirectories)

                Array.append fsfiles fsxfiles
                |> Seq.iter (fun c -> metrics.RunAnalyses(c, File.ReadAllText(c), ""))
            )

            writeMetrics metrics
        with
        | ex -> printf "    Failed: %A" ex
    else
        ShowHelp()

    0
