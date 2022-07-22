package com.ibm.geo.user.service;

import static com.ibm.geo.user.service.UserRegistrationUtil.locatedInCanada;
import static com.ibm.geo.user.service.UserRegistrationUtil.mapSuccessResponse;
import static com.ibm.geo.user.service.UserRegistrationUtil.mapToResult;
import static com.ibm.geo.user.service.UserRegistrationUtil.validGeoLocation;
import static org.junit.jupiter.api.Assertions.*;

import com.ibm.geo.user.dto.GeoLocationResponse;
import com.ibm.geo.user.dto.User;
import com.ibm.geo.user.dto.UserRegistrationError;
import com.ibm.geo.user.dto.UserRegistrationSuccess;
import io.vavr.control.Either;
import java.util.function.Function;
import org.junit.jupiter.api.Test;

class UserRegistrationUtilTest {

	@Test
	void validGeoLocation_invalidResponse_false(){
		GeoLocationResponse geoLocationResponse = new GeoLocationResponse();
		geoLocationResponse.setMessage("error");
		assertFalse(validGeoLocation.test(geoLocationResponse));
	}

	@Test
	void validGeoLocation_validResponse_true(){
		GeoLocationResponse geoLocationResponse = new GeoLocationResponse();
		geoLocationResponse.setMessage(null);
		assertTrue(validGeoLocation.test(geoLocationResponse));
	}

	@Test
	void locatedInCanada_outsideCanada_false(){
		GeoLocationResponse geoLocationResponse = new GeoLocationResponse();
		geoLocationResponse.setCountry("United States");
		assertFalse(locatedInCanada.test(geoLocationResponse));
	}

	@Test
	void locatedInCanada_Canada_true(){
		GeoLocationResponse geoLocationResponse = new GeoLocationResponse();
		geoLocationResponse.setCountry("Canada");
		assertTrue(locatedInCanada.test(geoLocationResponse));
	}

	@Test
	void locatedInCanada_CanadaInDifferentCase_true(){
		GeoLocationResponse geoLocationResponse = new GeoLocationResponse();
		geoLocationResponse.setCountry("canada");
		assertTrue(locatedInCanada.test(geoLocationResponse));
	}

	@Test
	void testMapSuccessResponse() {
		User user = new User();
		user.setName("username");

		GeoLocationResponse geoLocationResponse = new GeoLocationResponse();
		geoLocationResponse.setCity("Toronto");

		Function<GeoLocationResponse, UserRegistrationSuccess> successMapper = mapSuccessResponse(user);

		UserRegistrationSuccess success = successMapper.apply(geoLocationResponse);
		assertNotNull(success.getUserId());
		assertTrue(success.getMessage().contains(user.getName()) &&
				    success.getMessage().contains(geoLocationResponse.getCity()));

	}

	@Test
	void testMapToResult_invalidLocation_returnsUserRegistrationError() {
		GeoLocationResponse geoLocationResponse = new GeoLocationResponse();
		geoLocationResponse.setMessage("error");

		User user = new User();
		user.setName("username");

		Either<UserRegistrationError, UserRegistrationSuccess> result = mapToResult(user, geoLocationResponse);
		assertTrue(result.isLeft());
		assertNotNull(result.getLeft());

	}

	@Test
	void testMapToResult_validLocation_returnsUserRegistrationSuccess() {
		GeoLocationResponse geoLocationResponse = new GeoLocationResponse();
		geoLocationResponse.setMessage(null);
		geoLocationResponse.setCountry("Canada");

		User user = new User();
		user.setName("username");

		Either<UserRegistrationError, UserRegistrationSuccess> result = mapToResult(user, geoLocationResponse);
		assertTrue(result.isRight());
		assertNotNull(result.get().getUserId());
		assertNotNull(result.get().getMessage());

	}
}