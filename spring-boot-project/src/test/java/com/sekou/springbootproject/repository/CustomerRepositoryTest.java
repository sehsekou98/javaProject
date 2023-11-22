package com.sekou.springbootproject.repository;

import com.sekou.springbootproject.AbstractUniteTestContainer;
import com.sekou.springbootproject.customers.Customer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CustomerRepositoryTest  extends AbstractUniteTestContainer {
    @Autowired
    private CustomerRepository underTest;

    @Autowired
    private ApplicationContext applicationContext;

    @BeforeEach
    void setUp() {
    }

    @Test
    void existsCustomerByEmail() {
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        Customer customer = new Customer(
                FAKER.name().fullName(),
                email,
                19
        );
        underTest.save(customer);

        //When
        var actual = underTest.existsCustomerByEmail(email);

        // Then

        assertThat(actual).isTrue();
    }

    @Test
    void existsCustomerByEmailWhenEmailNotPresent() {
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();


        //When
        var actual = underTest.existsCustomerByEmail(email);

        // Then

        assertThat(actual).isFalse();
    }



    @Test
    void existsCustomerById() {
        //Given
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        Customer customer = new Customer(
                FAKER.name().fullName(),
                email,
                20
        );
        underTest.save(customer);

        int id = underTest.findAll()
                .stream()
                .filter(c -> email.equals(c.getEmail())) // Check if email equals, and make sure to handle null cases
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();


        // When
        var actual = underTest.existsCustomerById(id);

         assertThat(actual).isTrue();
    }
    @Test
    void existsCustomerByIdWhenIdNotPresent() {
        //Given
        int id = -1;

        // When
        var actual = underTest.existsCustomerById(id);

        assertThat(actual).isFalse();
    }
}