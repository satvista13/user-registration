package com.ibm.geo.user.api;

import com.ibm.geo.user.config.UserRegistrationConstants;
import com.ibm.geo.user.dto.User;
import com.ibm.geo.user.dto.UserRegistrationError;
import com.ibm.geo.user.dto.UserRegistrationSuccess;
import com.ibm.geo.user.service.UserRegistrationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.vavr.control.Either;
import java.util.function.Function;
import javax.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(value = { UserRegistrationConstants.API_BASE})
public class UserRegistrationController {

	private UserRegistrationService userRegistrationService;
	public UserRegistrationController(UserRegistrationService userRegistrationService){
		this.userRegistrationService = userRegistrationService;
	}

	Function<Either<UserRegistrationError,UserRegistrationSuccess>,ResponseEntity<?>> mapResponse
			= result -> {
		if(result.isLeft())return ResponseEntity.ok(result.getLeft());
		else return ResponseEntity.ok(result.get());
	};

	@Operation(summary = "Register a given User")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "success",
					content = { @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
							schema = @Schema(implementation = UserRegistrationSuccess.class)) }),
			@ApiResponse(responseCode = "500", description = "Geolocation error",
					content = { @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
							schema = @Schema(implementation = UserRegistrationError.class)) }),
			@ApiResponse(responseCode = "400", description = "Validation Failure",
					content = @Content)
	}
	)
	@PostMapping(value = UserRegistrationConstants.REGISTER_USER_ENDPOINT,produces = { MediaType.APPLICATION_JSON_VALUE })
	public Mono<ResponseEntity<?>> registerUser( @Valid @RequestBody User user){
		return userRegistrationService.registerUserMap(user)
				.map(mapResponse);
	}
}
