The plugin enables analysis of F# within SonarQube. 

[![Build status](https://ci.appveyor.com/api/projects/status/osw71fasx87k64au/branch/master?svg=true)](https://ci.appveyor.com/project/jorgecosta/sonar-fsharp-plugin/branch/master)

# Description / Features

 - Metrics: LOC, number of classes, number of methics, complexity 
 - Code duplication detection
 - FSharpLint Support
 - FxCop Support
 - Unit test metrics and Coverage
 - Runs under windows and Linux (mono)
 
# Configuration
## General Configuration
  sonar.fs.file.suffixes - files extensions to import

## Coverage
 Generate reports and feed using the following properties
   - sonar.fs.ncover3.reportsPaths 
   - sonar.fs.opencover.reportsPaths
   - sonar.fs.dotcover.reportsPaths
   - sonar.fs.vscoveragexml.reportsPaths

## Test Reports
Generate reports and feed using the following properties
  - sonar.fs.vstest.reportsPaths
  - sonar.fs.nunit.reportsPaths

