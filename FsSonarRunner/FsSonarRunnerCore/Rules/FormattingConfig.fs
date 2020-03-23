module FormattingConfig

open FSharpLint.Framework.Configuration
open FSharpLint.Rules

(** Rules FL0001-FL0012 *)
let SonarConfiguration(config : ConfHelper.InputConfigution.AnalysisInput): FormattingConfig option =
    let typedItemSpacingConfig: RuleConfig<TypedItemSpacing.Config> option = 
        let ruleId = "RulesFormattingTypedItemSpacingError"
        match ConfHelper.GetEnaFlagForRule(config, ruleId)  with
        | false -> None
        | true ->
            let defaultValue = TypedItemSpacing.TypedItemStyle.SpacesAround.ToString()
            Some {
                enabled = true;
                config = Some { 
                    typedItemStyle = ConfHelper.GetValueForEnum(config, ruleId, "TypedItemStyle", defaultValue) }
            }

    let tupleFormattingConfig = Some { 
        tupleCommaSpacing = ConfHelper.EnableRuleIfExist(config, "RulesFormattingTupleCommaSpacingError") // FL0001
        tupleIndentation = ConfHelper.EnableRuleIfExist(config, "RulesFormattingTupleIndentationError") // FL0002
        tupleParentheses = ConfHelper.EnableRuleIfExist(config, "RulesFormattingTupleParenthesesError") // FL0003
    }

    let patternMatchFormattingConfig = Some { 
        patternMatchClausesOnNewLine = ConfHelper.EnableRuleIfExist(config, "RulesFormattingPatternMatchClausesOnNewLineError") // FL0004
        patternMatchOrClausesOnNewLine = ConfHelper.EnableRuleIfExist(config, "RulesFormattingPatternMatchOrClausesOnNewLineError") // FL0005
        patternMatchClauseIndentation = ConfHelper.EnableRuleIfExist(config, "RulesFormattingPatternMatchClauseIndentationError") // FL0006
        patternMatchExpressionIndentation = ConfHelper.EnableRuleIfExist(config, "RulesFormattingMatchExpressionIndentationError") // FL0007
    }

    Some {
      typedItemSpacing = typedItemSpacingConfig // FL0010
      typePrefixing = ConfHelper.EnableRuleIfExist(config, "RulesTypePrefixing") // FL0011
      unionDefinitionIndentation = ConfHelper.EnableRuleIfExist(config, "RulesFormattingUnionDefinitionIndentationError") // FL0012
      moduleDeclSpacing = ConfHelper.EnableRuleIfExist(config, "RulesFormattingModuleDeclSpacingError") // FL0008
      classMemberSpacing = ConfHelper.EnableRuleIfExist(config, "RulesFormattingClassMemberSpacingError") // FL0009
      tupleFormatting = tupleFormattingConfig
      patternMatchFormatting = patternMatchFormattingConfig
    }
