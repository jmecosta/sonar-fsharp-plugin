/*
 * Sonar F# Plugin :: Core
 * Copyright (C) 2015 Jorge Costa and SonarSource
 * dev@sonar.codehaus.org
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02
 */
package org.sonar.plugins.fsharp;

import com.google.common.collect.ImmutableList;
import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.component.ResourcePerspectives;
import org.sonar.api.config.PropertyDefinition;
import org.sonar.api.config.Settings;
import org.sonar.api.profiles.RulesProfile;
import org.sonar.api.resources.Qualifiers;
import org.sonar.squidbridge.rules.SqaleXmlLoader;

import java.util.List;
import org.sonar.plugins.fsharp.fxcop.FxCopConfiguration;
import org.sonar.plugins.fsharp.fxcop.FxCopRulesDefinition;

public class FSharpFxCopProvider {

  private static final String CATEGORY = "F#";
  private static final String SUBCATEGORY = "Code Analysis / FxCop";
  
  private static final FxCopConfiguration FXCOP_CONF = new FxCopConfiguration(FSharpPlugin.LANGUAGE_KEY, "fxcop-fs");

  private FSharpFxCopProvider() {
  }

  public static List extensions() {
    return ImmutableList.of(
      FSharpFxCopRulesDefinition.class);
  }

  public static class FSharpFxCopRulesDefinition extends FxCopRulesDefinition {

    public FSharpFxCopRulesDefinition() {
      super(
        FXCOP_CONF,
        new FxCopRulesDefinitionSqaleLoader() {
          @Override
          public void loadSqale(NewRepository repository) {
            SqaleXmlLoader.load(repository, "/com/sonar/sqale/fxcop.xml");
          }
        });
    }

  }
}
