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

import org.sonar.plugins.fsharp.FSharp;
import org.sonar.plugins.fsharp.FSharpPlugin;
import org.junit.Before;
import org.junit.Test;
import org.sonar.api.config.Settings;

import static org.fest.assertions.Assertions.assertThat;

public class FSharpTest {

  private Settings settings;
  private FSharp fsharp;

  @Before
  public void init() {
    settings = Settings.createForComponent(new FSharpPlugin());
    fsharp = new FSharp(settings);
  }

  @Test
  public void shouldGetDefaultFileSuffixes() {
    assertThat(fsharp.getFileSuffixes()).containsOnly(".fs", ".fsx", ".fsi");
  }

  @Test
  public void shouldGetCustomFileSuffixes() {
    settings.setProperty(FSharpPlugin.FILE_SUFFIXES_KEY, ".fs,.fsharp");
    assertThat(fsharp.getFileSuffixes()).containsOnly(".fs", ".fsharp");
  }

}
