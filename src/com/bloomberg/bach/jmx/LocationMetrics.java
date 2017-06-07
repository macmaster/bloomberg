package com.bloomberg.bach.jmx;

import org.apache.hadoop.metrics2.MetricsInfo;

/**
 * @author Ronald Macmaster
 * Location Metric for Hadoop2.
 */
public class LocationMetrics implements MetricsInfo {
	
	private String name, description;
	
	/**
	 * Constructs a new LocationMetrics object.
	 */
	public LocationMetrics(String name, String description) {
		this.name = name;
		this.description = description;
	}
	
	/* (non-Javadoc)
	 * @see org.apache.hadoop.metrics2.MetricsInfo#description()
	 */
	@Override
	public String description() {
		return this.description;
	}
	
	/* (non-Javadoc)
	 * @see org.apache.hadoop.metrics2.MetricsInfo#name()
	 */
	@Override
	public String name() {
		return this.name;
	}
	
}
