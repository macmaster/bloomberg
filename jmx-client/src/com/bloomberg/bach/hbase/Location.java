package com.bloomberg.bach.hbase;


/**
 * @author Ronald Macmaster
 * Model Object for a City Location.
 */
public class Location {
	
	public String countryCode;
	public String asciiName;
	public String cityName;
	public String region;
	public String population;
	public String latitude;
	public String longitude;
	
	public static Location parseLocation(String locationString){
		if (locationString == null)
			return null;
		
		try{
			Location location = new Location();
			String[] fields = locationString.split(",");
			location.setCountryCode(fields[0]);
			location.setAsciiName(fields[1]);
			location.setCityName(fields[2]);
			location.setRegion(fields[3]);
			location.setPopulation(fields[4]);
			location.setLatitude(fields[5]);
			location.setLongitude(fields[6]);
			return location;
		} catch (Exception err) {
			System.err.format("failed to parse location string: \"%s\"%n", locationString);
			return null;
		}
		
	}

	public String getRowTag(){
		return String.format("%s-%s", asciiName, region);
	}
	
	/**
	 * @return the countryCode
	 */
	public String getCountryCode() {
		return countryCode;
	}

	
	/**
	 * @param countryCode the countryCode to set
	 */
	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	
	/**
	 * @return the asciiName
	 */
	public String getAsciiName() {
		return asciiName;
	}

	
	/**
	 * @param asciiName the asciiName to set
	 */
	public void setAsciiName(String asciiName) {
		this.asciiName = asciiName;
	}

	
	/**
	 * @return the cityName
	 */
	public String getCityName() {
		return cityName;
	}

	
	/**
	 * @param cityName the cityName to set
	 */
	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	
	/**
	 * @return the region
	 */
	public String getRegion() {
		return region;
	}

	
	/**
	 * @param region the region to set
	 */
	public void setRegion(String region) {
		this.region = region;
	}

	
	/**
	 * @return the population
	 */
	public String getPopulation() {
		return population;
	}

	
	/**
	 * @param population the population to set
	 */
	public void setPopulation(String population) {
		this.population = population;
	}

	
	/**
	 * @return the latitude
	 */
	public String getLatitude() {
		return latitude;
	}

	
	/**
	 * @param latitude the latitude to set
	 */
	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	
	/**
	 * @return the longitude
	 */
	public String getLongitude() {
		return longitude;
	}

	
	/**
	 * @param longitude the longitude to set
	 */
	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}
	
	
	
	
}
