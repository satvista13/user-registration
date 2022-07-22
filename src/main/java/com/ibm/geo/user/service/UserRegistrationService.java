package com.ibm.geo.user.service;

import static com.ibm.geo.user.service.UserRegistrationUtil.mapToResult;

import com.ibm.geo.user.dto.GeoLocationResponse;
import com.ibm.geo.user.dto.User;
import com.ibm.geo.user.dto.UserRegistrationError;
import com.ibm.geo.user.dto.UserRegistrationSuccess;
import io.vavr.control.Either;
import java.util.UUID;
import java.util.function.Function;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

/**
 * Service to help register a given user
 * @author Sathish Raghupathy
 */
@Service
public class UserRegistrationService {

	private GeoLocationService geoLocationService;

	public UserRegistrationService(GeoLocationService geoLocationService){
		this.geoLocationService = geoLocationService;
	}



	/**
	 * Method which accepts a validate User and returns a Mono of the User Registration Result
	 * Retrieves the GeoLocationResponse and maps it to required result
	 * @param user User
	 * @return Mono<Either<UserRegistrationError,UserRegistrationSuccess>>
	 */
	public Mono<Either<UserRegistrationError,UserRegistrationSuccess>> registerUser(User user){
		return geoLocationService.getByIp(user.getIp())
				.map(geolocationResponse ->mapToResult(user,geolocationResponse));


	}
}
