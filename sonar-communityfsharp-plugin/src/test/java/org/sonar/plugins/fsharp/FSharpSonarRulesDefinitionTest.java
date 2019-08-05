/*
 * Sonar FSharp Plugin, open source software quality management tool.
 *
 * Sonar FSharp Plugin is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * Sonar FSharp Plugin is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 */
package org.sonar.plugins.fsharp;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.sonar.api.server.rule.RulesDefinition.Context;

public class FSharpSonarRulesDefinitionTest {
  @Test
  public void repositories_exactelyOne() {
    // Arrange
    Context context = new Context();
    assertEquals(0, context.repositories().size());

    // Act
    new FSharpSonarRulesDefinition().define(context);

    // Assert
    assertEquals(1, context.repositories().size());
  }

  @Test
  public void repository_expectedNameAndKey() {
    // Arrange
    Context context = new Context();

    // Act
    new FSharpSonarRulesDefinition().define(context);

    // Assert
    assertEquals(FSharpPlugin.REPOSITORY_NAME, context.repositories().get(0).name());
    assertNotNull(context.repository(FSharpPlugin.REPOSITORY_KEY));
  }

  @Test
  @Disabled("not all FSharpLint rules can be configured today, see https://github.com/jmecsoftware/sonar-fsharp-plugin/issues/75")
  public void FSharpLint_numberOfRules() {
    // Arrange
    Context context = new Context();
    assertEquals(0, context.repositories().size());

    // Act
    new FSharpSonarRulesDefinition().define(context);

    // Assert
    assertEquals(55, context.repository(FSharpPlugin.REPOSITORY_KEY).rules().size());
  }
}
