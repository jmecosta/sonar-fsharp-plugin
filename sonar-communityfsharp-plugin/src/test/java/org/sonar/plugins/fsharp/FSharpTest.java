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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.lang.StringUtils;
import org.junit.Test;
import org.sonar.api.config.Configuration;
import org.sonar.api.config.internal.MapSettings;

public class FSharpTest {
    @Test
    public void baseClassConstructorCall() {
        Configuration configuration = (new MapSettings()).asConfig();
        FSharp fsharp = new FSharp(configuration);

        assertEquals("Language Key", FSharpPlugin.LANGUAGE_KEY, fsharp.getKey());
        assertEquals("Language Name", FSharpPlugin.LANGUAGE_NAME, fsharp.getName());
    }

    @Test
    public void equals_true() {
        Configuration configuration = (new MapSettings()).asConfig();
        FSharp fsharp1 = new FSharp(configuration);
        FSharp fsharp2 = new FSharp(configuration);

        boolean areEqual = fsharp1.equals(fsharp2);

        assertTrue(areEqual);
    }

    @Test
    public void equals_false() {
        Configuration configuration1 = (new MapSettings()).asConfig();
        Configuration configuration2 = (new MapSettings()).asConfig();
        FSharp fsharp1 = new FSharp(configuration1);
        FSharp fsharp2 = new FSharp(configuration2);

        boolean areEqual = fsharp1.equals(fsharp2);

        assertFalse(areEqual);
    }

    @Test
    public void fileSuffixes_default() {
        Configuration configuration = (new MapSettings()).asConfig();
        FSharp fsharp = new FSharp(configuration);

        String[] suffixes = fsharp.getFileSuffixes();

        assertEquals(3, suffixes.length);
        for (String suffix : suffixes) {
            assertTrue("`" + suffix + "` not found", FSharpPlugin.FILE_SUFFIXES_DEFVALUE.contains(suffix));
        }
    }

    @Test
    public void fileSuffixes_userDefined() {
        String keys = ".fs,.fsx";
        int no_keys = 1 + StringUtils.countMatches(keys, ",");
        MapSettings settings = new MapSettings();
        settings.setProperty(FSharpPlugin.FILE_SUFFIXES_KEY, keys);
        Configuration configuration = settings.asConfig();
        FSharp fsharp = new FSharp(configuration);

        String[] suffixes = fsharp.getFileSuffixes();

        assertEquals(no_keys, suffixes.length);
        for (String suffix : suffixes) {
            assertTrue("`" + suffix + "` not found", keys.contains(suffix));
        }
    }
}
