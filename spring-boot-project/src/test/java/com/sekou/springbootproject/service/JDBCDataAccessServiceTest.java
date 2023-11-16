package com.sekou.springbootproject.service;

import com.sekou.springbootproject.AbstractUniteTestContainer;
import com.sekou.springbootproject.customers.Customer;
import com.sekou.springbootproject.customers.CustomerRowMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;


class JDBCDataAccessServiceTest extends AbstractUniteTestContainer {

    private JDBCDataAccessService underTest;
    private final CustomerRowMapper customerRowMapper = new CustomerRowMapper();

    @BeforeEach
    void setUp() {
        underTest = new JDBCDataAccessService(
                getJdbcTemplate(),
                customerRowMapper
        );
    }

    @Test
    void selectAllCustomers() {
        // Given
        Customer customer = new Customer(
                FAKER.name().fullName(),
                FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID(),
                20
        );
        underTest.insertCustomer(customer);

        //When
        List <Customer> actual = underTest.selectAllCustomers();

        //Then
        assertThat(actual).isNotEmpty();

    }

    @Test
    void selectCustomerById() {
        //Given
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        Customer customer = new Customer(
                FAKER.name().fullName(),
                email,
                20
        );
        underTest.insertCustomer(customer);

        int id = underTest.selectAllCustomers()
                .stream()
                .filter(c -> email.equals(c.getEmail())) // Check if email equals, and make sure to handle null cases
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();


        // When
       Optional<Customer> actual = underTest.selectCustomerById(id);

       // Then
        assertThat(actual).isPresent().hasValueSatisfying(c -> {
            assertThat(c.getId()).isEqualTo(id);
            assertThat(c.getName()).isEqualTo(customer.getName());
            assertThat(c.getEmail()).isEqualTo(customer.getEmail());
            assertThat(c.getAge()).isEqualTo(customer.getAge());
        });
    }

    @Test
    void insertCustomer() {
    }

    @Test
    void existsPersonWithEmail() {
    }

    @Test
    void existsPersonWithId() {
    }

    @Test
    void deleteCustomerById() {
    }

    @Test
    void updateCustomer() {
    }
}