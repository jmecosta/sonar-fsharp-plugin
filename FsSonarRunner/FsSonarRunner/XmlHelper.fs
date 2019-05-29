module XmlHelper

open System.Text.RegularExpressions
open FSharp.Data

type InputXml = XmlProvider<"""
<AnalysisInput>
  <Settings>
    <Setting>
      <Key>sonar.cs.ignoreHeaderComments</Key>
      <Value>sasdas</Value>
    </Setting>
    <Setting>
      <Key>sonar.cs.ignoreHeaderComments</Key>
      <Value>dsfsd</Value>
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
          <Value>1500</Value>
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
          <Key>maximumLineLength</Key>
          <Value>200</Value>
        </Parameter>
      </Parameters>
    </Rule>
  </Rules>
  <Files>
    <File>asdfsad\WorkspaceFeature.cs</File>
    <File>afdasfas\WorkspaceView.xaml.cs</File>
    <File>asfdsa\WorkspaceViewModel.cs</File>
  </Files>
</AnalysisInput>
""">

// parse command using regex
// if matched, return (command name, command value) as a tuple
let (|Command|_|) (s:string) =
    let r = new Regex(@"^(?:-{1,2}|\/)(?<command>\w+)[=:]*(?<value>.*)$",RegexOptions.IgnoreCase)
    let m = r.Match(s)
    if m.Success then
        Some(m.Groups.["command"].Value.ToLower(), m.Groups.["value"].Value)
    else
        None

// take a sequence of argument values
// map them into a (name,value) tuple
// scan the tuple sequence and put command name into all subsequent tuples without name
// discard the initial ("","") tuple
// group tuples by name
// convert the tuple sequence into a map of (name,value seq)
let parseArgs (args:string seq) =
    args
    |> Seq.map (fun i ->
                        match i with
                        | Command (n,v) -> (n,v) // command
                        | _ -> ("",i)            // data
                       )
    |> Seq.scan (fun (sn,_) (n,v) -> if n.Length>0 then (n,v) else (sn,v)) ("","")
    |> Seq.skip 1
    |> Seq.groupBy (fun (n,_) -> n)
    |> Seq.map (fun (n,s) -> (n, s |> Seq.map (fun (_,v) -> v) |> Seq.filter (fun i -> i.Length>0)))
    |> Map.ofSeq