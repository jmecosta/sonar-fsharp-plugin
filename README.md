The plugin enables analysis of F# within SonarQube. 

[![Build status](https://ci.appveyor.com/api/projects/status/jira637y22trnuc4?svg=true)](https://ci.appveyor.com/project/jorgecosta/sonar-fsharp-plugin-wxq94)

Download latest snapshot from : https://ci.appveyor.com/project/jorgecosta/sonar-fsharp-plugin-wxq94/build/artifacts -> ex : sonar-fsharp-plugin-1.0.RC1.jar artifact

# Description / Features

 - Metrics: LOC, number of classes, number of methics, complexity 
 - Code duplication detection
 - FSharpLint Support
 - FxCop Support
 - Unit test metrics and Coverage
 - Runs under windows and Linux (mono)
 
# Configuration
## Installation
Download the plugin from CI and copy to extensions/plugins in your SonarQube server installation. Restart the server and review the F# quality profile before running.

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

