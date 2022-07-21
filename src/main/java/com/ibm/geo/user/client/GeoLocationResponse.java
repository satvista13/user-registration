package com.ibm.geo.user.client;

import java.util.Optional;
import lombok.Data;

@Data
public class GeoLocationResponse {

	private String status;
	private String country;
	private String countryCode;
	private String city;
	private String message;
}
