package test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Arrays;

import org.apache.hadoop.hbase.client.metrics.ScanMetrics;
import org.apache.log4j.BasicConfigurator;
import org.json.JSONObject;
import org.junit.Test;

import com.bloomberg.bach.api.ElevationRequest;
import com.bloomberg.bach.context.BachMetricsContext;
import com.bloomberg.bach.hbase.Location;
import com.bloomberg.bach.hbase.LocationTable;
import com.bloomberg.bach.hdfs.HDFSFile;

/**
 * @author Ronald Macmaster
 * Test driver to test the Google API classes.
 */
public class APIDriver {
	
	public String filename = "input/texas.txt";
	
	public static void main(String[] args) throws Exception {
		APIDriver driver = new APIDriver();
		BasicConfigurator.configure();
		driver.populate();
	}
	
	public void populate() throws IOException {
		BachMetricsContext.start();
		for (String filename : new String[] { "texas.txt", "hawaii.txt", "new_york.txt", "wyoming.txt" }) {
			this.filename = "input/" + filename;
			testLocationTablePut();
		}
		// testScan();
		testLocationTableGet();
	}
	
	@Test
	public void testMetrics() throws Exception {
		// record locationStrings in HBase Location table.
		LocationTable metricsTable = new LocationTable();
		for (String filename : new String[] { "texas.txt", "hawaii.txt", "new_york.txt", "wyoming.txt", "hawaii.txt" }) {
			filename = "input/" + filename;
			try (FileReader file = new FileReader(filename);
				BufferedReader reader = new BufferedReader(file);) {
				
				String locationString = "";
				while ((locationString = reader.readLine()) != null) {
					Location location = Location.parseLocation(locationString);
					metricsTable.putLocation(location);
				}
				
			} catch (Exception exception) {
				exception.printStackTrace();
			}
			
			metricsTable.scan();
			try (InputStreamReader stream = new InputStreamReader(System.in);
				BufferedReader reader = new BufferedReader(stream);) {
				System.out.println("/*** Location Table Shell ***/");
				// command loop.
				String query = "";
				System.out.print("> ");
				while ((query = reader.readLine()) != null) {
					switch (query) {
						case "exit":
						case "quit":
							System.exit(0);
							metricsTable.close();
						default:
							System.out.println(new JSONObject(metricsTable.getLocation(query)).toString(2));
					}
					System.out.print("> ");
				}
			} catch (Exception exception) {
				exception.printStackTrace();
			} finally {
				metricsTable.close();
			}
			
		}
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
	public void testScan() throws IOException {
		try (LocationTable table = new LocationTable();) {
			// print scan metrics.
			ScanMetrics metrics = table.scan();
			System.out.format("regions: %d%n", metrics.countOfRegions.get());
			System.out.format("rpc calls: %d%n", metrics.countOfRemoteRPCcalls.get());
			System.out.print("\n");
			
			System.out.println("Metrics Map: ");
			System.out.println(Arrays.toString(metrics.getMetricsMap().entrySet().toArray()));
			
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
