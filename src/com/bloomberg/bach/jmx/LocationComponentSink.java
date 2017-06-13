package com.bloomberg.bach.jmx;

import org.apache.commons.configuration.SubsetConfiguration;
import org.apache.hadoop.metrics2.MetricsRecord;
import org.apache.hadoop.metrics2.MetricsSink;

/**
 * @author Ronald Macmaster
 * Place to sink the JMX Metrics for my location table.
 */
public class LocationComponentSink implements MetricsSink {
	
	/* (non-Javadoc)
	 * @see org.apache.hadoop.metrics2.MetricsPlugin#init(org.apache.commons.configuration.SubsetConfiguration)
	 */
	@Override
	public void init(SubsetConfiguration conf) {
		// TODO Auto-generated method stub
	}
	
	/* (non-Javadoc)
	 * @see org.apache.hadoop.metrics2.MetricsSink#flush()
	 */
	@Override
	public void flush() {
		// TODO Auto-generated method stub
	}
	
	/* (non-Javadoc)
	 * @see org.apache.hadoop.metrics2.MetricsSink#putMetrics(org.apache.hadoop.metrics2.MetricsRecord)
	 */
	@Override
	public void putMetrics(MetricsRecord record) {
		System.out.println(record);
	}
	
}
