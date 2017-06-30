package com.bloomberg.bach.context;

import java.util.Date;

import org.apache.hadoop.metrics2.annotation.Metric;
import org.apache.hadoop.metrics2.annotation.Metrics;

/**
 * @author Ronald Macmaster
 * Creates a new bach metrics context for monitoring.
 */
@Metrics(context = "Hadoop")
public class BachMetricsJVM {
	
	@Metric({ "counter", "custom bach metric" })
	public String getCounter() {
		return Integer.toBinaryString(-42);
	}
	
	@Metric({ "name", "application's name" })
	public String getApplicationName() {
		return this.getClass().getName();
	}
	
	@Metric({ "datetime", "the current datetime" })
	public String getDate() {
		return new Date().toString();
	}
	
	@Metric({ "classloader", "application class loader" })
	public String getClassLoader() {
		return this.getClass().getClassLoader().getClass().getName();
	}
	
}
