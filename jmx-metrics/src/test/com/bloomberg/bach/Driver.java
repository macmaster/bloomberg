package test.com.bloomberg.bach;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;

import com.bloomberg.bach.context.BachMetricsContext;

/**
 * @author Ronald Macmaster
 * TODO document
 */
public class Driver {
	
	/**
	 * TODO document <br>
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		System.out.println("Metrics Test Driver: main");
		BachMetricsContext.start();
		
		Configuration config = new Configuration();
		String key = "client.graphite.server.server_host";
		System.err.println(String.format("%s : %s %n", key, config.get(key)));
		
		// System.getProperties().list(System.out);
		while (true) {
		}
	}
	
}
