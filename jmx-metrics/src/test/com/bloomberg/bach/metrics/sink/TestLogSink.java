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

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.apache.hadoop.metrics2.MetricsSystem;
import org.apache.hadoop.metrics2.annotation.Metric;
import org.apache.hadoop.metrics2.annotation.Metric.Type;
import org.apache.hadoop.metrics2.annotation.Metrics;
import org.apache.hadoop.metrics2.impl.MetricsSystemImpl;
import org.apache.hadoop.metrics2.lib.DefaultMetricsSystem;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;
import org.mockito.Mockito;
import org.slf4j.Logger;

/**
 * Tests the LogSink. <br>
 */
public class TestLogSink {

  // configuration file
  public static String TEST_PREFIX = "test";
  private File configFile;

  // Metrics System.
  private MetricsSystem metricsSystem;

  @Rule // junit test name
  public TestName testName = new TestName();

  @Metrics(name = "jvmMetricsRecord", context = "TestConsoleSink")
  public static class JvmMetrics {

    private static Runtime runtime = Runtime.getRuntime();

    @Metric(value = { "freeMemory", "free memory in the jvm" }, type = Type.GAUGE)
    long freeMemory() {
      return runtime.freeMemory();
    }

    @Metric(value = { "maxMemory", "max memory in the jvm" }, type = Type.GAUGE)
    long maxMemory() {
      return runtime.maxMemory();
    }

    @Metric(value = { "totalMemory", "total memory in the jvm" }, type = Type.GAUGE)
    long getTotalMemory() {
      return runtime.totalMemory();
    }

  }

  @Metrics(name = "junkMetricsRecord", context = "TestConsoleSink")
  public static class JunkMetrics {
    public static String STRING_TAG = "testing the log sink ...";
    public static Integer METRIC1 = -7, METRIC2 = 8675309;

    @Metric(value = { "StringTag", "a simple string metric" }, type = Type.TAG)
    String stringTag() {
      return STRING_TAG;
    }

    @Metric(value = { "metric1", "a simple gauge" }, type = Type.GAUGE)
    long metric1() {
      return METRIC1;
    }

    @Metric(value = { "metric2", "a simple counter" }, type = Type.COUNTER)
    long metric2() {
      return METRIC2;
    }

  }

  /**
   * Configuration file builder.
   */
  public static class LogSinkConfiguration {
    private static final String SINK_PREFIX = "sink.log";
    private static final String PERIOD_KEY = "period";
    private static final String CLASS_KEY = "class";

    private PropertiesConfiguration config;

    public static LogSinkConfiguration build() {
      // can reform later to be a singleton.
      return new LogSinkConfiguration();
    }

    private LogSinkConfiguration() {
      config = new PropertiesConfiguration();
      config.setProperty(configKey(PERIOD_KEY), 100000); // long to avoid automatic sampling.
      config.setProperty(configKey(CLASS_KEY), LogSink.class.getName());
      config.setProperty(configKey(LogSink.FORMAT_KEY), "%n : %v");
      config.setProperty(configKey(LogSink.LEVEL_KEY), "WARN");
      config.setProperty(configKey(LogSink.PREFIX_KEY), "\"\t\"");// long to avoid automatic sampling.
    }

    public LogSinkConfiguration setFormat(String format) {
      config.setProperty(configKey(LogSink.FORMAT_KEY), format);
      return this;
    }

    public LogSinkConfiguration setLevel(String level) {
      config.setProperty(configKey(LogSink.LEVEL_KEY), level);
      return this;
    }

    public LogSinkConfiguration setPrefix(String prefix) {
      config.setProperty(configKey(LogSink.PREFIX_KEY), prefix);
      return this;
    }

    public void save(File file) throws ConfigurationException, IOException {
      config.write(new FileWriter(file));
    }

    private String configKey(String key) {
      return String.format("%s.%s.%s", TEST_PREFIX, SINK_PREFIX, key);
    }

  }

  @Before
  public void setUp() throws IOException {
    this.metricsSystem = new MetricsSystemImpl(TEST_PREFIX);
    this.configFile = new File(String.format("hadoop-metrics2-%s.properties", TEST_PREFIX));
  }

  @After
  public void tearDown() throws IOException {
    this.configFile.delete();
    this.metricsSystem.shutdown();
    DefaultMetricsSystem.shutdown();
  }

  /**
   * Verify correct number of calls to the log sink.
   */
  @Test
  public void testLogSinkCalls() throws Exception {
    // swap LogSink logger for mock logger.
    Logger logger = Mockito.mock(Logger.class);
    Field logField = LogSink.class.getDeclaredField("LOG");
    Field modifiersField = Field.class.getDeclaredField("modifiers");
    modifiersField.setAccessible(true);
    modifiersField.setInt(logField, logField.getModifiers() & ~Modifier.FINAL);
    logField.set(null, logger);

    LogSinkConfiguration.build().setFormat("%n = %v").setLevel("WARN").save(configFile);
    registerMetrics(new Class<?>[] { JvmMetrics.class, JunkMetrics.class });
    collectMetricsSample();

    Mockito.verify(logger, Mockito.atLeast(3)).warn(Mockito.anyString());
    Mockito.verify(logger, Mockito.atMost(9)).warn(Mockito.anyString());
  }

  /**
   * Prints to the log with no exceptions. <br>
   */
  @Test
  public void testSimpleLogOutput() throws Exception {
    LogSinkConfiguration.build().setFormat("(%n\\, %v)").setLevel("WARN").save(configFile);
    registerMetrics(new Class<?>[] { JvmMetrics.class, JunkMetrics.class });
    collectMetricsSample();
  }

  /**
   * Collect and report a single metrics sample. <br>
   */
  private void collectMetricsSample() throws Exception {
    metricsSystem.start();
    metricsSystem.publishMetricsNow();
    metricsSystem.stop();
  }

  private void registerMetrics(Class<?>[] metricsClasses) throws Exception {
    for (Class<?> metric : metricsClasses) {
      metricsSystem.register(metric.newInstance());
    }
  }

}
