package com.ibm.geo.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO modelling error response from Geolocation API
 * @author Sathish Raghupathy
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRegistrationError {
	private String error;
}
