package com.bloomberg.bach.api;

import java.io.IOException;

import org.apache.http.HttpHost;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.fluent.Request;

/**
 * @author Ronald Macmaster
 * TODO document
 */
public interface GoogleRequest {
	
	public static final String API_KEY = "AIzaSyDwDZ8pKDljXGax1y1L71hv7ryJcc0GD20";
	public static final HttpHost PROXY = new HttpHost("devproxy.bloomberg.com", 82, "http");
	
	public static void main(String[] args) throws ClientProtocolException, IOException {
		
	}
	
	/**
	 * returns the format string for the API request. <br>
	 * Substitute the parameters with String.format().
	 */
	public String apiFormatString();
	
}
