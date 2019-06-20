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

class RuleProperty {
    private String name;
    private String severity;
    private String htmlDescription;

    public RuleProperty(String severity, String name, String htmlDescription) {
        this.severity = severity;
        this.name = name;
        this.htmlDescription = htmlDescription;
    }

    public String getHtmlDescription() {
        return htmlDescription;
    }

    public String getName() {
        return name;
    }

    public String getSeverity() {
        return severity;
    }
}