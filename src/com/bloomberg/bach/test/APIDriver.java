package com.bloomberg.bach.test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Map;

import javax.management.MalformedObjectNameException;

import org.jolokia.client.J4pClient;
import org.jolokia.client.exception.J4pException;
import org.jolokia.client.request.J4pReadRequest;
import org.jolokia.client.request.J4pReadResponse;
import org.json.JSONObject;
import org.junit.Test;

import com.bloomberg.bach.api.ElevationRequest;
import com.bloomberg.bach.hbase.Location;
import com.bloomberg.bach.hbase.LocationTable;
import com.bloomberg.bach.hdfs.HDFSFile;

/**
 * @author Ronald Macmaster
 * Test driver to test the Google API classes.
 */
public class APIDriver {
	
	public String filename = "input/texas.txt";
	
	public static void main(String[] args) throws IOException {
		APIDriver driver = new APIDriver();
		for (String filename : new String[] {
				"texas.txt", "hawaii.txt", "new_york.txt", "wyoming.txt"
		}) {
			driver.filename = "input/" + filename;
			driver.testLocationTablePut();
		}
		
		driver.testLocationTableGet();
		
		//		new Thread(() -> {
		//			try { // collect a jolokia stat
		//				J4pClient j4pClient = new J4pClient("http://localhost:8080/jolokia");
		//				J4pReadRequest req = new J4pReadRequest("java.lang:type=Memory", "HeapMemoryUsage");
		//				for (int i = 0; i < 50; i++) {
		//					J4pReadResponse resp = j4pClient.execute(req);
		//					Map<String, Long> vals = resp.getValue();
		//					long used = vals.get("used");
		//					long max = vals.get("max");
		//					int usage = (int) (used * 100 / max);
		//					System.out.println("Memory usage: used: " + used + " / max: " + max + " = " + usage + "%");
		//					Thread.sleep(500);
		//				}
		//			} catch (Exception e) {
		//				e.printStackTrace();
		//			}
		//		}).start();
	}
	
	@Test
	public void testHDFSFile() throws Exception {
		
		try (HDFSFile input = new HDFSFile("/user/hadoop/cities/hawaii.txt");
			HDFSFile output = new HDFSFile("/user/ronny/copy.txt");
			BufferedReader reader = new BufferedReader(input.getStreamReader());
			PrintWriter writer = new PrintWriter(output.getStreamWriter())) {
			
			String line = ""; // copy input to output.
			while ((line = reader.readLine()) != null) {
				writer.println(line);
			}
		} catch (IOException exception) {
			throw exception;
		}
		
	}
	
	@Test
	public void testLocationTablePut() throws IOException {
		
		// record locationStrings in HBase Location table.
		try (FileReader file = new FileReader(filename);
			BufferedReader reader = new BufferedReader(file);
			LocationTable table = new LocationTable();) {
			
			String locationString = "";
			while ((locationString = reader.readLine()) != null) {
				Location location = Location.parseLocation(locationString);
				table.putLocation(location);
				
			}
			
		} catch (Exception exception) {
			exception.printStackTrace();
		}
		
	}
	
	// @Ignore
	@Test
	public void testLocationTableGet() throws IOException {
		
		// record locationStrings in HBase Location table.
		try (InputStreamReader stream = new InputStreamReader(System.in);
			BufferedReader reader = new BufferedReader(stream);
			LocationTable table = new LocationTable();) {
			
			System.out.println("/*** Location Table Shell ***/");
			
			// command loop.
			String query = "";
			System.out.print("> ");
			while ((query = reader.readLine()) != null) {
				switch (query) {
					case "exit":
					case "quit":
						System.exit(0);
					default:
						System.out.println(new JSONObject(table.getLocation(query)).toString(2));
				}
				System.out.print("> ");
			}
			
		} catch (Exception exception) {
			exception.printStackTrace();
		}
		
	}
	
	@Test
	public void testElevationAPI() {
		ElevationRequest elevationRequest = new ElevationRequest();
		System.out.println(elevationRequest.get(27.986065, 86.922623)); // pyongyang
	}
	
}
