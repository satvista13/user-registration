package com.ibm.geo.user.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.config.EnableWebFlux;
import org.springframework.web.reactive.config.WebFluxConfigurer;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * Configures the REST Client for GeoLocation Service.
 * Returns a Webclient which enables to perform Asynchronous
 * non-blocking calls to Geolocation API
 */
@Configuration
@EnableWebFlux
@Slf4j
public class GeoLocationClientConfig implements WebFluxConfigurer
{

	@Value("${geolocation.api.endpoint}")
	private String GEOLOCATION_ENDPOINT;


	@Bean
	@Qualifier("geolocationClient")
	public WebClient getWebClient()
	{
		return WebClient.builder()
		        .baseUrl(GEOLOCATION_ENDPOINT)
		        .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
		        .build();
	}
}