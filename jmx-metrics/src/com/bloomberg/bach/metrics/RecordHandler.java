package com.bloomberg.bach.metrics;

import java.io.IOException;

import org.apache.hadoop.metrics2.MetricsRecord;

/**
 * @author Ronald Macmaster
 * 
 */
public interface RecordHandler {
	
	public void handleRecord(MetricsRecord record);
	
	public void flush();
	
	public void close() throws IOException;
	
}
