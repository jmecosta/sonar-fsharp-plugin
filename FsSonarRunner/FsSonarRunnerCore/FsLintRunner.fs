namespace FsSonarRunnerCore

open FSharpLint.Framework.Ast
open FSharpLint.Application
open FSharpLint.Framework
open System
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
        let resourceManager =
            // see FSharpLint.Framework.Resources how to get the resource manager
            let assembly = Assembly.Load("FSharpLint.Core")

            let resourceName = assembly.GetManifestResourceNames()
                               |> Seq.find (fun n -> n.EndsWith("Text.resources", StringComparison.Ordinal))
            ResourceManager(resourceName.Replace(".resources", String.Empty), assembly)

        let set = resourceManager.GetResourceSet(CultureInfo.CurrentUICulture, true, true)
        let mutable rules = List.Empty

        let createProfileRule(lem:DictionaryEntry) =
            try
                if (lem.Key :?> string).StartsWith("Rules") ||
                   (lem.Key :?> string).Equals("LintError")  ||
                   (lem.Key :?> string).Equals("LintSourceError") then
                    let rule = FsLintRule(lem.Key :?> string, lem.Value :?> string)
                    rules <- rules @ [rule]
            with
            | _ -> ()

        for setEntry in set do
            createProfileRule(setEntry :?> DictionaryEntry)

        rules

    member this.GetRule(txt : string) =

        let verifyIfExists(rule : FsLintRule, txtdata : string) =
            let pattern = rule.Text
                                .Replace("{0}", "[a-zA-Z0-9]{1,}")
                                .Replace("{1}", "[a-zA-Z0-9]{1,}")
                                .Replace("{2}", "[a-zA-Z0-9]{1,}")
                                .Replace("{3}", "[a-zA-Z0-9]{1,}")
                                .Replace("{4}", "[a-zA-Z0-9]{1,}")

            Regex.Match(txtdata, pattern).Success

        let foundItem = fsLintProfile |> Seq.tryFind (fun c -> verifyIfExists(c, txt))
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

type FsLintRunner(filePath : string, rules : SonarRules, configuration : Configuration.Configuration) =
    let mutable notsupportedlines = List.Empty
    let mutable issues = List.empty

    let getErrorMessage (range:FSharp.Compiler.Range.range) =
        let error = Resources.GetString("LintSourceError")
        String.Format(error, range.StartLine, range.StartColumn)

    let highlightErrorText (range:FSharp.Compiler.Range.range) (errorLine:string) =
        let highlightColumnLine =
            if String.length errorLine = 0 then "^"
            else
                errorLine
                |> Seq.mapi (fun i _ -> if i = range.StartColumn then "^" else " ")
                |> Seq.reduce (+)
        errorLine + Environment.NewLine + highlightColumnLine

    let reportWarningLine (warning:Suggestion.LintWarning) =
        let highlightedErrorText = highlightErrorText warning.Details.Range (getErrorMessage warning.Details.Range)
        let str = warning.Details.Message + Environment.NewLine + highlightedErrorText + Environment.NewLine + warning.ErrorText
        let rule = rules.GetRule(warning.Details.Message)
        if not (isNull rule) then
            let issue = SonarIssue(Rule = rule.Rule, Line = warning.Details.Range.StartLine, Component = filePath, Message = warning.Details.Message)
            issues  <- issues @ [issue]
        else
            notsupportedlines <- notsupportedlines @ [str]

    let runLintOnFile lintParams pathToFile =
        lintFile lintParams pathToFile

    let outputLintResult (pathToFile: string) =
        printf "%s: " (pathToFile.Trim())
        function
        | LintResult.Success(_) -> printfn "Lint Ok"
        | LintResult.Failure(error) -> printfn "Lint Nok %s" (error.ToString())

    member this.ExecuteAnalysis() =
        let lintParams =
            { CancellationToken = None
              ReceivedWarning = Some reportWarningLine
              Configuration = Configuration configuration
              ReportLinterProgress = None
              ReleaseConfiguration = None }

        issues <- List.Empty
        if File.Exists(filePath) then
            runLintOnFile lintParams filePath |> outputLintResult filePath

        issues
