package com.bloomberg.bach.test;

import com.bloomberg.bach.api.ElevationRequest;

/**
 * @author Ronald Macmaster
 * Test driver to test the Google API classes.
 */
public class APIDriver {
	
	public static void main(String[] args){
		ElevationRequest elevationRequest = new ElevationRequest();
		System.out.println(elevationRequest.get(39.0392, 125.7625)); // pyongyang
	}
	
}
