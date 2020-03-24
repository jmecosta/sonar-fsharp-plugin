module InputConfigHandler

open System
open System.IO
open FSharpLint.Framework.Configuration

let CreateALintConfiguration(path : string): Configuration =
    if not(String.IsNullOrEmpty(path)) && File.Exists(path) then
        let sonarConfig = ConfHelper.InputConfigution.Parse(File.ReadAllText(path))

        // TODO FSharpLint > 0.13.3 grouped configs deprecation: formatting, conventons, typography
        let configuration = {
            ignoreFiles = IgnoreFilesConfig.SonarConfiguration(sonarConfig)
            formatting = FormattingConfig.SonarConfiguration(sonarConfig)
            conventions = ConventionsConfig.SonarConfiguration(sonarConfig)
            typography = TypographyConfig.SonarConfiguration(sonarConfig)
            hints = HintsConfig.SonarConfiguration(sonarConfig)
        }
        configuration
    else
        defaultConfiguration


        // TODO FL0016
        //("FailwithWithSingleArgument",
        //    {
        //        Settings = Map.ofList
        //            [ ("Enabled", ConfHelper.GetEnaFlagForRule(config, "RulesFailwithWithSingleArgument")) ]
        //    })
