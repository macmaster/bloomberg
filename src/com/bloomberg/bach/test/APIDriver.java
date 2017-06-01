package com.bloomberg.bach.test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.json.JSONObject;

import com.bloomberg.bach.api.ElevationRequest;
import com.bloomberg.bach.hbase.Location;
import com.bloomberg.bach.hbase.LocationTable;

/**
 * @author Ronald Macmaster
 * Test driver to test the Google API classes.
 */
public class APIDriver {
	
	public static void main(String[] args) throws IOException {
		testLocationTablePut("input/wyoming.txt");
	}
	
	public static void testLocationTablePut(String filename) throws IOException {
		
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
	
	public static void testLocationTableGet() throws IOException {
		
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
	
	public static void testElevationAPI() {
		ElevationRequest elevationRequest = new ElevationRequest();
		System.out.println(elevationRequest.get(39.0392, 125.7625)); // pyongyang
	}
	
}
