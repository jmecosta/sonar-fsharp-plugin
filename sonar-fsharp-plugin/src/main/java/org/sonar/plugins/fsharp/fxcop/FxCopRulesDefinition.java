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
package org.sonar.plugins.fsharp.fxcop;

import com.google.common.base.Charsets;
import org.sonar.api.server.rule.RulesDefinition;
import org.sonar.api.server.rule.RulesDefinitionXmlLoader;

import java.io.InputStreamReader;

public class FxCopRulesDefinition implements RulesDefinition {

  private static final String REPOSITORY_NAME = "FxCop / Code Analysis";

  private final FxCopConfiguration fxCopConf;
  private final FxCopRulesDefinitionSqaleLoader sqaleLoader;

  public FxCopRulesDefinition(FxCopConfiguration fxCopConf, FxCopRulesDefinitionSqaleLoader sqaleLoader) {
    this.fxCopConf = fxCopConf;
    this.sqaleLoader = sqaleLoader;
  }

  @Override
  public void define(Context context) {
    NewRepository repository = context
      .createRepository(fxCopConf.repositoryKey(), fxCopConf.languageKey())
      .setName(REPOSITORY_NAME);

    RulesDefinitionXmlLoader loader = new RulesDefinitionXmlLoader();
    loader.load(repository, new InputStreamReader(getClass().getResourceAsStream("/org/sonar/plugins/fxcop/" + fxCopConf.languageKey() + "-rules.xml"), Charsets.UTF_8));

    sqaleLoader.loadSqale(repository);

    repository.done();
  }

  public static interface FxCopRulesDefinitionSqaleLoader {
    void loadSqale(NewRepository repository);
  }

}
