module Helper

open System.IO
open FSharp.Compiler.SourceCodeServices
open FSharp.Data

let getAstByContent(file : string, input : string) =
    let checker = FSharpChecker.Create()

    // Get compiler options for the 'project' implied by a single script file
    let (projOptions, _diagnostics) =
        checker.GetProjectOptionsFromScript(file, input)
        |> Async.RunSynchronously

    // Run the first phase (untyped parsing) of the compiler
    let parseFileResults =
        checker.ParseFileInProject(file, input, projOptions)
        |> Async.RunSynchronously

    parseFileResults.ParseTree

let getAstByFile(file : string) =
    let input = File.ReadAllText(file)

    getAstByContent(file, input)

type ResXml = XmlProvider<"""
<!--This XML format is not an API-->
<AnalysisOutput>
  <Files>
    <File>
      <Path>E:\prod\trimble-connect-desktop\Source\SharedAssemblyInfo.cs</Path>
      <Metrics>
        <Lines>24</Lines>
        <Classes>5</Classes>
        <Accessors>0</Accessors>
        <Statements>0</Statements>
        <Functions>0</Functions>
        <PublicApi>0</PublicApi>
        <PublicUndocumentedApi>0</PublicUndocumentedApi>
        <Complexity>0</Complexity>
        <FileComplexityDistribution>0=1;5=0;10=0;20=0;30=0;60=0;90=0</FileComplexityDistribution>
        <FunctionComplexityDistribution>1=0;2=0;4=0;6=0;8=0;10=0;12=0</FunctionComplexityDistribution>
        <Comments>
          <NoSonar />
          <NonBlank>
            <Line>4</Line>
            <Line>8</Line>
            <Line>12</Line>
            <Line>19</Line>
            <Line>22</Line>
          </NonBlank>
        </Comments>
        <LinesOfCode>
          <Line>1</Line>
          <Line>2</Line>
          <Line>5</Line>
          <Line>6</Line>
          <Line>9</Line>
          <Line>10</Line>
          <Line>16</Line>
          <Line>20</Line>
          <Line>23</Line>
        </LinesOfCode>
      </Metrics>
      <Issues>
        <Issue>
          <Id>LineLength</Id>
          <Line>47</Line>
          <Message>Split this 213 characters long line (which is greater than 200 authorized).</Message>
        </Issue>
        <Issue>
          <Id>LineLength</Id>
          <Line>47</Line>
          <Message>Split this 213 characters long line (which is greater than 200 authorized).</Message>
        </Issue>
      </Issues>
      <CopyPasteTokens>
        <Token>
          <Value>QXNzZW1ibHlDdWx0dXJl</Value>
          <Line>23</Line>
        </Token>
        <Token>
          <Value>KA==</Value>
          <Line>23</Line>
        </Token>
        <Token>
          <Value>IiI=</Value>
          <Line>23</Line>
        </Token>
        <Token>
          <Value>KQ==</Value>
          <Line>23</Line>
        </Token>
        <Token>
          <Value>XQ==</Value>
          <Line>23</Line>
        </Token>
      </CopyPasteTokens>
    </File>
    <File>
      <Path>E:\prod\trimble-connect-desktop\Source\TrimbleConnect\Common.UI\AttachedBehaviours\AcceptOnEnter.cs</Path>
      <Metrics>
        <Lines>91</Lines>
        <Classes>1</Classes>
        <Accessors>0</Accessors>
        <Statements>20</Statements>
        <Functions>5</Functions>
        <PublicApi>4</PublicApi>
        <PublicUndocumentedApi>0</PublicUndocumentedApi>
        <Complexity>15</Complexity>
        <FileComplexityDistribution>0=0;5=0;10=1;20=0;30=0;60=0;90=0</FileComplexityDistribution>
        <FunctionComplexityDistribution>1=3;2=0;4=2;6=0;8=0;10=0;12=0</FunctionComplexityDistribution>
        <Comments>
          <NoSonar />
          <NonBlank>
            <Line>7</Line>
            <Line>8</Line>
            <Line>9</Line>
            <Line>12</Line>
            <Line>13</Line>
            <Line>14</Line>
          </NonBlank>
        </Comments>
        <LinesOfCode>
          <Line>1</Line>
          <Line>2</Line>
          <Line>3</Line>
          <Line>4</Line>
          <Line>5</Line>
          <Line>10</Line>
          <Line>11</Line>
          <Line>15</Line>
          <Line>16</Line>
          <Line>23</Line>
          <Line>24</Line>
          <Line>25</Line>
          <Line>26</Line>
        </LinesOfCode>
      </Metrics>
      <Issues />
      <CopyPasteTokens>
        <Token>
          <Value>bmFtZXNwYWNl</Value>
          <Line>1</Line>
        </Token>
        <Token>
          <Value>VHJpbWJsZQ==</Value>
          <Line>1</Line>
        </Token>
        <Token>
          <Value>Lg==</Value>
          <Line>1</Line>
        </Token>
        <Token>
          <Value>Q29ubmVjdA==</Value>
          <Line>1</Line>
        </Token>
        <Token>
          <Value>Lg==</Value>
          <Line>1</Line>
        </Token>
        <Token>
          <Value>Q29tbW9u</Value>
          <Line>1</Line>
        </Token>
        <Token>
          <Value>Lg==</Value>
          <Line>1</Line>
        </Token>
        <Token>
          <Value>VUk=</Value>
          <Line>1</Line>
        </Token>
        <Token>
          <Value>Lg==</Value>
          <Line>1</Line>
        </Token>
      </CopyPasteTokens>
    </File>
  </Files>
</AnalysisOutput> """>
