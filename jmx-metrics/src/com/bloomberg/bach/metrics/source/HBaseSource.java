package com.bloomberg.bach.metrics.source;

import static com.bloomberg.bach.metrics.source.HBaseMetricsInfo.HBaseMetrics;

import org.apache.hadoop.classification.InterfaceAudience;
import org.apache.hadoop.classification.InterfaceStability;
import org.apache.hadoop.metrics2.MetricsCollector;
import org.apache.hadoop.metrics2.MetricsSource;
import org.apache.hadoop.metrics2.MetricsSystem;

/**
 * A metrics source that draws already-reported metrics from the jmx server.
 * Used for mapping metrics reported outside metrics2 to sinks through the MetricsSystem.
 */
@InterfaceAudience.Public
@InterfaceStability.Evolving
public class HBaseSource implements MetricsSource {
	
	// singleton JmxSource object
	private static volatile HBaseSource INSTANCE = null;
	private String processName, sessionId;
	
	/**
	 * Constructs a new JmxSource singleton
	 */
	private HBaseSource(String processName, String sessionId) {
		this.processName = processName;
		this.sessionId = sessionId;
	}
	
	/**
	 * Construct a singleton instance of the HBaseSource for metrics collection. <br>
	 */
	private static synchronized HBaseSource initSingleton(String processName, String sessionId) {
		if (INSTANCE == null) {
			INSTANCE = new HBaseSource(processName, sessionId);
		} // return singleton context.
		return INSTANCE;
	}
	
	/**
	 * Construct a singleton instance of the HBaseSource for metrics collection. <br>
	 * Register it with the MetricsSystem.
	 */
	public static HBaseSource create(String processName, String sessionId, MetricsSystem system) {
		return system.register(HBaseMetrics.name(), HBaseMetrics.description(), initSingleton(processName, sessionId));
	}
	
	@Override
	public void getMetrics(MetricsCollector collector, boolean all) {
		// MetricsRecordBuilder rb = collector.addRecord(HBaseMetrics).setContext("jvm").tag(ProcessName, processName).tag(SessionId, sessionId);
		
	}
	
}
