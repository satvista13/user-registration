package com.ibm.geo.user.service;

import com.ibm.geo.user.client.GeoLocationResponse;
import com.ibm.geo.user.dto.User;
import com.ibm.geo.user.dto.UserRegistrationError;
import com.ibm.geo.user.dto.UserRegistrationSuccess;
import io.vavr.control.Either;
import java.util.UUID;
import java.util.function.Function;
import java.util.function.Predicate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class UserRegistrationService {

	private GeoLocationService geoLocationService;

	public UserRegistrationService(GeoLocationService geoLocationService){
		this.geoLocationService = geoLocationService;
	}

	static Predicate<GeoLocationResponse> validGeoLocation = geoLocationResponse ->
			geoLocationResponse.getMessage()==null;

	static Predicate<GeoLocationResponse> locatedInCanada = geoLocationResponse ->
			geoLocationResponse.getCountry().equalsIgnoreCase("canada");

	public Function<GeoLocationResponse,UserRegistrationSuccess> resultMapperForUser(User user){
              return geoLocationResponse -> new UserRegistrationSuccess(UUID.randomUUID(),
					  "User "+user.getName() +" registered successfully in city: "+geoLocationResponse.getCity());
	}

	 Either<UserRegistrationError,UserRegistrationSuccess> mapToResult(User user,GeoLocationResponse geoLocationResponse)  {
		if(validGeoLocation.and(locatedInCanada).test(geoLocationResponse))
			return Either.right(resultMapperForUser(user).apply(geoLocationResponse));
		else
			return Either.left( new UserRegistrationError("User is not eligible to register"));
	};

	static Function<GeoLocationResponse, Either<UserRegistrationError,UserRegistrationSuccess>> mapToResult = geoLocationResponse -> {
		if(validGeoLocation.and(locatedInCanada).test(geoLocationResponse))
			return Either.right(new UserRegistrationSuccess(UUID.randomUUID(),"success message"));
		else
			return Either.left( new UserRegistrationError("User is not eligible to register"));
	};

	public Mono<GeoLocationResponse> registerUser(User user){
		return geoLocationService.getByIp(user.getIp())
				.filter(validGeoLocation.and(locatedInCanada));


	}

	public Mono<Either<UserRegistrationError,UserRegistrationSuccess>> registerUserMap(User user){
		return geoLocationService.getByIp(user.getIp())
				.map(geolocationResponse ->mapToResult(user,geolocationResponse));


	}
}
