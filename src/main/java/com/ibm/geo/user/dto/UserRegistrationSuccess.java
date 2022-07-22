package com.ibm.geo.user.dto;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO modelling Success response from Geolocation API
 * @author Sathish Raghupathy
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRegistrationSuccess {

	private UUID userId;
	private String message;
}
