module FsSonarRunnerCore.XmlConfigurationHelper

open FSharpLint.Application.ConfigurationManager
open FSharpLint.Application.XmlConfiguration

/// Tries to convert an old style FSharpLint XmlConfiguration to new JSON config.
// proposed as FSharpLint PR 354 https://github.com/fsprojects/FSharpLint/pull/354
let convertToConfig xmlConfig =
    { ignoreFiles = convertIgnoreFiles xmlConfig
      hints = convertHints xmlConfig
      formatting = convertFormatting xmlConfig
      conventions = convertConventions xmlConfig
      typography = convertTypography xmlConfig }
