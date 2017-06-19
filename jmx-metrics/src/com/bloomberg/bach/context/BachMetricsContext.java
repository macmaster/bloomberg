package com.bloomberg.bach.context;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.configuration.SubsetConfiguration;
import org.apache.hadoop.metrics2.MetricsSystem;
import org.apache.hadoop.metrics2.lib.DefaultMetricsSystem;
import org.apache.hadoop.metrics2.sink.GraphiteSink;
import org.jolokia.jvmagent.JolokiaServer;
import org.jolokia.jvmagent.JolokiaServerConfig;

import com.bloomberg.bach.metrics.BachMetricsJVM;

/**
 * @author Ronald Macmaster
 * Context Package for Bach Metrics package.
 */
public class BachMetricsContext {
	
	private static volatile BachMetricsContext context;
	
	private static String host = "localhost";
	private static Integer port = getFreePort();
	
	private BachMetricsContext(MetricsSystem system) {
		system.register(new BachMetricsJVM());
	}
	
	public synchronized static BachMetricsContext registerContext(MetricsSystem system) {
		if (context == null) {
			context = new BachMetricsContext(system);
		} // return singleton context.
		return context;
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
	 * Start metrics JSON service on localhost and an arbitrary port.  <br>
	 */
	public static void start() throws IOException {
		System.out.format("Bach Metrics Context: start <%s, %d> %n", host, port);
		
		// initialize metrics system.
		MetricsSystem system = DefaultMetricsSystem.initialize("bach.metrics");
		BachMetricsContext.registerContext(system);
		system.start();
		
		startJolokiaServer();
		
		// debug system properties.
		// System.getProperties().list(System.out);
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
		
		JolokiaServerConfig config = new JolokiaServerConfig(configMap);
		JolokiaServer server = new JolokiaServer(config, true);
		server.start();
		System.out.format("I> Now hosting jolokia json metrics on port: %s.%n", configMap.get("port"));
		
	}
	
	private static void registerGraphiteSink() {
		
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
