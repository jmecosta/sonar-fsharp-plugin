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
        let metrics = new SQAnalyser()
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

                let metrics = new SQAnalyser()
                options.Files |> Seq.iter  (fun c -> if File.Exists(c) then metrics.RunAnalyses(c, File.ReadAllText(c), input))

                metrics.WriteXmlToDisk(output, false)
            with
            | ex -> printf "    Failed: %A" ex
        ()
    elif arguments.ContainsKey("d") then
        try
            let directory = arguments.["d"] |> Seq.head

            let fsfiles = Directory.GetFiles(directory, "*.fs", SearchOption.AllDirectories)
            let fsxfiles = Directory.GetFiles(directory, "*.fsx", SearchOption.AllDirectories)

            let metrics = new SQAnalyser()

            fsfiles |> Seq.iter (fun c -> metrics.RunAnalyses(c, File.ReadAllText(c), ""))
            fsxfiles |> Seq.iter (fun c -> metrics.RunAnalyses(c, File.ReadAllText(c), ""))

            metrics.WriteXmlToDisk(argv.[2], true)
        with
        | ex -> printf "    Failed: %A" ex
    else
        ShowHelp()

    0
