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
import java.util.Date;
import java.util.regex.Matcher;

import org.apache.commons.configuration.SubsetConfiguration;
import org.apache.hadoop.classification.InterfaceAudience;
import org.apache.hadoop.classification.InterfaceStability;
import org.apache.hadoop.metrics2.AbstractMetric;
import org.apache.hadoop.metrics2.MetricsRecord;
import org.apache.hadoop.metrics2.MetricsSink;
import org.apache.hadoop.metrics2.MetricsTag;

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
		System.out.format("%s: %s %n", record.name(), new Date(record.timestamp()));
		String metricsString = "";
		
		// print tags.
		System.out.println("tags:");
		for (MetricsTag tag : record.tags()) {
			metricsString = replaceMetricString(formatString, tag.name(), tag.description(), tag.value());
			System.out.format("\t%s%n", metricsString);
		}

		// print metrics.
		System.out.println("metrics:");
		for (AbstractMetric metric : record.metrics()) {
			metricsString = replaceMetricString(formatString, metric.name(), metric.description(), metric.value().toString());
			System.out.format("\t%s%n", metricsString);
		}
	}
	
	@Override
	public void flush() {
		System.out.flush();
	}
	
	/**
	 * build metrics string from token replacement. <br>
	 * format string specifiers: <br>
	 * %n = name <br>
	 * %d = description <br>
	 * %v = value <br>
	 */
	private String replaceMetricString(String formatString, String name, String description, String value) {
		String output = formatString;
		output = output.replaceAll("%n", Matcher.quoteReplacement(name));
		output = output.replaceAll("%d", Matcher.quoteReplacement(description));
		output = output.replaceAll("%v", Matcher.quoteReplacement(value));
		return output;
	}
	
}
