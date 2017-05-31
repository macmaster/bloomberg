package com.bloomberg.bach.test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import com.bloomberg.bach.api.ElevationRequest;
import com.bloomberg.bach.hbase.Location;
import com.bloomberg.bach.hbase.LocationTable;

/**
 * @author Ronald Macmaster
 * Test driver to test the Google API classes.
 */
public class APIDriver {
	
	public static void main(String[] args) throws IOException {
		testLocationTable("input/hawaii.txt");
	}
	
	public static void testLocationTable(String filename) throws IOException {
		
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
	
	public static void testElevationAPI() {
		ElevationRequest elevationRequest = new ElevationRequest();
		System.out.println(elevationRequest.get(39.0392, 125.7625)); // pyongyang
	}
	
}
