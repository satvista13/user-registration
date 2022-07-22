package com.ibm.geo.user.service;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.ibm.geo.user.dto.GeoLocationResponse;
import com.ibm.geo.user.dto.User;
import com.ibm.geo.user.dto.UserRegistrationError;
import com.ibm.geo.user.dto.UserRegistrationSuccess;
import io.vavr.control.Either;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.mockito.Mock;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@TestInstance(Lifecycle.PER_CLASS)
class UserRegistrationServiceTest {

	UserRegistrationService userRegistrationService;

	@Mock
	GeoLocationService geoLocationService;

	@BeforeAll
	void setUp() {
		geoLocationService = mock(GeoLocationService.class);
		userRegistrationService = new UserRegistrationService(geoLocationService);
	}


	@Test
	void registerUser_validIP_success() {

		// Given
		GeoLocationResponse geoLocationResponse = new GeoLocationResponse();
		geoLocationResponse.setCountry("Canada");
		geoLocationResponse.setCity("Montreal");
		geoLocationResponse.setMessage(null);
		when(geoLocationService.getByIp("24.48.0.1")).thenReturn(Mono.just(geoLocationResponse));
		User user = new User("name", "password", "24.48.0.1");

		// When
		Mono<Either<UserRegistrationError, UserRegistrationSuccess>> mono = userRegistrationService.registerUser(user);

		// Then
		StepVerifier.create(mono)
				.assertNext(either -> {
					assertTrue(either.isRight());
					assertNotNull(either.get().getUserId());
					assertNotNull(either.get().getMessage());
				})
				.verifyComplete();

	}

	@Test
	void registerUser_invalidIP_failure() {
		// Given
		GeoLocationResponse geoLocationResponse = new GeoLocationResponse();
		geoLocationResponse.setMessage("error message");
		when(geoLocationService.getByIp("100.100.100.100")).thenReturn(Mono.just(geoLocationResponse));
		User user = new User("name", "password", "100.100.100.100");

		// When
		Mono<Either<UserRegistrationError, UserRegistrationSuccess>> mono = userRegistrationService.registerUser(user);

		// Then
		StepVerifier.create(mono)
				.assertNext(either -> {
					assertTrue(either.isLeft());
					assertNotNull(either.getLeft().getError());
				})
				.verifyComplete();
	}
}