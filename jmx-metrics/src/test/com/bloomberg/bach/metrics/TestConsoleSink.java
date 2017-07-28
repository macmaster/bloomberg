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

package com.bloomberg.bach.metrics;

import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.PrintStream;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
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

import com.bloomberg.bach.metrics.sink.ConsoleSink;

/**
 * Tests the ConsoleSink. <br>
 * Redirects output from std.out (System.out) to verify console output.
 */
public class TestConsoleSink {

  // configuration file
  public static String TEST_PREFIX = "test";
  private File configFile;

  // redirection io streams
  private BufferedReader reader;
  private PrintStream writer;
  private PrintStream console;

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
    public static String STRING_TAG = "testing the console sink ...";
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

  @Before
  public void setUp() throws IOException {
    this.metricsSystem = new MetricsSystemImpl(TEST_PREFIX);
    this.configFile = new File(String.format("hadoop-metrics2-%s.properties", TEST_PREFIX));

    PipedInputStream inputStream = new PipedInputStream();
    PipedOutputStream outputStream = new PipedOutputStream(inputStream);
    this.reader = new BufferedReader(new InputStreamReader(inputStream));
    this.writer = new PrintStream(outputStream);
    this.console = System.out;
    System.setOut(this.writer); // redirects System.out to test pipedInputStream.
  }

  @After
  public void tearDown() throws IOException {
    System.setOut(this.console);
    this.writer.close();
    this.reader.close();

    this.configFile.delete();
    this.metricsSystem.shutdown();
    DefaultMetricsSystem.shutdown();
  }

  /**
   * Verifies correct console output for metrics reporting. <br>
   * Output is redirected from std.out through piped input and output streams.
   */
  @Test
  public void testConsoleSink() throws Exception {
    buildConfiguration("%n - %v");
    registerMetrics(new Class<?>[] { JvmMetrics.class, JunkMetrics.class });
    collectMetricsSample();

    writer.close(); // close pipe
    String line = "", buffer = "";
    StringBuilder builder = new StringBuilder();
    while ((line = reader.readLine()) != null) {
      builder.append(line + "\n");
    }

    // build buffered output
    buffer = builder.toString();

    assertTrue(buffer.contains("StringTag - testing the console sink ..."));
    assertTrue(buffer.contains("metric1 - -7"));
    assertTrue(buffer.contains("metric2 - 8675309"));
  }

  /**
   * Prints to the console with no exceptions. <br>
   */
  @Test
  public void testConsoleOutput() throws Exception {
    System.setOut(console);
    buildConfiguration("%n - %v");
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

  private void buildConfiguration(String formatString) throws ConfigurationException {
    PropertiesConfiguration config = new PropertiesConfiguration();
    config.addProperty("*.period", 10000); // long to avoid automatic sampling.
    config.addProperty("test.sink.console.class", ConsoleSink.class.getName());
    config.addProperty("test.sink.console.format", formatString);
    config.save(configFile);
  }

}
