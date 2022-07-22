package com.ibm.geo.user.service;

import com.ibm.geo.user.dto.GeoLocationResponse;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

/**
 * Service class to communicate with Geolocation API
 * @author Sathish Raghupathy
 */
@Service
public class GeoLocationService {


	@Qualifier("geolocationClient")
	private WebClient geolocationClient;

	public GeoLocationService(WebClient geoLocationClient){
		this.geolocationClient = geoLocationClient;
	}

	/**
	 * Makes Asynchronous Non-Blocking REST call to Geo location Service
	 * and retrieves the response
	 * @param ip ip address
	 * @return Mono GeoLocationResponse
	 */
	public Mono<GeoLocationResponse> getByIp(String ip) {

		return geolocationClient.get()
				.uri(ip)
				.retrieve()
				.bodyToMono(GeoLocationResponse.class);
	}

}
