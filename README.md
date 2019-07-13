# SonarQube F# Code Analyzer Plugin

The plugin enables analysis of F# within SonarQube.

[![Build status](https://ci.appveyor.com/api/projects/status/jira637y22trnuc4?svg=true)](https://ci.appveyor.com/project/jorgecosta/sonar-fsharp-plugin-wxq94)

Download latest snapshot from : <https://ci.appveyor.com/project/jorgecosta/sonar-fsharp-plugin-wxq94/build/artifacts>

## Description / Features

- Metrics: LOC, number of classes, number of methods
- Code duplication detection
- FSharpLint Support
- Runs under .NET Core 2.2 on Windows, Linux, and OS-X

## Configuration

### Requirements

- Minimal supported version of SonarQube: 6.7 LTS
- Analyzer is deployed as .NET Core 2.2 framework dependent executeable (FDE).
- .NET Core 2.2 runtime needs to be installed on the build server.

### Installation

- Download the JAR plugin file from releases and copy it to the _extensions/plugins/_
directory of your SonarQube installation then start server.
The file _logs/sonar.log_ will then contain a log line indicating the loaded
plugin or any errors. The installed plugin will also be shown
on the Marketplace of your SonarQube installation.
- Review the F# quality profile before running.
- Install .NET Core 2.2 Runtime on build server. Download is available from [Microsoft](https://dotnet.microsoft.com/download).

### General Configuration

- `sonar.fs.file.suffixes` - files extensions to import

### Coverage and Tests

Please use generic solutions like
<https://docs.sonarqube.org/latest/analysis/generic-test/> or
<https://github.com/jmecsoftware/sonarqube-testdata-plugin>
