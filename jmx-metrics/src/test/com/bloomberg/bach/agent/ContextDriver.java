package com.bloomberg.bach.agent;

import java.io.IOException;

import org.apache.hadoop.metrics2.lib.DefaultMetricsSystem;
import org.apache.log4j.BasicConfigurator;

import com.bloomberg.bach.agent.BachMetricsContext;

/**
 * @author Ronald Macmaster
 * TODO document
 */
public class ContextDriver {
	
	/**
	 * TODO document <br>
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		System.out.println("Metrics Test Driver: main");
		
		System.out.println("%v".replace("%v", "$"));
		BasicConfigurator.configure();
		DefaultMetricsSystem.initialize("test");
		BachMetricsContext.start();
		
		// Configuration config = new Configuration();
		// String key = "client.graphite.server.server_host";
		// System.err.println(String.format("%s : %s %n", key, config.get(key)));	
		// System.getProperties().list(System.out);
		while (true) {
			
		}
	}
	
}
