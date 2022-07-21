package com.ibm.geo.user.service;

import static org.junit.jupiter.api.Assertions.*;

import com.ibm.geo.user.client.GeoLocationResponse;
import com.ibm.geo.user.dto.User;
import com.ibm.geo.user.dto.UserRegistrationError;
import com.ibm.geo.user.dto.UserRegistrationSuccess;
import io.vavr.control.Either;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class UserRegistrationServiceTest {

	@Autowired
	UserRegistrationService userRegistrationService;

	@Test
	void testUser(){
		User user = new User("name","password","24.48.0.1");
		GeoLocationResponse block = userRegistrationService.registerUser(user).block();
		System.out.println(block.toString());
	}

	@Test
	void testUserMap(){
		User user = new User("name","password","24.48.0.1");
		Either<UserRegistrationError, UserRegistrationSuccess> block = userRegistrationService.registerUserMap(user).block();
		System.out.println(block.isRight());
		System.out.println(block.get());
	}

	@Test
	void testUser_error(){
		User user = new User("name","password","100.100.100.100");
		GeoLocationResponse block = userRegistrationService.registerUser(user).block();
		System.out.println(block==null);
	}

	@Test
	void testUser_errorMap(){
		User user = new User("name","password","100.100.100.100");
		Either<UserRegistrationError, UserRegistrationSuccess> block = userRegistrationService.registerUserMap(user).block();
		System.out.println(block.isLeft());
		System.out.println(block.getLeft());
	}
}