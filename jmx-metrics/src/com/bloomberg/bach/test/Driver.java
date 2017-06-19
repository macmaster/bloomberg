package com.bloomberg.bach.test;

import org.apache.hadoop.metrics2.MetricsSystem;
import org.apache.hadoop.metrics2.lib.DefaultMetricsSystem;

import com.bloomberg.bach.context.BachMetricsContext;

/**
 * @author Ronald Macmaster
 * TODO document
 */
public class Driver {
	
	/**
	 * TODO document <br>
	 */
	public static void main(String[] args) {
		System.out.println("Metrics Test Driver: main");
		MetricsSystem system = DefaultMetricsSystem.initialize("bach.metrics");
		BachMetricsContext.registerContext(system);
		system.start();
		
		System.getProperties().list(System.out);
		while (true) {
		}
	}
	
}
