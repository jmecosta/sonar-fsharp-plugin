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

import org.apache.commons.lang.StringUtils;
import org.sonar.api.config.Configuration;
import org.sonar.api.resources.AbstractLanguage;

public class FSharp extends AbstractLanguage {

  private final Configuration configuration;

  public FSharp(Configuration configuration) {
    super(FSharpPlugin.LANGUAGE_KEY, FSharpPlugin.LANGUAGE_NAME);
    this.configuration = configuration;
  }

  @Override
  public boolean equals(Object obj) {
    if (!super.equals(obj)) {
      return false;
    }

    FSharp fobj = (FSharp) obj;
    if (configuration.equals(fobj.configuration)) { // added fields are tested
      return true;
    }

    return false;
  }

  @Override
  public String[] getFileSuffixes() {
    String[] suffixes = configuration.getStringArray(FSharpPlugin.FILE_SUFFIXES_KEY);
    if (suffixes.length == 0) {
      suffixes = StringUtils.split(FSharpPlugin.FILE_SUFFIXES_DEFVALUE, ",");
    }

    return suffixes;
  }
}
