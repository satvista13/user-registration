package com.ibm.geo.user.service;

import com.ibm.geo.user.dto.GeoLocationResponse;
import com.ibm.geo.user.dto.User;
import com.ibm.geo.user.dto.UserRegistrationError;
import com.ibm.geo.user.dto.UserRegistrationSuccess;
import io.vavr.control.Either;
import java.util.UUID;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Class containing the core business logic for User Registration
 * @author Sathish Raghupathy
 */
public class UserRegistrationUtil {

	/**
	 * Checks if response from Geo Location Service is success or not.
	 * 'message' in GeoLocationResponse will be empty in case of Success
	 * Refer: https://ip-api.com/docs/api:json
	 */
	public static Predicate<GeoLocationResponse> validGeoLocation = geoLocationResponse ->
			geoLocationResponse.getMessage()==null;
	/**
	 * Checks if IP range is in Canada
	 * Refer: https://ip-api.com/docs/api:json
	 */
	public static Predicate<GeoLocationResponse> locatedInCanada = geoLocationResponse ->
			geoLocationResponse.getCountry().equalsIgnoreCase("canada");

	/**
	 * Method to map Success response for a given User
	 * @param user User
	 * @return Function which accepts GeoLocationResponse and returns UserRegistrationSuccess
	 */
	public static Function<GeoLocationResponse, UserRegistrationSuccess> mapSuccessResponse(User user){
		return geoLocationResponse -> new UserRegistrationSuccess(UUID.randomUUID(),
				"User "+user.getName() +" registered successfully in city: "+geoLocationResponse.getCity());
	}

	/**
	 * Method which returns the Sum type of UserRegistrationError and UserRegistrationSuccess
	 * based on business rules
	 * @param user User
	 * @param geoLocationResponse GeoLocationResponse
	 * @return Either of UserRegistrationError or UserRegistrationSuccess
	 */
	public static Either<UserRegistrationError,UserRegistrationSuccess> mapToResult(User user,GeoLocationResponse geoLocationResponse)  {
		if(validGeoLocation.and(locatedInCanada).test(geoLocationResponse))
			return Either.right(mapSuccessResponse(user).apply(geoLocationResponse));
		else
			return Either.left( new UserRegistrationError("User is not eligible to register"));
	};
}
