package com.bloomberg.bach.jmx;

import org.apache.hadoop.metrics2.MetricsInfo;

/**
 * @author Ronald Macmaster
 * Location Metric for Hadoop2.
 */
public class LocationMetrics implements MetricsInfo {
	
	public static void main(String[] args) throws Exception {
		//		J4pClient j4pClient = new J4pClient("http://localhost:8778/jolokia");
		//		J4pReadRequest req = new J4pReadRequest("java.lang:type=Memory", "HeapMemoryUsage");
		//		J4pReadResponse resp = j4pClient.execute(req);
		//		Map<String, Long> vals = resp.getValue();
		//		long used = vals.get("used");
		//		long max = vals.get("max");
		//		int usage = (int) (used * 100 / max);
		//		for (int idx = 0;; ++idx) {
		//			System.out.println("Memory usage: used: " + used + " / max: " + max + " = " + usage + "%");
		//			Thread.sleep(500);
		//		}
	}
	
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
