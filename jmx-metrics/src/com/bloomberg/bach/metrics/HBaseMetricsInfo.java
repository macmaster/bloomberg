package com.bloomberg.bach.metrics;

import org.apache.hadoop.metrics2.MetricsInfo;

/**
 * @author Ronald Macmaster
 * HBase metrics collection. Logging is fed from the JMX server.
 */
public enum HBaseMetricsInfo implements MetricsInfo {
	
	HBaseMetrics("HBase client metrics collected from JMX server.");
	
	private String description;
	
	private HBaseMetricsInfo(String description) {
		this.description = description;
	}
	
	@Override
	public String description() {
		return description;
	}
	
}
