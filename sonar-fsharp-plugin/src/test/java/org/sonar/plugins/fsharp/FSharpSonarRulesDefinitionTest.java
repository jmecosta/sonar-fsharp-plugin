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

import static org.junit.Assert.assertEquals;
import org.sonar.plugins.fsharp.FSharpSonarRulesDefinition;
import org.junit.Test;
import org.sonar.api.server.rule.RulesDefinition.Context;

public class FSharpSonarRulesDefinitionTest {

  @Test
  public void test() {
    Context context = new Context();
    assertEquals(0, context.repositories().size());

    new FSharpSonarRulesDefinition().define(context);
    assertEquals(1, context.repositories().size());    
    assertEquals(43, context.repository("fsharplint").rules().size());
  }
}
