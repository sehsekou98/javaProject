package com.sekou.springbootproject.journey;

import com.github.javafaker.Faker;
import com.github.javafaker.Name;
import com.sekou.springbootproject.customers.Customer;
import com.sekou.springbootproject.customers.CustomerRegistrationRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Random;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
public class CustomerIntegrationTest {

    @Autowired
    private WebTestClient webTestClient;

    private static  final Random RANDOM = new Random();
    private static final String Customer_URI = "/api/v1/customers";

    @Test
    void canRegisterCustomer() {
        // create register request
        Faker faker = new Faker();
        Name fakerName = faker.name();
        String name = fakerName.fullName();
        String email = fakerName.firstName() + "-" + UUID.randomUUID() + "@gmail.com";
        int age = RANDOM.nextInt(1, 100);
        CustomerRegistrationRequest request = new CustomerRegistrationRequest(
            name, email, age
        );

        // send a post request
        webTestClient.post()
                .uri(Customer_URI)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(request),CustomerRegistrationRequest.class)
                .exchange()
                .expectStatus()
                .isOk();

        // get all customer
       List<Customer> allCustomer = webTestClient.get()
                .uri(Customer_URI)
               .accept(MediaType.APPLICATION_JSON)
               .exchange()
               .expectStatus()
               .isOk()
               .expectBodyList(new ParameterizedTypeReference<Customer>() {
               })
               .returnResult()
               .getResponseBody();

       Customer expectedCustomer = new Customer(
               name, email, age
       );

       assertThat(allCustomer)
               .usingRecursiveFieldByFieldElementComparatorIgnoringFields("id")
               .contains(expectedCustomer);

    }
}
