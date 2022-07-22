package com.ibm.geo.user.api;

import com.ibm.geo.user.config.UserRegistrationConstants;
import com.ibm.geo.user.dto.User;
import com.ibm.geo.user.dto.UserRegistrationError;
import com.ibm.geo.user.dto.UserRegistrationSuccess;
import com.ibm.geo.user.service.UserRegistrationService;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.vavr.control.Either;
import java.util.function.Function;
import javax.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

/**
 * API to register given User
 */
@OpenAPIDefinition(
		info = @Info(
				title = "User Registration API",
				description = "" +
						"Registers a given user using the Geo Location Service",
				contact = @Contact(
						name = "Sathish Raghupathy",
						url = "https://github.com/satvista13/user-registration",
						email = "satvista@gmail.com"
				))
)
@RestController
@RequestMapping(value = { UserRegistrationConstants.API_BASE})
@Tag(name = "Register User")
public class UserRegistrationController {

	private UserRegistrationService userRegistrationService;

	public UserRegistrationController(UserRegistrationService userRegistrationService){
		this.userRegistrationService = userRegistrationService;
	}



	@Operation(summary = "Register a given User")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Success response containing an UUID for userid"
					+ " and success message",
					content = { @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
							schema = @Schema(implementation = UserRegistrationSuccess.class)) }),
			@ApiResponse(responseCode = "500", description = "Geolocation error. Contains error message",
					content = { @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
							schema = @Schema(implementation = UserRegistrationError.class)) }),
			@ApiResponse(responseCode = "400", description = "Validation Failure. Returns validation failure "
					+ "response",
					content = @Content)
	}
	)
	@PostMapping(value = UserRegistrationConstants.REGISTER_USER_ENDPOINT,produces = { MediaType.APPLICATION_JSON_VALUE })
	public Mono<ResponseEntity<?>> registerUser( @Valid @RequestBody User user){
		return userRegistrationService.registerUser(user)
				.map(mapResponse);
	}

	Function<Either<UserRegistrationError,UserRegistrationSuccess>,ResponseEntity<?>> mapResponse
			= result -> {
		if(result.isLeft())return new ResponseEntity(result.getLeft(), HttpStatus.INTERNAL_SERVER_ERROR);
		else return ResponseEntity.ok(result.get());
	};
}
