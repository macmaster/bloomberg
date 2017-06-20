package com.bloomberg.bach.context;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.util.HashMap;
import java.util.Map;

import org.apache.hadoop.metrics2.MetricsSystem;
import org.apache.hadoop.metrics2.impl.MetricsSystemImpl;
import org.apache.hadoop.metrics2.lib.DefaultMetricsSystem;
import org.apache.hadoop.metrics2.sink.GraphiteSink;
import org.jolokia.jvmagent.JolokiaServer;
import org.jolokia.jvmagent.JolokiaServerConfig;

import com.bloomberg.bach.metrics.BachMetricsJVM;

/**
 * @author Ronald Macmaster
 * Context Package for Bach Metrics package.
 * Manages a singleton context.
 */
public class BachMetricsContext {
	
	private static volatile BachMetricsContext context = null;
	private static volatile MetricsSystem system = DefaultMetricsSystem.initialize("client");
	
	private static String host = "localhost";
	private static Integer port = getFreePort();
	
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
		System.out.format("Bach Metrics Context: start <%s, %d> %n", host, port);
		
		// initialize metrics system.
		BachMetricsContext.registerContext();
		registerGraphiteSink();
		startJolokiaServer();
		system.start();
		
		// debug system properties.
		// System.getProperties().list(System.out);
	}
	
	/**
	 * Start the metrics JSON service on the given host and port. <br>
	 */
	public static void start(String host, Integer port) throws IOException {
		BachMetricsContext.host = host;
		BachMetricsContext.port = port;
		start();
	}
	
	/**
	 * Registers a Graphite Sink with the default metrics source. <br>
	 */
	private static void registerGraphiteSink() {
		GraphiteSink sink = new GraphiteSink();
		system.register("graphiteSink", "Bach Metrics Graphite Sink.", sink);
	}
	
	/**
	 * Configure and start the jolokia server. <br>
	 * @throws IOException 
	 */
	private static void startJolokiaServer() throws IOException {
		// configure and start Jolokia server.
		Map<String, String> configMap = new HashMap<String, String>();
		configMap.put("port", port.toString());
		configMap.put("host", host);
		
		System.out.format("I> Now hosting jolokia json metrics on port: %s.%n", configMap.get("port"));
		JolokiaServerConfig config = new JolokiaServerConfig(configMap);
		JolokiaServer server = new JolokiaServer(config, true);
		server.start();
		
	}
	
	private static Integer getFreePort() throws IllegalStateException {
		for (int port = 7777; port < 8888; port++) {
			try { // attempt to bind the port.
				ServerSocket socket = new ServerSocket();
				socket.bind(new InetSocketAddress("localhost", port));
				socket.close();
				return port;
			} catch (IOException e) {
				System.err.format("Cannot bind to port %d, trying next port...%n", port);
			}
		}
		
		throw new IllegalStateException("I> No available port in range: <7777, 8888>");
	}
	
}
