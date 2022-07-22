package com.ibm.geo.user.dto;

import lombok.Data;

/**
 * DTO modelling the REST API response from Geolocation API
 * Captures on the required fields from the JSON response. Additional
 * fields can be added as required
 * @author Sathish Raghupathy
 */
@Data
public class GeoLocationResponse {

	private String status;
	private String country;
	private String countryCode;
	private String city;
	private String message;
}
