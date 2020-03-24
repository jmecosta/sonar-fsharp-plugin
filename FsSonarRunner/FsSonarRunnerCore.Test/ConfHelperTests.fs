namespace FsSonarRunnerCore.Test

open NUnit.Framework
open FsSonarRunnerCore

type TestEnum =
    | TestValue0 = 0
    | TestValue1 = 1
    | TestValue2 = 2
    | TestValue3 = 3

// TODO
[<TestFixture>]
type ConfHelperTests() =
    let configValue = TestEnum.TestValue1
    let config =
        sprintf "<AnalysisInput>
            <Rules>
                <Rule>
                    <Key>EnumTestRule</Key>
                    <Parameters>
                        <Parameter>
                            <Key>EnumTestParam</Key>
                            <Value>%s</Value>
                        </Parameter>
                    </Parameters>
                </Rule>
                <Rule>
                    <Key>RuleWithoutParameter</Key>
                </Rule>
            </Rules>
        </AnalysisInput>" (configValue.ToString())
        |> ConfHelper.InputConfigution.Parse
    let defaultValue = TestEnum.TestValue2

    [<Test>]
    member this.``GetValueForEnum returns `defaultValue` when `ruleId` not found``() =
        let actual: TestEnum = ConfHelper.GetValueForEnum(config, "NoEnumTestRule", "EnumTestParam",  defaultValue)
        Assert.AreEqual(defaultValue, actual)

    [<Test>]
    member this.``GetValueForEnum returns `defaultValue` when `paramName` not found``() =
        let actual: TestEnum = ConfHelper.GetValueForEnum(config, "EnumTestRule", "NoEnumTestParam",  defaultValue)
        Assert.AreEqual(defaultValue, actual)

    [<Test>]
    member this.``GetValueForEnum returns `defaultValue` when `ruleId` has no parameters``() =
        let actual: TestEnum = ConfHelper.GetValueForEnum(config, "RuleWithoutParameter", "NoEnumTestParam",  defaultValue)
        Assert.AreEqual(defaultValue, actual)

    [<Test>]
    member this.``GetValueForEnum returns parsed value``() =
        let actual: TestEnum = ConfHelper.GetValueForEnum(config, "EnumTestRule", "EnumTestParam",  defaultValue)
        Assert.AreEqual(configValue, actual)
