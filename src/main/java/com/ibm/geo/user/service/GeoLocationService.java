package com.ibm.geo.user.service;

import com.ibm.geo.user.client.GeoLocationResponse;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class GeoLocationService {


	@Qualifier("geolocationClient")
	private WebClient geolocationClient;

	public GeoLocationService(WebClient geoLocationClient){
		this.geolocationClient = geoLocationClient;
	}

	public Mono<GeoLocationResponse> getByIp(String ip) {

		return geolocationClient.get()
				.uri("/"+ip)
				.retrieve()
				.bodyToMono(GeoLocationResponse.class);
	}

}
