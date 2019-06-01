module ConfHelper

open System

open FParsec
open FSharpLint.Framework.HintParser
open FSharpLint.Framework.Configuration

open FSharp.Data

type InputConfigution = XmlProvider<"""
<AnalysisInput>
  <Settings>
    <Setting>
      <Key>sonar.cs.ignoreHeaderComments</Key>
      <Value>true</Value>
    </Setting>
  </Settings>
  <Rules>
    <Rule>
      <Key>FileLoc</Key>
      <Parameters>
        <Parameter>
          <Key>maximumFileLocThreshold</Key>
          <Value>1500</Value>
        </Parameter>
        <Parameter>
          <Key>maximumFileLocThreshold</Key>
          <Value>sdfsdfs</Value>
        </Parameter>
      </Parameters>
    </Rule>
    <Rule>
      <Key>SwitchWithoutDefault</Key>
    </Rule>
    <Rule>
      <Key>LineLength</Key>
      <Parameters>
        <Parameter>
          <Key>djhda</Key>
          <Value>true</Value>
        </Parameter>
      </Parameters>
    </Rule>
  </Rules>
  <Files>
    <File>E:\file.fs</File>
    <File>E:\file2.fs</File>
  </Files>
</AnalysisInput>
""">

let GetEnaFlagForParam(config : InputConfigution.AnalysisInput, ruleId : string, paramName : string) =
    try
        let rule = config.Rules |> Seq.find (fun c -> c.Key.Equals(ruleId))
        let enabledis = rule.Parameters.Value.Parameters |> Seq.find (fun c -> c.Key.Equals(paramName))
        Enabled(enabledis.Value.Number.Value <> 0)
    with
    | _ -> Enabled(false)

let GetEnaFlagForRule(config : InputConfigution.AnalysisInput, ruleId : string) =
    try
        config.Rules |> Seq.find (fun c -> c.Key.Equals(ruleId)) |> ignore
        Enabled(true)
    with
    | _ -> Enabled(false)

let GetValueForInt(config : InputConfigution.AnalysisInput, ruleId : string, paramName : string, defaultValue : int) =
    try
        let rule = config.Rules |> Seq.find (fun c -> c.Key.Equals(ruleId))
        let enabledis = rule.Parameters.Value.Parameters |> Seq.find (fun c -> c.Key.Equals(paramName))
        enabledis.Value.Number.Value
    with
    | _ -> defaultValue

let GetValueForStringOrStringList(config : InputConfigution.AnalysisInput, ruleId : string, paramName : string) =
    let rule = config.Rules |> Seq.find (fun c -> c.Key.Equals(ruleId))
    let enabledis = rule.Parameters.Value.Parameters |> Seq.find (fun c -> c.Key.Equals(paramName))
    enabledis.Value.String.Value

let GetValueForStringList(config : InputConfigution.AnalysisInput, ruleId : string, paramName : string, defaultValue : string List) =
    try
        let stringList = GetValueForStringOrStringList(config, ruleId, paramName)
        stringList.Split(';') |> Array.toList
    with
    | _ -> defaultValue

let GetValueForString(config : InputConfigution.AnalysisInput, ruleId : string, paramName : string, defaultValue : string) =
    try
        GetValueForStringOrStringList(config, ruleId, paramName)
    with
    | _ -> defaultValue

let GetValueForEnum(config : InputConfigution.AnalysisInput, ruleId : string, paramName : string, defaultValue : string, enumType : 'T) =
    try
        let rule = config.Rules |> Seq.find (fun c -> c.Key.Equals(ruleId))
        let param = rule.Parameters.Value.Parameters |> Seq.find (fun c -> c.Key.Equals(paramName))
        Enum.Parse(enumType, param.Value.String.Value) :?> 'T
    with
    | _ -> Enum.Parse(enumType, defaultValue) :?> 'T

let parseHints hints =
    let parseHint hint =
        match CharParsers.run phint hint with
        | FParsec.CharParsers.Success(hint, _, _) -> hint
        | FParsec.CharParsers.Failure(error, _, _) -> failwithf "Invalid hint %s" error

    let hintsData = List.map (fun x -> { Hint = x; ParsedHint = parseHint x }) hints

    { Hints = hintsData; Update = Update.Overwrite }

let GetValueForBool(config : InputConfigution.AnalysisInput, ruleId : string, paramName : string, defaultValue : bool) =
    try
        let rule = config.Rules |> Seq.find (fun c -> c.Key.Equals(ruleId))
        let enabledis = rule.Parameters.Value.Parameters |> Seq.find (fun c -> c.Key.Equals(paramName))
        enabledis.Value.Boolean.Value
    with
    | _ -> defaultValue
