# Release Notes

## v3.1.0 FSharpLint.Core 0.13.3

- Switch to FSharpLint new (Json) configuration

## v3.0.2

- Supports multiple arguments `/d`, `/f` and `/i` for files to analyze

## v3.0.1 FSharpLint.Core 0.13.0

- Update to last FSharpLint working with XML configuration.

## v3.0.0 SonarQube 7.9 LTS

- Update SonarQube API to 7.9 (latest SonarQube 7.9 LTS is now minimum server requirement)
- Update to FSharpLint.Core 0.12.10

## v2.1.1 .NET Core 3.1

- As EOL of .NET Core 3.0 is reached today, a small maintenance update
- Update to self-contained netcoreapp3.1

## v2.1 .NET Core 3.0

- Update to self-contained netcoreapp3.0
- smaller jar based on assembly linking of .NET Core 3.0

## v2.0 .NET Core 2.2 and Quality Gate

- BREAKING: rename plugin to `sonar-communityfsharp-plugin` to match SonarQube marketplace requirement
- Add analysis of Quality Gate at Sonarcloud.io (no F# analyzed)
- Update to FSharpLint.Core 0.12.2

## v1.1 Update to net472 and FSharpLint 0.12.1

- #30 Update to .NET 4.7.2
- #26 Update all nuget packages and Maven dependencies, especially
  - Update to FSharpLint.Core 0.12.1 with reworked linter and updated FSharp.Compiler.Service
- #57 "Deprecated rules" are removed from ruleset
- Dependency on MSBuild.v12 removed
