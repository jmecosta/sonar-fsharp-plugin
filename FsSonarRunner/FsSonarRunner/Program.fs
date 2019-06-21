// Learn more about F# at http://fsharp.org
// See the 'F# Tutorial' project for more help.
open System
open System.IO
open FsSonarRunnerCore

let ShowHelp () =
        Console.WriteLine ("Usage: FsSonarRunner [OPTIONS]")
        Console.WriteLine ("Collects results for Sonar Analsyis using FSharpLint")
        Console.WriteLine ()
        Console.WriteLine ("Options:")
        Console.WriteLine ("    /I|/i:<input xml>")
        Console.WriteLine ("    /F|/f:<analyse single file>")
        Console.WriteLine ("    /O|/o:<output xml file>")
        Console.WriteLine ("    /D|/d:<directory to analyse>")
        Console.WriteLine ("    /displayrules")

[<EntryPoint>]
let main argv =
    printfn "%A" argv
    let arguments = XmlHelper.parseArgs(argv)

    if arguments.ContainsKey("h") then
        ShowHelp()
    if arguments.ContainsKey("displayrules") then
        let rules = new SonarRules()
        rules.ShowRules()
    elif arguments.ContainsKey("f") then
        let input = arguments.["f"] |> Seq.head
        let metrics = SQAnalyser()
        try
            metrics.RunAnalyses(input, File.ReadAllText(input), "")
            metrics.PrintIssues()
        with
        | ex -> printf "    Failed: %A" ex

        // todo print to output
    elif arguments.ContainsKey("i") then
        if not(arguments.ContainsKey("o")) then
            Console.WriteLine ("    Mission /O")
            ShowHelp()
        else
            try
                let input = arguments.["i"] |> Seq.head
                let output = arguments.["o"] |> Seq.head
                let options = XmlHelper.InputXml.Parse(File.ReadAllText(input))

                let metrics = SQAnalyser()
                let HandleFileToAnalyse(file) = 
                    if File.Exists(file) then
                        metrics.RunAnalyses(file, File.ReadAllText(file), input)
                    else
                        printf "    [FsSonarRunner] [Error]: %s not found \r\n" file

                options.Files
                |> Seq.iter (fun file -> HandleFileToAnalyse(file.Replace("file:///", "")))
                metrics.WriteXmlToDisk(output, false)
            with
            | ex -> printf "    Failed: %A" ex
        ()
    elif arguments.ContainsKey("d") then
        try
            let directory = arguments.["d"] |> Seq.head

            let fsfiles = Directory.GetFiles(directory, "*.fs", SearchOption.AllDirectories)
            let fsxfiles = Directory.GetFiles(directory, "*.fsx", SearchOption.AllDirectories)

            let metrics = SQAnalyser()

            fsfiles |> Seq.iter (fun c -> metrics.RunAnalyses(c, File.ReadAllText(c), ""))
            fsxfiles |> Seq.iter (fun c -> metrics.RunAnalyses(c, File.ReadAllText(c), ""))

            if arguments.ContainsKey("o")
            then
                let xmlOutPath = arguments.["o"] |> Seq.head
                metrics.WriteXmlToDisk(xmlOutPath, true)
            metrics.PrintIssues()
        with
        | ex -> printf "    Failed: %A" ex
    else
        ShowHelp()

    0
