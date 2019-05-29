namespace FsSonarRunnerCore

open System
open FSharpLint.Framework.Ast
open FSharpLint.Application
open System.Resources
open System.Reflection
open System.Globalization
open System.Collections
open System.IO

open System.Text.RegularExpressions

[<AllowNullLiteralAttribute>]
type FsLintRule(name : string, value : string) =
    member val Rule : string = name with get
    member val Text : string = value with get

type SonarRules() =

    let fsLintProfile =
        let resourceManager = new ResourceManager("Text" ,Assembly.Load("FSharpLint.Core"))
        let set = resourceManager.GetResourceSet(CultureInfo.CurrentUICulture, true, true)
        let mutable rules = List.Empty

        let CreateProfileRule(lem:DictionaryEntry) =
            try
                if (lem.Key :?> string).StartsWith("Rules") ||
                   (lem.Key :?> string).Equals("LintError")  ||
                   (lem.Key :?> string).Equals("LintSourceError") then
                    let rule = new FsLintRule(lem.Key :?> string, lem.Value :?> string)
                    rules <- rules @ [rule]
            with
            | _ -> ()

        let ddd = set |> Seq.cast<ResourceSet>

        for setentry in set do
            CreateProfileRule(setentry :?> DictionaryEntry)

        rules

    member this.GetRule(txt : string) =

        let VerifyIfExists(rule : FsLintRule, txtdata : string) =
            let pattern = rule.Text
                                .Replace("{0}", "[a-zA-Z0-9]{1,}")
                                .Replace("{1}", "[a-zA-Z0-9]{1,}")
                                .Replace("{2}", "[a-zA-Z0-9]{1,}")
                                .Replace("{3}", "[a-zA-Z0-9]{1,}")
                                .Replace("{4}", "[a-zA-Z0-9]{1,}")

            Regex.Match(txtdata, pattern).Success

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
                CancellationToken = None
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
