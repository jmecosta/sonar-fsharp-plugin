module FormattingConfig

open FSharpLint.Framework.Configuration
open FSharpLint.Rules

[<Literal>]
let TypedItemStyle = TypedItemSpacing.TypedItemStyle.SpacesAround

(** Rules FL0001-FL0012 *)
let SonarConfiguration(config : ConfHelper.InputConfigution.AnalysisInput): FormattingConfig option =

    let typedItemSpacingConfig: RuleConfig<TypedItemSpacing.Config> option =
        let ruleId = "RulesFormattingTypedItemSpacingError"
        match ConfHelper.RuleExists(config, ruleId)  with
        | false -> None
        | true ->
            Some {
                Enabled = true;
                Config = Some { TypedItemStyle = ConfHelper.GetValueForEnum(config, ruleId, "TypedItemStyle", TypedItemStyle) }
            }

    let tupleFormattingConfig = Some {
        tupleCommaSpacing = ConfHelper.EnableRule(config, "RulesFormattingTupleCommaSpacingError") // FL0001
        tupleIndentation = ConfHelper.EnableRule(config, "RulesFormattingTupleIndentationError") // FL0002
        tupleParentheses = ConfHelper.EnableRule(config, "RulesFormattingTupleParenthesesError") // FL0003
    }

    let patternMatchFormattingConfig = Some {
        patternMatchClausesOnNewLine = ConfHelper.EnableRule(config, "RulesFormattingPatternMatchClausesOnNewLineError") // FL0004
        patternMatchOrClausesOnNewLine = ConfHelper.EnableRule(config, "RulesFormattingPatternMatchOrClausesOnNewLineError") // FL0005
        patternMatchClauseIndentation = ConfHelper.EnableRule(config, "RulesFormattingPatternMatchClauseIndentationError") // FL0006
        patternMatchExpressionIndentation = ConfHelper.EnableRule(config, "RulesFormattingMatchExpressionIndentationError") // FL0007
    }

    Some {
      typedItemSpacing = typedItemSpacingConfig // FL0010
      typePrefixing = ConfHelper.EnableRule(config, "RulesTypePrefixing") // FL0011
      unionDefinitionIndentation = ConfHelper.EnableRule(config, "RulesFormattingUnionDefinitionIndentationError") // FL0012
      moduleDeclSpacing = ConfHelper.EnableRule(config, "RulesFormattingModuleDeclSpacingError") // FL0008
      classMemberSpacing = ConfHelper.EnableRule(config, "RulesFormattingClassMemberSpacingError") // FL0009
      tupleFormatting = tupleFormattingConfig
      patternMatchFormatting = patternMatchFormattingConfig
    }
