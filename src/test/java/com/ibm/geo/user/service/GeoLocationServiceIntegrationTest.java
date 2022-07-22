package com.ibm.geo.user.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import com.ibm.geo.user.dto.GeoLocationResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@SpringBootTest
class GeoLocationServiceIntegrationTest {

	@Autowired
	GeoLocationService geoLocationService;
	@Test
	void getByIp_canadianIP_successResponse(){
		Mono<GeoLocationResponse> byIp = geoLocationService.getByIp("24.48.0.1");
		StepVerifier.create(byIp)
				.assertNext(geoLocationResponse -> {
					assertEquals("Canada", geoLocationResponse.getCountry());
					assertEquals("Montreal",geoLocationResponse.getCity());
					assertNull(geoLocationResponse.getMessage());
						}
				)
				.verifyComplete();
	}

	@Test
	void getByIp_nonCanadianIP_successResponse(){
		Mono<GeoLocationResponse> byIp = geoLocationService.getByIp("72.229.28.185");
		StepVerifier.create(byIp)
				.assertNext(geoLocationResponse -> {
							assertEquals("United States", geoLocationResponse.getCountry());
							assertEquals("New York",geoLocationResponse.getCity());
							assertNull(geoLocationResponse.getMessage());
						}
				)
				.verifyComplete();
	}

	@Test
	void getByIp_invalidIP_statusFail(){
		Mono<GeoLocationResponse> byIp = geoLocationService.getByIp("100.100.100.100");
		StepVerifier.create(byIp)
				.assertNext(geoLocationResponse -> {
							assertEquals("fail", geoLocationResponse.getStatus());
							assertNotNull(geoLocationResponse.getMessage());
						}
				)
				.expectError();

	}

}