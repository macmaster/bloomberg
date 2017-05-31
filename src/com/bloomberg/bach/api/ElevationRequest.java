package com.bloomberg.bach.api;

import java.io.IOException;

import org.apache.http.client.fluent.Request;

/**
 * @author Ronald Macmaster
 * TODO document
 */
public class ElevationRequest implements GoogleRequest {
	
	/**
	 * locations: (double latitude, double longitude) <br>
	 * key : String API_KEY
	 */
	private static final String API_STRING = "https://maps.googleapis.com/maps/api/elevation/json?locations=%f,%f&key=%s";
	
	/* (non-Javadoc)
	 * @see distributed.GoogleRequest#apiFormatString()
	 */
	@Override
	public String apiFormatString() {
		return API_STRING;
	}
	
	/**
	 * Returns a JSON string with the response from the maps.google elevation API. <br>
	 * provide (double latitude, double longitude).
	 */
	public String get(double latitude, double longitude){
		try { // send HTTP Get to google api.
			String requestString = String.format(API_STRING, latitude, longitude, API_KEY);
			String responseString = Request.Get(requestString).viaProxy(PROXY).execute().returnContent().asString();
			return responseString;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
}
