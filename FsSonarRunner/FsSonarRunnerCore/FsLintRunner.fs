namespace FsSonarRunnerCore

open System
open FSharpLint.Framework
open FSharpLint.Framework.Ast
open FSharpLint.Framework.Configuration
open FSharpLint.Application
open System.Resources
open System.Reflection
open System.Globalization
open System.Collections
open System.IO

[<AllowNullLiteralAttribute>]
type FsLintRule(name : string, value : string) =
    member val Rule : string = name with get
    member val Text : string = value with get

type SonarRules() = 

    let fsLintProfile = 
        let resourceManager = new ResourceManager("Text" ,Assembly.Load("FSharpLint.Framework"))
        let set = resourceManager.GetResourceSet(CultureInfo.CurrentUICulture, true, true)
        let mutable rules = List.Empty
        
        for resoure in set do
            let lem = resoure :?> DictionaryEntry
            try
                if (lem.Key :?> string).StartsWith("Rules") ||
                   (lem.Key :?> string).Equals("LintError")  ||
                   (lem.Key :?> string).Equals("LintSourceError") then
                    let rule = new FsLintRule(lem.Key :?> string, lem.Value :?> string)
                    rules <- rules @ [rule]
            with
            | _ -> ()
        rules

    member this.GetRule(txt : string) =
        
        let VerifyIfExists(rule : FsLintRule, txtdata : string) = 
            if rule.Text.StartsWith("{") then
                if rule.Text.Contains("can be refactored") && txt.Contains("can be refactored into") then
                    true
                elif rule.Text.Contains("s should be less than") && txt.Contains("s should be less than") then
                    true
                else
                    false
            else
                let start = rule.Text.Split('{').[0]
                if txt.StartsWith(start) then
                    true
                else
                    false


        let foundItem = fsLintProfile |> Seq.tryFind (fun c -> VerifyIfExists(c, txt))
        match foundItem with
        | Some v -> v
        | _ -> null

    member this.ShowRules() =
        fsLintProfile |> Seq.iter (fun c -> printf "%s  = %s\r\n" c.Rule c.Text)
        printf "\r\n Total Rules: %i\r\n" fsLintProfile.Length

type SonarIssue() =
    member val Rule : string = "" with get, set
    member val Line : int = 0 with get, set
    member val Component : string = "" with get, set
    member val Message : string = "" with get, set

type private Argument =
    | ProjectFile of string
    | SingleFile of string
    | Source of string
    | UnexpectedArgument of string

type FsLintRunner(filePath : string, rules : SonarRules, configuration : FSharpLint.Framework.Configuration.Configuration) =
    let mutable notsupportedlines = List.Empty
    let mutable issues = List.empty

    let reportLintWarning (warning:LintWarning.Warning) = 
        let output = warning.Info + System.Environment.NewLine + LintWarning.getWarningWithLocation warning.Range warning.Input
        let rule = rules.GetRule(warning.Info)
        if rule <> null then
            let issue = new SonarIssue(Rule = rule.Rule, Line = warning.Range.StartLine, Component = filePath, Message = warning.Info)
            issues  <- issues @ [issue]  
        else
            notsupportedlines <- notsupportedlines @ [output]

    let runLintOnFile pathToFile =
        let parseInfo =
            {
                FinishEarly = None
                ReceivedWarning = Some reportLintWarning
                Configuration = Some configuration
            }

        lintFile parseInfo pathToFile
                    
    let outputLintResult = function
        | LintResult.Success(_) -> Console.WriteLine("Lint Ok")
        | LintResult.Failure(error) -> Console.WriteLine("Lint Nok" + error.ToString())
        
    member this.ExecuteAnalysis() =
        issues <- List.Empty
        if File.Exists(filePath) then
            runLintOnFile filePath (Version(4, 0)) |> outputLintResult

        issues
