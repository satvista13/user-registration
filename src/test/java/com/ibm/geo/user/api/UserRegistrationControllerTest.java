package com.ibm.geo.user.api;

import static org.mockito.Mockito.when;

import com.ibm.geo.user.config.UserRegistrationConstants;
import com.ibm.geo.user.dto.User;
import com.ibm.geo.user.dto.UserRegistrationError;
import com.ibm.geo.user.dto.UserRegistrationSuccess;
import com.ibm.geo.user.service.UserRegistrationService;
import io.vavr.control.Either;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

@WebFluxTest(UserRegistrationController.class)
public class UserRegistrationControllerTest {

    @Autowired
    WebTestClient webTestClient;

    @MockBean
    private UserRegistrationService userRegistrationService;

    @Test
    public void testGetEmployeeById() {
        User user = new User("name","password","24.48.0.1");
        Mono<Either<UserRegistrationError,UserRegistrationSuccess>> employeeMono = Mono.just(Either.right(new UserRegistrationSuccess(UUID.randomUUID(),"success message")));

        when(userRegistrationService.registerUserMap(user)).thenReturn(employeeMono);

        webTestClient.post()
                .uri(UserRegistrationConstants.API_BASE+UserRegistrationConstants.REGISTER_USER_ENDPOINT)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(user), User.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody(UserRegistrationSuccess.class)
                .consumeWith(System.out::println);


//                .value(employee1 -> employee.getAge(), equalTo(23));
    }

    @Test
    public void testGetEmployeeById_validation() {
        User user = new User("dfsd","13$56Ba","24.48.0.1");
        Mono<Either<UserRegistrationError,UserRegistrationSuccess>> employeeMono = Mono.just(Either.right(new UserRegistrationSuccess(UUID.randomUUID(),"success message")));

        when(userRegistrationService.registerUserMap(user)).thenReturn(employeeMono);

        webTestClient.post()
                .uri(UserRegistrationConstants.API_BASE+UserRegistrationConstants.REGISTER_USER_ENDPOINT)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(user), User.class)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .consumeWith(System.out::println);


        //                .value(employee1 -> employee.getAge(), equalTo(23));
    }

    @Test
    public void testGetEmployeeById_error() {
        User user = new User("name","password","100.100.100.100");
        Mono<Either<UserRegistrationError,UserRegistrationSuccess>> employeeMono = Mono.just(Either.right(new UserRegistrationSuccess(UUID.randomUUID(),"success message")));

        when(userRegistrationService.registerUserMap(user)).thenReturn(employeeMono);

        webTestClient.post()
                .uri(UserRegistrationConstants.API_BASE+UserRegistrationConstants.REGISTER_USER_ENDPOINT)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(user), User.class)
                .exchange()
                .expectStatus().isOk()

                .expectBody(UserRegistrationError.class);
        //                .value(employee1 -> employee.getAge(), equalTo(23));
    }


}