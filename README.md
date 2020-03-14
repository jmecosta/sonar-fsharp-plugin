# SonarQube F# Code Analyzer Plugin

The plugin enables analysis of F# within SonarQube.

## Builds

[![Build status](https://ci.appveyor.com/api/projects/status/jira637y22trnuc4/branch/master?svg=true)](https://ci.appveyor.com/project/jorgecosta/sonar-fsharp-plugin-wxq94/branch/master)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=jmecsoftware.sonarqube.fsharp%3Asonar-communityfsharp-analyzer&metric=alert_status)](https://sonarcloud.io/dashboard?id=jmecsoftware.sonarqube.fsharp%3Asonar-communityfsharp-analyzer)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=jmecsoftware.sonarqube.fsharp%3Asonar-communityfsharp-analyzer&metric=coverage)](https://sonarcloud.io/dashboard?id=jmecsoftware.sonarqube.fsharp%3Asonar-communityfsharp-analyzer)

Download latest snapshot from Appveyor: <https://ci.appveyor.com/project/jorgecosta/sonar-fsharp-plugin-wxq94/build/artifacts>

## Description / Features

- Metrics: LOC, number of classes, number of methods
- Code duplication detection
- Based on [FSharpLint](http://fsprojects.github.io/FSharpLint/)
- Runs under .NET Core 3.1 on Windows and Linux

## Configuration

### Requirements

- Minimal supported version of SonarQube: 7.9 LTS
- Working on SonarQube 8.0
- Analyzer uses .NET Core 3.1, the corresponding depencies of .NET Core
  needs to be installed (especially on Linux). .NET Core is not
  required to be installed, as the application is _self-contained_.
- Operating system (64 bit only): Windows, Linux.

### Installation

- Download the JAR plugin file from releases and copy it to the
_extensions/downloads/_ directory of your SonarQube installation.
  - Delete any  previous plugin `sonarqube-fsharp-plugin-*.jar` or
  `sonar-communityfsharp-plugin-*.jar` from plugins directory.
- Restart SonarQube server.
The plugin will be moved to the directory _extensions/plugins/_ and the previous plugin version will be removed.
The file _logs/sonar.log_ will contain a log line indicating the loaded
plugin or any errors. The installed F# plugin will also be shown
on the Marketplace of your SonarQube installation.
- Review the F# quality profile before running for any updated rule set.

### General Configuration

- `sonar.fs.file.suffixes` - files extensions to import

### Coverage and Tests

Please use generic solutions like
<https://docs.sonarqube.org/latest/analysis/generic-test/> or
<https://github.com/jmecsoftware/sonarqube-testdata-plugin>
