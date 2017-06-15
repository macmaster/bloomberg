package com.bloomberg.bach.jmx;

import org.apache.hadoop.metrics2.MetricsCollector;
import org.apache.hadoop.metrics2.MetricsRecordBuilder;
import org.apache.hadoop.metrics2.MetricsSource;

/**
 * @author Ronald Macmaster
 * Component source for getting the JMX Metrics. 
 */
public class LocationComponentSource implements MetricsSource {
	
	/* (non-Javadoc)
	 * @see org.apache.hadoop.metrics2.MetricsSource#getMetrics(org.apache.hadoop.metrics2.MetricsCollector, boolean)
	 */
	@Override
	public void getMetrics(MetricsCollector collector, boolean all) {
		MetricsRecordBuilder builder = collector.addRecord("testMetric").setContext("testContext");
		builder.addCounter(new LocationMetrics("counter", "A simple location test counter."), 2);
	}
	
}
