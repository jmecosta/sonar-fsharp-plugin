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
// adapted from https://github.com/SonarSource/sonar-csharp
package org.sonar.plugins.fsharp;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.sonar.api.batch.DependedUpon;
import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.rule.ActiveRule;
import org.sonar.api.batch.sensor.Sensor;
import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.api.batch.sensor.SensorDescriptor;
import org.sonar.api.batch.sensor.cpd.NewCpdTokens;
import org.sonar.api.batch.sensor.highlighting.NewHighlighting;
import org.sonar.api.batch.sensor.highlighting.TypeOfText;
import org.sonar.api.batch.sensor.issue.NewIssue;
import org.sonar.api.batch.sensor.issue.NewIssueLocation;
import org.sonar.api.issue.NoSonarFilter;
import org.sonar.api.measures.CoreMetrics;
import org.sonar.api.measures.FileLinesContext;
import org.sonar.api.measures.FileLinesContextFactory;
import org.sonar.api.rule.RuleKey;
import org.sonar.api.utils.command.Command;
import org.sonar.api.utils.command.CommandExecutor;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;

@DependedUpon("NSonarQubeAnalysis")
public class FSharpSensor implements Sensor {

  private static final Logger LOG = Loggers.get(FSharpSensor.class);

  private static final String RULE_KEY = "RuleKey";

  private final FsSonarRunnerExtractor extractor;
  private final FileSystem fs;
  private final FileLinesContextFactory fileLinesContextFactory;
  private final NoSonarFilter noSonarFilter;

  public FSharpSensor(FsSonarRunnerExtractor extractor, FileSystem fs, FileLinesContextFactory fileLinesContextFactory,
      NoSonarFilter noSonarFilter) {
    this.extractor = extractor;
    this.fs = fs;
    this.fileLinesContextFactory = fileLinesContextFactory;
    this.noSonarFilter = noSonarFilter;
  }

  @Override
  public void describe(SensorDescriptor descriptor) {
    descriptor.name(FSharpPlugin.LANGUAGE_NAME).onlyOnLanguage(FSharpPlugin.LANGUAGE_KEY);
  }

  @Override
  public void execute(SensorContext context) {
    try {
      analyze(context);
      importResults(context);
    } catch (Exception ex) {
      LOG.error("SonarQube Community F# plugin analyzis failed", ex);
    }
  }

  private void analyze(SensorContext context) {
    StringBuilder sb = createConfiguration(context);
    File analysisInput = toolInput();
    File analysisOutput = toolOutput();

    try {
      String workdirRoot = context.fileSystem().workDir().getCanonicalPath();
      FSharpSensor.writeStringToFile(analysisInput.getAbsolutePath(), sb.toString());

      File executableFile = extractor.executableFile(workdirRoot);

      Command command = Command.create(executableFile.getAbsolutePath());
      command.addArgument("/i:" + analysisInput.getAbsolutePath())
          .addArgument("/o:" + analysisOutput.getAbsolutePath());
      LOG.debug(command.toCommandLine());
      CommandExecutor.create().execute(command, LOG::info, LOG::error, Integer.MAX_VALUE);
    } catch (IOException e) {
      LOG.error("Could not write settings to file '{0}'", e.getMessage());
    }
  }

  private StringBuilder createConfiguration(SensorContext context) {
    StringBuilder sb = new StringBuilder();
    appendLine(sb, 0, "<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
    appendLine(sb, 0, "<AnalysisInput>");
    appendSettings(sb);
    appendRules(sb, context.activeRules().findByRepository(FSharpPlugin.REPOSITORY_KEY));
    appendFiles(sb);
    appendLine(sb, 0, "</AnalysisInput>");
    return sb;
  }

  private void appendSettings(StringBuilder sb) {
    appendLine(sb, 1, "<Settings>");
    appendLine(sb, 1, "</Settings>");
  }

  private void appendRules(StringBuilder sb, Collection<ActiveRule> rules) {
    appendLine(sb, 1, "<Rules>");
    for (ActiveRule activeRule : rules) {
      appendLine(sb, 2, "<Rule>");
      Map<String, String> parameters = effectiveParameters(activeRule);
      appendLine(sb, 3, "<Key>" + parameters.get(RULE_KEY) + "</Key>");
      if (!parameters.isEmpty()) {
        appendLine(sb, 3, "<Parameters>");
        for (Entry<String, String> parameter : parameters.entrySet()) {
          if (RULE_KEY.equals(parameter.getKey())) {
            continue;
          }

          appendLine(sb, 4, "<Parameter>");
          appendLine(sb, 5, "<Key>" + parameter.getKey() + "</Key>");
          appendLine(sb, 5, "<Value>" + StringEscapeUtils.escapeXml(parameter.getValue()) + "</Value>");
          appendLine(sb, 4, "</Parameter>");
        }
        appendLine(sb, 3, "</Parameters>");
      }
      appendLine(sb, 2, "</Rule>");
    }
    appendLine(sb, 1, "</Rules>");
  }

  private void appendFiles(StringBuilder sb) {
    appendLine(sb, 1, "<Files>");
    filesToAnalyze().forEach(f -> appendFile(sb, f));
    appendLine(sb, 1, "</Files>");
  }

  private void appendFile(StringBuilder sb, InputFile file) {
    appendLine(sb, 2, "<File>" + Paths.get(file.uri()).toAbsolutePath() + "</File>");
  }

  private static Map<String, String> effectiveParameters(ActiveRule activeRule) {
    Map<String, String> builder = new HashMap<>();

    if (!"".equals(activeRule.templateRuleKey())) {
      builder.put(RULE_KEY, activeRule.ruleKey().rule());
    }

    for (Map.Entry<String, String> param : activeRule.params().entrySet()) {
      builder.put(param.getKey(), param.getValue());
    }

    return builder;
  }

  private void importResults(SensorContext context) {
    File analysisOutput = toolOutput();

    new AnalysisResultImporter(context, fs, fileLinesContextFactory, noSonarFilter).parse(analysisOutput, context);
  }

  private static class AnalysisResultImporter {

    private final SensorContext context;
    private final FileSystem fs;
    private XMLStreamReader stream;
    private final FileLinesContextFactory fileLinesContextFactory;
    private final NoSonarFilter noSonarFilter;

    public AnalysisResultImporter(SensorContext context, FileSystem fs, FileLinesContextFactory fileLinesContextFactory, NoSonarFilter noSonarFilter) {
      this.context = context;
      this.fs = fs;
      this.fileLinesContextFactory = fileLinesContextFactory;
      this.noSonarFilter = noSonarFilter;
    }

    public void parse(File file, SensorContext context) {
      LOG.trace("-> AnalysisResultImporter.parse start");

      XMLInputFactory xmlFactory = XMLInputFactory.newInstance();

      // disable external entities
      xmlFactory.setProperty(XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES, Boolean.FALSE);
      xmlFactory.setProperty(XMLInputFactory.SUPPORT_DTD, Boolean.FALSE);

      try (FileInputStream fis = new FileInputStream(file);
          InputStreamReader reader = new InputStreamReader(fis, StandardCharsets.UTF_8)) {
        stream = xmlFactory.createXMLStreamReader(reader);

        while (stream.hasNext()) {
          if (stream.next() == XMLStreamConstants.START_ELEMENT) {
            String tagName = stream.getLocalName();

            if ("File".equals(tagName)) {
              handleFileTag(context);
            }
          }
        }
      } catch (IOException | XMLStreamException e) {
        LOG.error("Not able to parse file : {0}", e.getMessage());
      } finally {
        closeXmlStream();
      }

      LOG.trace("<- AnalysisResultImporter.parse end");
    }

    private void closeXmlStream() {
      if (stream != null) {
        try {
          stream.close();
        } catch (XMLStreamException e) {
          LOG.error("Not able to close stream file : {0}", e.getMessage());
        }
      }
    }

    private void handleFileTag(SensorContext context) throws XMLStreamException {
      InputFile inputFile = null;
      LOG.trace("-> handleFileTag of file " + stream.getLocalName() + " start");

      while (stream.hasNext()) {
        int next = stream.next();

        if (next == XMLStreamConstants.END_ELEMENT && "File".equals(stream.getLocalName())) {
          LOG.trace("<- handleFileTag of file " + stream.getLocalName() + " end", stream.getLocalName());
          break;
        } else if (next == XMLStreamConstants.START_ELEMENT) {
          String tagName = stream.getLocalName();

          if ("Path".equals(tagName)) {
            String path = stream.getElementText();
            inputFile = fs.inputFile(fs.predicates().hasAbsolutePath(path));
            String filename = inputFile != null ? inputFile.filename() : "<no input file>";
            LOG.trace("handleFileTag inputFile " + filename);
          } else if ("Metrics".equals(tagName)) {
            handleMetricsTag(inputFile);
          } else if ("Issues".equals(tagName)) {
            handleIssuesTag(inputFile, context);
          } else if ("CopyPasteTokens".equals(tagName)) {
            handleCopyPasteTokensTag(context, inputFile);
          }
        }
      }
    }

    private void handleCopyPasteTokensTag(SensorContext context, InputFile inputFile) throws XMLStreamException {
      NewCpdTokens cpdTokens = context.newCpdTokens().onFile(inputFile);
      NewHighlighting highlights = context.newHighlighting().onFile(inputFile);
      handleCopyPasteTokensTag(cpdTokens, highlights);
      cpdTokens.save();
      highlights.save();
    }

    private void handleCopyPasteTokensTag(NewCpdTokens cpdTokens, NewHighlighting highlights)
        throws XMLStreamException {
      LOG.trace("-> handleCopyPasteTokensTag start");
      while (stream.hasNext()) {
        int next = stream.next();

        if (next == XMLStreamConstants.END_ELEMENT && "CopyPasteTokens".equals(stream.getLocalName())) {
          LOG.trace("<- handleCopyPasteTokensTag start");
          break;
        } else if (next == XMLStreamConstants.START_ELEMENT) {
          String tagName = stream.getLocalName();

          if ("Token".equals(tagName)) {
            handleTokenTag(cpdTokens, highlights);
          } else {
            throw new IllegalArgumentException(tagName);
          }
        }
      }
    }

    private void handleTokenTag(NewCpdTokens cpdTokens, NewHighlighting highlights) throws XMLStreamException {
      String value = null;
      int line = -1;
      int rightCol = -1;
      int leftCol = -1;
      String highlight = null;

      while (stream.hasNext()) {
        int next = stream.next();

        if (next == XMLStreamConstants.END_ELEMENT && "Token".equals(stream.getLocalName())) {
          if (value != null) {
            cpdTokens.addToken(line, leftCol, line, rightCol, value);
          } else {
            LOG.error("value is `null`");
          }

          try {
            if (highlight != null) {
              highlights.highlight(line, leftCol, line, rightCol, TypeOfText.valueOf(highlight));
            }

          } catch (IllegalArgumentException ex) {
            LOG.error("Invalid token for hightlight : " + highlight + " : " + ex.getMessage());
          }

          break;
        } else if (next == XMLStreamConstants.START_ELEMENT) {
          String tagName = stream.getLocalName();
          if (tagName == null) {
            LOG.error("token tag `null`");
            throw new NullPointerException("token tag");
          }

          switch (tagName) {
          case "Value":
            value = new String(Base64.getDecoder().decode(stream.getElementText()), StandardCharsets.UTF_8);
            break;
          case "Line":
            line = Integer.parseInt(stream.getElementText());
            break;
          case "LeftColoumn":
            leftCol = Integer.parseInt(stream.getElementText());
            break;
          case "RightColoumn":
            rightCol = Integer.parseInt(stream.getElementText());
            break;
          case "HighLight":
            highlight = stream.getElementText();
            break;
          default:
            throw new IllegalArgumentException(tagName);
          }
        }
      }
    }

    private void handleMetricsTag(InputFile inputFile) throws XMLStreamException {
      LOG.trace("-> handleMetricsTag start");
      while (stream.hasNext()) {
        int next = stream.next();

        if (next == XMLStreamConstants.END_ELEMENT && "Metrics".equals(stream.getLocalName())) {
          LOG.trace("<- handleMetricsTag end");
          break;
        } else if (next == XMLStreamConstants.START_ELEMENT) {
          handleMetricsStartTag(inputFile);
        }
      }
    }

    private void handleMetricsStartTag(InputFile inputFile) throws XMLStreamException {
      String tagName = stream.getLocalName();
      if (tagName == null) {
        LOG.error("metric tag `null`");
        return;
      }

      switch (tagName) {
      case "Lines":
        handleLinesMetricTag(inputFile);
        break;
      case "Classes":
        handleClassesMetricTag(inputFile);
        break;
      case "Accessors":
        // no handling yet
        break;
      case "Statements":
        handleStatementsMetricTag(inputFile);
        break;
      case "Functions":
        handleFunctionsMetricTag(inputFile);
        break;
      case "PublicApi":
        handlePublicApiMetricTag(inputFile);
        break;
      case "PublicUndocumentedApi":
        handlePublicUndocumentedApiMetricTag(inputFile);
        break;
      case "Complexity":
        handleComplexityMetricTag(inputFile);
        break;
      case "Comments":
        handleCommentsMetricTag(inputFile);
        break;
      case "LinesOfCode":
        handleLinesOfCodeMetricTag(inputFile);
        break;
      default:
        LOG.info("metric tag {} not handled", tagName);
      }
    }

    private void handleLinesMetricTag(InputFile inputFile) throws XMLStreamException {
      Integer value = Integer.parseInt(stream.getElementText());
      context.<Integer>newMeasure().forMetric(CoreMetrics.LINES).on(inputFile).withValue(value).save();
    }

    private void handleClassesMetricTag(InputFile inputFile) throws XMLStreamException {
      Integer value = Integer.parseInt(stream.getElementText());
      context.<Integer>newMeasure().forMetric(CoreMetrics.CLASSES).on(inputFile).withValue(value).save();
    }

    private void handleStatementsMetricTag(InputFile inputFile) throws XMLStreamException {
      Integer value = Integer.parseInt(stream.getElementText());
      context.<Integer>newMeasure().forMetric(CoreMetrics.STATEMENTS).on(inputFile).withValue(value).save();
    }

    private void handleFunctionsMetricTag(InputFile inputFile) throws XMLStreamException {
      Integer value = Integer.parseInt(stream.getElementText());
      context.<Integer>newMeasure().forMetric(CoreMetrics.FUNCTIONS).on(inputFile).withValue(value).save();
    }

    private void handlePublicApiMetricTag(InputFile inputFile) throws XMLStreamException {
      Integer value = Integer.parseInt(stream.getElementText());
      context.<Integer>newMeasure().forMetric(CoreMetrics.PUBLIC_API).on(inputFile).withValue(value).save();
    }

    private void handlePublicUndocumentedApiMetricTag(InputFile inputFile) throws XMLStreamException {
      Integer value = Integer.parseInt(stream.getElementText());
      context.<Integer>newMeasure().forMetric(CoreMetrics.PUBLIC_UNDOCUMENTED_API).on(inputFile).withValue(value).save();
    }

    private void handleComplexityMetricTag(InputFile inputFile) throws XMLStreamException {
      Integer value = Integer.parseInt(stream.getElementText());
      context.<Integer>newMeasure().forMetric(CoreMetrics.COMPLEXITY).on(inputFile).withValue(value).save();
    }

    private void handleCommentsMetricTag(InputFile inputFile) throws XMLStreamException {
      while (stream.hasNext()) {
        int next = stream.next();

        if (next == XMLStreamConstants.END_ELEMENT && "Comments".equals(stream.getLocalName())) {
          break;
        } else if (next == XMLStreamConstants.START_ELEMENT) {
          String tagName = stream.getLocalName();

          if ("NoSonar".equals(tagName)) {
            handleNoSonarCommentsMetricTag(inputFile);
          } else if ("NonBlank".equals(tagName)) {
            handleNonBlankCommentsMetricTag(inputFile);
          }
        }
      }
    }

    private void handleNoSonarCommentsMetricTag(InputFile inputFile) throws XMLStreamException {
      HashSet<Integer> builder = new HashSet<>();

      while (stream.hasNext()) {
        int next = stream.next();

        if (next == XMLStreamConstants.END_ELEMENT && "NoSonar".equals(stream.getLocalName())) {
          break;
        } else if (next == XMLStreamConstants.START_ELEMENT) {
          String tagName = stream.getLocalName();

          if ("Line".equals(tagName)) {
            int line = Integer.parseInt(stream.getElementText());
            builder.add(line);
          } else {
            throw new IllegalArgumentException(tagName);
          }
        }
      }

      noSonarFilter.noSonarInFile(inputFile, builder);
    }

    private void handleNonBlankCommentsMetricTag(InputFile inputFile) throws XMLStreamException {
      int value = 0;
      FileLinesContext fileLinesContext = fileLinesContextFactory.createFor(inputFile);

      while (stream.hasNext()) {
        int next = stream.next();

        if (next == XMLStreamConstants.END_ELEMENT && "NonBlank".equals(stream.getLocalName())) {
          break;
        } else if (next == XMLStreamConstants.START_ELEMENT) {
          String tagName = stream.getLocalName();

          if ("Line".equals(tagName)) {
            value++;
          } else {
            throw new IllegalArgumentException(tagName);
          }
        }
      }

      fileLinesContext.save();
      context.<Integer>newMeasure().forMetric(CoreMetrics.COMMENT_LINES).on(inputFile).withValue(value).save();
    }

    private void handleLinesOfCodeMetricTag(InputFile inputFile) throws XMLStreamException {
      LOG.trace("-> handleLinesOfCodeMetricTag start");
      int value = 0;
      FileLinesContext fileLinesContext = fileLinesContextFactory.createFor(inputFile);

      while (stream.hasNext()) {
        int next = stream.next();

        if (next == XMLStreamConstants.END_ELEMENT && "LinesOfCode".equals(stream.getLocalName())) {
          LOG.trace("<- handleLinesOfCodeMetricTag end");
          break;
        } else if (next == XMLStreamConstants.START_ELEMENT) {
          String tagName = stream.getLocalName();

          if ("Line".equals(tagName)) {
            value++;

            int line = Integer.parseInt(stream.getElementText());
            fileLinesContext.setIntValue(CoreMetrics.NCLOC_DATA_KEY, line, 1);
          } else {
            throw new IllegalArgumentException(tagName);
          }
        }
      }

      fileLinesContext.save();
      context.<Integer>newMeasure().forMetric(CoreMetrics.NCLOC).on(inputFile).withValue(value).save();
    }

    private void handleIssuesTag(InputFile inputFile, SensorContext context) throws XMLStreamException {
      LOG.trace("-> handleIssuesTag start");
      while (stream.hasNext()) {
        int next = stream.next();

        if (next == XMLStreamConstants.END_ELEMENT && "Issues".equals(stream.getLocalName())) {
          LOG.trace("<- handleIssuesTag end");
          break;
        } else if (next == XMLStreamConstants.START_ELEMENT) {
          String tagName = stream.getLocalName();

          if ("Issue".equals(tagName)) {
            handleIssueTag(inputFile, context);
          }
        }
      }
    }

    private void handleIssueTag(InputFile inputFile, SensorContext context) throws XMLStreamException {
      String id = null;
      String message = null;
      Integer line = null;

      LOG.trace("-> handleIssueTag start");

      boolean finished = false;
      while (stream.hasNext() && !finished) {
        int next = stream.next();

        if (next == XMLStreamConstants.END_ELEMENT && "Issue".equals(stream.getLocalName())) {
          if (id != null) {

            RuleKey ruleKey = RuleKey.of(FSharpPlugin.REPOSITORY_KEY, id);
            ActiveRule rule = context.activeRules().find(ruleKey);
            if (rule != null) {
              NewIssue newIssue = context.newIssue().forRule(ruleKey);
              NewIssueLocation location = newIssue.newLocation().on(inputFile);
              if (line != null && line > 0) {
                location = location.at(inputFile.selectLine(line));
              }
              if (message != null) {
                location = location.message(message);
              }

              newIssue.at(location);
              newIssue.save();

              LOG.info("Save Issue : " + inputFile + " Line " + line + "  message " + message);
            } else {
              LOG.error("Rule id is `null`");
            }
          } else {
            LOG.error("Rule not active: " + id);
          }

          LOG.trace("<- handleIssueTag end");
          finished = true;
        } else if (next == XMLStreamConstants.START_ELEMENT) {
          String tagName = stream.getLocalName();
          if (tagName != null) {
            switch (tagName) {
            case "Id":
              id = stream.getElementText();
              break;
            case "Line":
              line = Integer.parseInt(stream.getElementText());
              break;
            case "Message":
              message = stream.getElementText();
              break;
            default:
              LOG.info("issue tag {} not handled", tagName);
            }
          } else {
            LOG.error("issue tag `null`");
          }

        }
      }
    }
  }

  private void appendLine(StringBuilder sb, int indent, String str) {
    sb.append(StringUtils.repeat("  ", indent)).append(str).append(System.lineSeparator());
  }

  private Iterable<InputFile> filesToAnalyze() {
    return fs.inputFiles(fs.predicates().hasLanguage(FSharpPlugin.LANGUAGE_KEY));
  }

  private File toolInput() {
    return new File(fs.workDir(), "fs-analysis-input.xml");
  }

  private File toolOutput() {
    return toolOutput(fs);
  }

  public static File toolOutput(FileSystem fileSystem) {
    return new File(fileSystem.workDir(), "fs-analysis-output.xml");
  }

  public static void writeStringToFile(String path, String content) throws IOException {
    File file = new File(path);
    try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
      writer.write(content);
    }
  }
}
