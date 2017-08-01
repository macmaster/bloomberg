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
import java.lang.reflect.Constructor;

import org.apache.commons.configuration2.SubsetConfiguration;
import org.apache.hadoop.classification.InterfaceAudience;
import org.apache.hadoop.classification.InterfaceStability;
import org.apache.hadoop.metrics2.MetricsException;
import org.apache.hadoop.metrics2.MetricsRecord;
import org.apache.hadoop.metrics2.MetricsSink;

import com.bloomberg.bach.metrics.RecordHandler;

/**
 * A metrics sink that dumps to a custom record handler.
 */
@InterfaceAudience.Public
@InterfaceStability.Evolving
public class CustomSink implements MetricsSink, Closeable {

  public static final String CLASS_KEY = "class";

  private RecordHandler recordHandler;

  @Override
  public void init(SubsetConfiguration conf) {
    String className = conf.getString(CLASS_KEY, null);

    try { // attempt to instantiate custom record handler.	
      Class<?> cls = Class.forName(className);
      Constructor<?> constructor = cls.getDeclaredConstructor();
      recordHandler = (RecordHandler) constructor.newInstance();

    } catch (Exception err) {
      String errorMessage = String.format("Can't instantiate the custom sink class: %s", className);
      throw new MetricsException(errorMessage);
    }
  }

  @Override
  public void close() throws IOException {
    recordHandler.close();
  }

  @Override
  public void putMetrics(MetricsRecord record) {
    recordHandler.handleRecord(record);
  }

  @Override
  public void flush() {
    recordHandler.flush();
  }

}
