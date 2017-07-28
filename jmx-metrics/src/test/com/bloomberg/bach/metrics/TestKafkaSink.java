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

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.hadoop.metrics2.MetricsSystem;
import org.apache.hadoop.metrics2.annotation.Metric;
import org.apache.hadoop.metrics2.annotation.Metric.Type;
import org.apache.hadoop.metrics2.annotation.Metrics;
import org.apache.hadoop.metrics2.impl.MetricsSystemImpl;
import org.apache.hadoop.metrics2.lib.DefaultMetricsSystem;
import org.apache.kafka.clients.producer.MockProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;

import com.bloomberg.bach.metrics.sink.KafkaSink;

/**
 * Tests the KafkaSink. <br>
 * The KafkaSink acts as a Producer, so we can mock it with a MockProducer.
 */
public class TestKafkaSink {

  // configuration file
  public static String TEST_PREFIX = "test", TEST_TOPIC = "testKafkaSink";
  private File configFile = new File(String.format("hadoop-metrics2-%s.properties", TEST_PREFIX));

  // kafka metrics testing
  private KafkaSink kafkaSink;
  private MetricsSystem metricsSystem;
  private MockProducer<String, String> producer;

  @Rule // junit test name
  public TestName testName = new TestName();

  @Metrics(name = "jvmMetricsRecord", context = "TestKafkaSink")
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

  @Metrics(name = "junkMetricsRecord", context = "TestKafkaSink")
  public static class JunkMetrics {
    public static String STRING_TAG = "testing the kafka sink ...";
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
  public void setUp() throws Exception {
    this.buildConfiguration(); // setup Kafka configuration. 
    this.kafkaSink = new KafkaSink();
    this.kafkaSink.topic = "testKafkaSink";
    this.metricsSystem = new MetricsSystemImpl(TEST_PREFIX);
    this.producer = new MockProducer<String, String>(true, new StringSerializer(), new StringSerializer());
  }

  @After
  public void tearDown() throws IOException {
    configFile.delete();
    metricsSystem.shutdown();
    DefaultMetricsSystem.shutdown();
  }

  /**
   * Verify correct Kafka producer output for metrics reporting. <br>
   */
  @Test
  public void testKafkaSink() throws Exception {
    registerMetrics(new Class<?>[] { JunkMetrics.class });
    collectMetricsSample();

    List<ProducerRecord<String, String>> history = this.producer.history();
    List<ProducerRecord<String, String>> expected = Arrays.asList(
        new ProducerRecord<String, String>("testKafkaSink", "TestKafkaSink.StringTag", JunkMetrics.STRING_TAG),
        new ProducerRecord<String, String>("testKafkaSink", "TestKafkaSink.metric1", JunkMetrics.METRIC1.toString()),
        new ProducerRecord<String, String>("testKafkaSink", "TestKafkaSink.metric2", JunkMetrics.METRIC2.toString()));

    Assert.assertTrue(history.containsAll(expected));
    // Assert.assertEquals(expected, history);
  }

  /**
   * Check if KafkaSink can report metrics more than once. <br>
   */
  @Test
  public void testProducerOutput() throws Exception {
    registerMetrics(new Class<?>[] { JvmMetrics.class });
    collectMetricsSample();
    collectMetricsSample();
  }

  /**
   * Substitute the real producer for the mock producer. <br>
   */
  private void swapSinks() throws Exception {
    Class<?> sinkClass = this.kafkaSink.getClass();
    Field producerField = sinkClass.getDeclaredField("producer");
    producerField.setAccessible(true);
    producerField.set(this.kafkaSink, this.producer);
  }

  /**
   * Collect and report a single metrics sample. <br>
   */
  private void collectMetricsSample() throws Exception {
    metricsSystem.start();
    metricsSystem.register("testKafkaSink", "kafka sink for testing", kafkaSink);
    this.swapSinks();
    metricsSystem.publishMetricsNow();
    metricsSystem.unregisterSource("testKafkaSink");
    metricsSystem.stop();
  }

  private void registerMetrics(Class<?>[] metricsClasses) throws Exception {
    for (Class<?> metric : metricsClasses) {
      metricsSystem.register(metric.newInstance());
    }
  }

  private void buildConfiguration() throws ConfigurationException {
    PropertiesConfiguration config = new PropertiesConfiguration();
    config.addProperty("*.period", 10000); // long to avoid automatic sampling.
    config.addProperty("test.sink.kafka.topic", TEST_TOPIC);
    config.save(configFile);
  }

}
