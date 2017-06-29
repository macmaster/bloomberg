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

import java.io.Closeable;
import java.io.IOException;

import org.apache.commons.configuration.SubsetConfiguration;
import org.apache.hadoop.classification.InterfaceAudience;
import org.apache.hadoop.classification.InterfaceStability;
import org.apache.hadoop.metrics2.AbstractMetric;
import org.apache.hadoop.metrics2.MetricsRecord;
import org.apache.hadoop.metrics2.MetricsSink;

/**
 * A metrics sink that dumps to the console.
 */
@InterfaceAudience.Public
@InterfaceStability.Evolving
public class ConsoleSink implements MetricsSink, Closeable {
	
	public static final String FORMAT_KEY = "format";
	private String formatString = "";
	
	@Override
	public void init(SubsetConfiguration conf) {
		formatString = conf.getString(FORMAT_KEY, "%n : %v");
	}
	
	@Override
	public void close() throws IOException {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void putMetrics(MetricsRecord record) {
		System.out.format("timestamp: %s%n", record.timestamp());
		for (AbstractMetric metric : record.metrics()) {
			String output = formatString;
			output = output.replaceAll("%n", metric.name());
			output = output.replaceAll("%d", metric.description());
			output = output.replaceAll("%v", metric.value().toString());
			System.out.println("\t" + output);
		}
	}
	
	@Override
	public void flush() {
		System.out.flush();
	}
	
}
