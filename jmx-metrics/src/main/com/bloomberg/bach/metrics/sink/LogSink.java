/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.bloomberg.bach.metrics.sink;

import java.io.Closeable;
import java.io.IOException;
import java.util.Date;
import java.util.Formatter;
import java.util.regex.Matcher;

import org.apache.commons.configuration2.SubsetConfiguration;
import org.apache.hadoop.classification.InterfaceAudience;
import org.apache.hadoop.classification.InterfaceStability;
import org.apache.hadoop.metrics2.AbstractMetric;
import org.apache.hadoop.metrics2.MetricsRecord;
import org.apache.hadoop.metrics2.MetricsSink;
import org.apache.hadoop.metrics2.MetricsTag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.event.Level;

/**
 * A metrics sink that dumps to an slf4j log. <br>
 * level : slf4j log level (WARN, TRACE, INFO, etc...) <br>
 * prefix : printed before each formatted metric line. <br>
 * format : format of metric line. <br>
 * %n : name <br>
 * %v : value <br>
 * %d : description <br>
 */
@InterfaceAudience.Public
@InterfaceStability.Evolving
public class LogSink implements MetricsSink, Closeable {

  public static final Logger LOG = LoggerFactory.getLogger(LogSink.class);
  public static final String LEVEL_KEY = "level";
  public static final String PREFIX_KEY = "prefix";
  public static final String FORMAT_KEY = "format";

  private Level level = null;
  private String prefix = "";
  private String format = "";

  @Override
  public void init(SubsetConfiguration conf) {
    this.level = Level.valueOf(conf.getString(LEVEL_KEY, "info"));
    this.prefix = conf.getString(PREFIX_KEY, "\t- ");
    this.format = conf.getString(FORMAT_KEY, "%n : %v");

    // strip quotes
    String quoteRegex = "(^\"|\"$)|(^\'|\'$)";
    this.prefix = this.prefix.replaceAll(quoteRegex, "");
    this.format = this.format.replaceAll(quoteRegex, "");
  }

  @Override
  public void close() throws IOException {
    // Shutdown slf4j factory?
  }

  @Override
  public void flush() {
    // Flush slf4j appenders?
  }

  @Override
  public void putMetrics(MetricsRecord record) {
    StringBuilder tags = new StringBuilder();
    StringBuilder metrics = new StringBuilder();
    String line = "";

    // compile tags.
    tags.append(String.format("TAGS:"));
    for (MetricsTag tag : record.tags()) {
      line = replaceMetricString(format,
          tag.name(), tag.description(), tag.value());
      tags.append(String.format("%n%s%s", prefix, line));
    }

    // compile metrics.
    metrics.append(String.format("METRICS:"));
    for (AbstractMetric metric : record.metrics()) {
      line = replaceMetricString(format,
          metric.name(), metric.description(), metric.value().toString());
      metrics.append(String.format("%n%s%s", prefix, line));
    }

    // print log.
    log("%s: %s", record.name(), new Date(record.timestamp()));
    log(tags.toString());
    log(metrics.toString());
  }

  /**
   * build metrics string from token replacement. <br>
   * format string specifiers: <br>
   * %n = name <br>
   * %d = description <br>
   * %v = value <br>
   */
  private String replaceMetricString(
      String formatString, String name, String description, String value) {
    String output = formatString;
    output = output.replaceAll("%n", Matcher.quoteReplacement(name));
    output = output.replaceAll("%d", Matcher.quoteReplacement(description));
    output = output.replaceAll("%v", Matcher.quoteReplacement(value));
    return output;
  }

  /**
   * Print a formatted log string at the sink's log level. <br>
   * Relies on the formatting of {@link Formatter}.
   */
  private void log(String formatString, Object... args) {
    String output = String.format(formatString, args);
    switch (level) {
    case TRACE:
      LOG.trace(output);
      break;
    case DEBUG:
      LOG.debug(output);
      break;
    case INFO:
      LOG.info(output);
      break;
    case WARN:
      LOG.warn(output);
      break;
    case ERROR:
      LOG.debug(output);
      break;
    default:
      LOG.info(output);
      break;
    }
  }

}
