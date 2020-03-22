module FormattingConfig

open FSharpLint.Framework.Configuration

let SonarConfiguration(config : ConfHelper.InputConfigution.AnalysisInput): FormattingConfig option =
    Some {
      typedItemSpacing = None
      typePrefixing = ConfHelper.EnableRuleIfExist(config, "RulesTypePrefixing") // FL0011
      unionDefinitionIndentation = ConfHelper.EnableRuleIfExist(config, "RulesFormattingUnionDefinitionIndentationError") // FL0012
      moduleDeclSpacing = ConfHelper.EnableRuleIfExist(config, "RulesFormattingModuleDeclSpacingError") // FL0008
      classMemberSpacing = ConfHelper.EnableRuleIfExist(config, "RulesFormattingClassMemberSpacingError") // FL0009
      tupleFormatting = None
      patternMatchFormatting = None
    }
