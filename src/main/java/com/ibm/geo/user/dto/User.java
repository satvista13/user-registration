package com.ibm.geo.user.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO modelling the Request Body from User Registration API
 * Does Javax Validation on required fields returning 400
 * in case of validation failure
 * @author Sathish Raghupathy
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

	private static final String passwordPattern = "^(?=.*[0-9])(?=.*[A-Z])(?=.*[_#$%.]).{8}$";
	@NotBlank(message = "Name cannot be blank")
	private String name;
	@NotBlank(message = "Password cannot be blank")
	@Pattern(regexp = passwordPattern, message = " Password need to be greater than 8 characters, containing at least 1 number, 1 Capitalized letter, 1 special character in this set [_ # $ % .]")
	private String password;
	@NotBlank(message = "IP cannot be blank")
	private String ip;

}
