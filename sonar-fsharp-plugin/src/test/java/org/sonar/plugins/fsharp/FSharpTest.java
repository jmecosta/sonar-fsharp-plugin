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

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.Before;
import org.junit.Test;
import org.sonar.api.config.Settings;

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
