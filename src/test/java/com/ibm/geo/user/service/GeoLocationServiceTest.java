package com.ibm.geo.user.service;

import com.ibm.geo.user.client.GeoLocationResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Mono;

@SpringBootTest
class GeoLocationServiceTest {

	@Autowired
	GeoLocationService geoLocationService;
	@Test
	void testGetByIp(){
		Mono<GeoLocationResponse> byIp = geoLocationService.getByIp("24.48.0.1");
		GeoLocationResponse block = byIp.block();
		System.out.println(block.toString());
	}

	@Test
	void testGetByIp_error(){
		Mono<GeoLocationResponse> byIp = geoLocationService.getByIp("100.100.100.100");
		GeoLocationResponse block = byIp.block();
		System.out.println(block.toString());
	}

}