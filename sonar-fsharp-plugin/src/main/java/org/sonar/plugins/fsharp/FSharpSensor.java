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
import org.sonar.api.batch.DependedUpon;
import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.config.Settings;
import org.sonar.api.issue.NoSonarFilter;
import org.sonar.api.measures.CoreMetrics;
import org.sonar.api.measures.FileLinesContext;
import org.sonar.api.measures.FileLinesContextFactory;
import org.sonar.api.utils.command.Command;
import org.sonar.api.utils.command.CommandExecutor;
import org.sonar.api.utils.command.StreamConsumer;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.Base64;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.commons.lang.StringEscapeUtils;
import org.sonar.api.batch.rule.ActiveRule;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;
import org.sonar.api.batch.sensor.Sensor;
import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.api.batch.sensor.SensorDescriptor;
import org.sonar.api.batch.sensor.cpd.NewCpdTokens;
import org.sonar.api.batch.sensor.highlighting.NewHighlighting;
import org.sonar.api.batch.sensor.highlighting.TypeOfText;
import org.sonar.api.batch.sensor.issue.NewIssue;
import org.sonar.api.batch.sensor.issue.NewIssueLocation;
import org.sonar.api.rule.RuleKey;

@DependedUpon("NSonarQubeAnalysis")
public class FSharpSensor implements Sensor {

  private static final Logger LOG = Loggers.get(FSharpSensor.class);

  private final Settings settings;
  private final FsSonarRunnerExtractor extractor;
  private final FileSystem fs;
  private final FileLinesContextFactory fileLinesContextFactory;
  private final NoSonarFilter noSonarFilter;

  public FSharpSensor(Settings settings, FsSonarRunnerExtractor extractor, FileSystem fs, FileLinesContextFactory fileLinesContextFactory,
    NoSonarFilter noSonarFilter) {
    this.settings = settings;
    this.extractor = extractor;
    this.fs = fs;
    this.fileLinesContextFactory = fileLinesContextFactory;
    this.noSonarFilter = noSonarFilter;
  }
  
  @Override
  public void describe(SensorDescriptor descriptor) {
    descriptor.name("F#").onlyOnLanguage(FSharpPlugin.LANGUAGE_KEY);
  }
  
  @Override
  public void execute(SensorContext context) {
    analyze(context);
    importResults(context);
  }

  private void analyze(SensorContext context) {
    StringBuilder sb = new StringBuilder();
    appendLine(sb, "<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
    appendLine(sb, "<AnalysisInput>");
    appendLine(sb, "  <Settings>");
    appendLine(sb, "  </Settings>");
    appendLine(sb, "  <Rules>");
    for (ActiveRule activeRule : context.activeRules().findByRepository(FSharpPlugin.REPOSITORY_KEY)) {
      appendLine(sb, "    <Rule>");
      Map<String, String> parameters = effectiveParameters(activeRule);
      appendLine(sb, "      <Key>" + parameters.get("RuleKey") + "</Key>");      
      if (!parameters.isEmpty()) {
        appendLine(sb, "      <Parameters>");
        for (Entry<String, String> parameter : parameters.entrySet()) {
          if ("RuleKey".equals(parameter.getKey())) {
              continue;
          }
          
          appendLine(sb, "        <Parameter>");
          appendLine(sb, "          <Key>" + parameter.getKey() + "</Key>");
          appendLine(sb, "          <Value>" + StringEscapeUtils.escapeXml(parameter.getValue()) + "</Value>");
          appendLine(sb, "        </Parameter>");
        }
        appendLine(sb, "      </Parameters>");
      }
      appendLine(sb, "    </Rule>");
    }
    appendLine(sb, "  </Rules>");
    appendLine(sb, "  <Files>");
    for (File file : filesToAnalyze()) {
      appendLine(sb, "    <File>" + file.getAbsolutePath() + "</File>");
    }
    appendLine(sb, "  </Files>");
    appendLine(sb, "</AnalysisInput>");

    File analysisInput = toolInput();
    File analysisOutput = toolOutput();

    
    try {
      FSharpSensor.writeStringToFile(analysisInput.getAbsolutePath(), sb.toString());

    File executableFile = extractor.executableFile();

    Command command;
    if (OsUtils.isWindows()) {
      command = Command.create(executableFile.getAbsolutePath())
              .addArgument("/i:" + analysisInput.getAbsolutePath())
              .addArgument("/o:" + analysisOutput.getAbsolutePath());      
    } else {
      command = Command.create("mono")
              .addArgument(executableFile.getAbsolutePath())
              .addArgument("/i:" + analysisInput.getAbsolutePath())
              .addArgument("/o:" + analysisOutput.getAbsolutePath());  
    }

    LOG.debug(command.toCommandLine());
    CommandExecutor.create().execute(command, new LogInfoStreamConsumer(), new LogErrorStreamConsumer(), Integer.MAX_VALUE);
    } catch (IOException e) {
        
        LOG.error("Could not write settings to file '{0}'", e.getMessage());
        
        String msg = new StringBuilder()
          .append("Could not write settings to file: '")
          .append(e)
          .append("'")
          .toString();
    }    
  }

  private static Map<String, String> effectiveParameters(ActiveRule activeRule) {
    Map<String, String> builder = new HashMap<>();

    if (!"".equals(activeRule.templateRuleKey())) {
      builder.put("RuleKey", activeRule.ruleKey().rule());
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
      InputStreamReader reader = null;
      XMLInputFactory xmlFactory = XMLInputFactory.newInstance();

      try {
        reader = new InputStreamReader(new FileInputStream(file), "UTF-8");
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
        closeXmlStream();
        LOG.error("Not able to parse file : {0}", e.getMessage());
      }

      return;
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

      while (stream.hasNext()) {
        int next = stream.next();

        if (next == XMLStreamConstants.END_ELEMENT && "File".equals(stream.getLocalName())) {
          break;
        } else if (next == XMLStreamConstants.START_ELEMENT) {
          String tagName = stream.getLocalName();

          if ("Path".equals(tagName)) {
            String path = stream.getElementText();
            inputFile = fs.inputFile(fs.predicates().hasAbsolutePath(path));
          } else if ("Metrics".equals(tagName)) {
            handleMetricsTag(inputFile);
          } else if ("Issues".equals(tagName)) {
            handleIssuesTag(inputFile, context);
          } else if ("CopyPasteTokens".equals(tagName)) {
            NewCpdTokens cpdTokens = context.newCpdTokens().onFile(inputFile);
            NewHighlighting highlights = context.newHighlighting().onFile(inputFile);            
            handleCopyPasteTokensTag(cpdTokens, highlights);
            cpdTokens.save();
            highlights.save();
          }
        }
      }
    }

    private void handleCopyPasteTokensTag(NewCpdTokens cpdTokens, NewHighlighting highlights) throws XMLStreamException {
      while (stream.hasNext()) {
        int next = stream.next();
    
        if (next == XMLStreamConstants.END_ELEMENT && "CopyPasteTokens".equals(stream.getLocalName())) {
          break;
        } else if (next == XMLStreamConstants.START_ELEMENT) {
          String tagName = stream.getLocalName();

          if ("Token".equals(tagName)) {
            handleTokenTag(cpdTokens, highlights);
          } else {
            throw new IllegalArgumentException();
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
          cpdTokens.addToken(line, leftCol, line, rightCol, value);        
          
          try
          {
              if (highlight != null) {
                  highlights.highlight(line, leftCol, line, rightCol, TypeOfText.valueOf(highlight));
              }
              
          } catch(IllegalArgumentException ex) {
              LOG.error("Invalid token for hightlight : " + highlight + " : " + ex.getMessage());
          }
          
          break;
        } else if (next == XMLStreamConstants.START_ELEMENT) {
          String tagName = stream.getLocalName();

          if ("Value".equals(tagName)) {
            value = new String(Base64.getDecoder().decode(stream.getElementText()), Charset.forName("UTF-8"));
          } else if ("Line".equals(tagName)) {
            line = Integer.parseInt(stream.getElementText());
          } else if ("LeftColoumn".equals(tagName)) {
            leftCol = Integer.parseInt(stream.getElementText());
          } else if ("RightColoumn".equals(tagName)) {
            rightCol = Integer.parseInt(stream.getElementText());
          } else if ("HighLight".equals(tagName)) {
            highlight = stream.getElementText();
          } else {
            throw new IllegalArgumentException();
          }
        }
      }
    }
    
    private void handleMetricsTag(InputFile inputFile) throws XMLStreamException {
      while (stream.hasNext()) {
        int next = stream.next();

        if (next == XMLStreamConstants.END_ELEMENT && "Metrics".equals(stream.getLocalName())) {
          break;
        } else if (next == XMLStreamConstants.START_ELEMENT) {
          String tagName = stream.getLocalName();

          if ("Lines".equals(tagName)) {
            handleLinesMetricTag(inputFile);
          } else if ("Classes".equals(tagName)) {
            handleClassesMetricTag(inputFile);
          } else if ("Accessors".equals(tagName)) {
          } else if ("Statements".equals(tagName)) {
            handleStatementsMetricTag(inputFile);
          } else if ("Functions".equals(tagName)) {
            handleFunctionsMetricTag(inputFile);
          } else if ("PublicApi".equals(tagName)) {
            handlePublicApiMetricTag(inputFile);
          } else if ("PublicUndocumentedApi".equals(tagName)) {
            handlePublicUndocumentedApiMetricTag(inputFile);
          } else if ("Complexity".equals(tagName)) {
            handleComplexityMetricTag(inputFile);
          } else if ("FileComplexityDistribution".equals(tagName)) {
            handleFileComplexityDistributionMetricTag(inputFile);
          } else if ("FunctionComplexityDistribution".equals(tagName)) {
            handleFunctionComplexityDistributionMetricTag(inputFile);
          } else if ("Comments".equals(tagName)) {
            handleCommentsMetricTag(inputFile);
          } else if ("LinesOfCode".equals(tagName)) {
            handleLinesOfCodeMetricTag(inputFile);
          } 
        }
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

    private void handleFileComplexityDistributionMetricTag(InputFile inputFile) throws XMLStreamException {
      String value = stream.getElementText();
      context.<String>newMeasure().forMetric(CoreMetrics.FILE_COMPLEXITY_DISTRIBUTION).on(inputFile).withValue(value).save();
    }

    private void handleFunctionComplexityDistributionMetricTag(InputFile inputFile) throws XMLStreamException {
      String value = stream.getElementText();
      context.<String>newMeasure().forMetric(CoreMetrics.FUNCTION_COMPLEXITY_DISTRIBUTION).on(inputFile).withValue(value).save();
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
      HashSet builder = new HashSet();

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
            throw new IllegalArgumentException();
          }
        }
      }

      noSonarFilter.noSonarInFile(inputFile, builder);
    }

    private void handleNonBlankCommentsMetricTag(InputFile inputFile) throws XMLStreamException {
      Integer value = 0;
      FileLinesContext fileLinesContext = fileLinesContextFactory.createFor(inputFile);

      while (stream.hasNext()) {
        int next = stream.next();

        if (next == XMLStreamConstants.END_ELEMENT && "NonBlank".equals(stream.getLocalName())) {
          break;
        } else if (next == XMLStreamConstants.START_ELEMENT) {
          String tagName = stream.getLocalName();

          if ("Line".equals(tagName)) {
            value++;

            int line = Integer.parseInt(stream.getElementText());
            fileLinesContext.setIntValue(CoreMetrics.COMMENT_LINES_DATA_KEY, line, 1);
          } else {
            throw new IllegalArgumentException();
          }
        }
      }

      fileLinesContext.save();
      context.<Integer>newMeasure().forMetric(CoreMetrics.COMMENT_LINES).on(inputFile).withValue(value).save();
    }

    private void handleLinesOfCodeMetricTag(InputFile inputFile) throws XMLStreamException {
      Integer value = 0;
      FileLinesContext fileLinesContext = fileLinesContextFactory.createFor(inputFile);

      while (stream.hasNext()) {
        int next = stream.next();

        if (next == XMLStreamConstants.END_ELEMENT && "LinesOfCode".equals(stream.getLocalName())) {
          break;
        } else if (next == XMLStreamConstants.START_ELEMENT) {
          String tagName = stream.getLocalName();

          if ("Line".equals(tagName)) {
            value++;

            int line = Integer.parseInt(stream.getElementText());
            fileLinesContext.setIntValue(CoreMetrics.NCLOC_DATA_KEY, line, 1);
          } else {
            throw new IllegalArgumentException();
          }
        }
      }

      fileLinesContext.save();
      context.<Integer>newMeasure().forMetric(CoreMetrics.NCLOC).on(inputFile).withValue(value).save();
    }

    private void handleIssuesTag(InputFile inputFile, SensorContext context) throws XMLStreamException {
      while (stream.hasNext()) {
        int next = stream.next();

        if (next == XMLStreamConstants.END_ELEMENT && "Issues".equals(stream.getLocalName())) {
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
      Integer line = 0;

      while (stream.hasNext()) {
        int next = stream.next();

        if (next == XMLStreamConstants.END_ELEMENT && "Issue".equals(stream.getLocalName())) {
            
          NewIssue newIssue = context.newIssue().forRule(RuleKey.of(FSharpPlugin.REPOSITORY_KEY, id));
          NewIssueLocation location = newIssue.newLocation()
            .on(inputFile)
            .at(inputFile.selectLine(line))
            .message(message);

          newIssue.at(location);
          newIssue.save();          
          
            LOG.info("Save Issue : " + inputFile + " Line " + line + "  message " + message);
          
          break;
        } else if (next == XMLStreamConstants.START_ELEMENT) {
          String tagName = stream.getLocalName();

          if ("Id".equals(tagName)) {
            id = stream.getElementText();
          } else if ("Line".equals(tagName)) {
            line = Integer.parseInt(stream.getElementText());
          } else if ("Message".equals(tagName)) {
            message = stream.getElementText();
          }
        }
      }
    }
  }

  private void appendLine(StringBuilder sb, String line) {
    sb.append(line);
    sb.append("\r\n");
  }

  private static class LogInfoStreamConsumer implements StreamConsumer {

    @Override
    public void consumeLine(String line) {
      LOG.info(line);
    }

  }

  private static class LogErrorStreamConsumer implements StreamConsumer {

    @Override
    public void consumeLine(String line) {
      LOG.error(line);
    }

  }

  private Iterable<File> filesToAnalyze() {
    return fs.files(fs.predicates().hasLanguage(FSharpPlugin.LANGUAGE_KEY));
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
      BufferedWriter writer = null;
      try {
          writer = new BufferedWriter(new FileWriter(file));
          writer.write(content);
      } finally {
          if (writer != null) writer.close();
      }  
  }
}
