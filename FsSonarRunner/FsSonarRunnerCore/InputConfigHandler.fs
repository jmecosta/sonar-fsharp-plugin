module InputConfigHandler

open System
open System.IO
open FSharpLint.Framework.Configuration

let CreateALintConfiguration(path : string): Configuration =
    if not(String.IsNullOrEmpty(path)) && File.Exists(path) then
        let sonarConfig = ConfHelper.InputConfigution.Parse(File.ReadAllText(path))

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


        
        //("FailwithWithSingleArgument",
        //    {
        //        Settings = Map.ofList
        //            [ ("Enabled", ConfHelper.GetEnaFlagForRule(config, "RulesFailwithWithSingleArgument")) ]
        //    })
