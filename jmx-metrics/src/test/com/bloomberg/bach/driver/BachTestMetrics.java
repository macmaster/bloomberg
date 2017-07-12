package com.bloomberg.bach.driver;

import java.util.Date;

import org.apache.hadoop.metrics2.annotation.Metric;
import org.apache.hadoop.metrics2.annotation.Metric.Type;
import org.apache.hadoop.metrics2.annotation.Metrics;

/**
 * @author Ronald Macmaster
 * Creates a new bach metrics context for monitoring.
 */
@Metrics(context = "Hadoop")
public class BachTestMetrics {
	
	@Metric(value = { "counter", "custom bach metric" }, type = Type.GAUGE)
	public long getCounter() {
		return -42;
	}
	
	@Metric(value = { "name", "application's name" }, type = Type.TAG)
	public String getApplicationName() {
		return this.getClass().getName();
	}
	
	@Metric(value = { "datetime", "the current datetime" }, type = Type.DEFAULT)
	public String getDate() {
		return new Date().toString();
	}
	
	@Metric(value = { "classloader", "application class loader" }, type = Type.DEFAULT)
	public String getClassLoader() {
		return this.getClass().getClassLoader().getClass().getName();
	}
	
}
