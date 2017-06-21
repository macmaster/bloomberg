package com.bloomberg.bach.context;

import java.io.IOException;

import org.apache.hadoop.metrics2.MetricsSystem;
import org.apache.hadoop.metrics2.lib.DefaultMetricsSystem;

import com.bloomberg.bach.metrics.BachMetricsJVM;

/**
 * @author Ronald Macmaster
 * Context Package for Bach Metrics package.
 * Manages a singleton context.
 */
public class BachMetricsContext {
	
	private static volatile BachMetricsContext context = null;
	private static BachMetricsServer server = null;
	private static MetricsSystem system = DefaultMetricsSystem.initialize("client");
	
	private BachMetricsContext() {
		system.register(new BachMetricsJVM());
	}
	
	private synchronized static BachMetricsContext registerContext() {
		if (context == null) {
			context = new BachMetricsContext();
		} // return singleton context.
		return context;
	}
	
	/**
	 * Start metrics JSON service on localhost and an arbitrary port.  <br>
	 */
	public static void start() throws IOException {
		server = new BachMetricsServer();
		register();
		server.start();
	}
	
	/**
	 * Start the metrics JSON service on the given host and port. <br>
	 */
	public static void start(String host, Integer port) throws IOException {
		server = new BachMetricsServer(host, port);
		register();
		server.start();
	}
	
	/**
	 * Stop the metrics context. <br>
	 */
	public static void stop() throws IOException {
		server.stop();
	}
	
	public static Integer getPort() {
		if (server != null) {
			return server.getPort();
		} else {
			throw new IllegalStateException("Metrics Context has not been started!");
		}
	}
	
	public static String getHost() {
		if (server != null) {
			return server.getHost();
		} else {
			throw new IllegalStateException("Metrics Context has not been started!");
		}
	}
	
	private static void register() {
		// initialize metrics system.
		System.out.format("Bach Metrics Context: started on  <%s, %d> %n", server.getHost(), server.getPort());
		BachMetricsContext.registerContext();
		registerGraphiteSink();
		
		// debug system properties.
		// System.getProperties().list(System.out);
	}
	
	/**
	 * Registers a Graphite Sink with the default metrics source. <br>
	 */
	private static void registerGraphiteSink() {
		// GraphiteSink sink = new GraphiteSink();
		// system.register("graphiteSink", "Bach Metrics Graphite Sink.", sink);
	}
	
}
