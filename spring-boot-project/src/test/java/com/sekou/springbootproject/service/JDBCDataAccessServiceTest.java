package com.sekou.springbootproject.service;

import com.sekou.springbootproject.AbstractUniteTestContainer;
import com.sekou.springbootproject.customers.Customer;
import com.sekou.springbootproject.customers.CustomerRowMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


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
    void ReturnEmptyWhenSelectCustomerById() {
        // Given
        int id = -1;

        // When
        var actual = underTest.selectCustomerById(id);

        // Then
        assertThat(actual).isEmpty();
    }

    @Test
    void insertCustomer() {
    }

    @Test
    void existsPersonWithEmail() {
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        String name = FAKER.name().fullName();
        Customer customer = new Customer(
                name,
                email,
                20
        );
        underTest.insertCustomer(customer);

        boolean actual = underTest.existsPersonWithEmail(email);

        assertThat(actual).isTrue();
    }

    @Test
    void exitsPersonWithReturnFalseWhenDoesNotExist(){
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();

        boolean actual = underTest.existsPersonWithEmail(email);

        assertThat(actual).isFalse();
    }

    @Test
    void existsPersonWithId() {
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        Customer customer = new Customer(
                FAKER.name().fullName(),
                email,
                20
        );
        underTest.insertCustomer(customer);

        int id = underTest.selectAllCustomers()
                .stream().filter(c->c.getEmail().equals(email))
                .map(Customer::getId).findFirst().orElseThrow();

        //When
        var actual = underTest.existsPersonWithId(id);

        // Then

        assertThat(actual).isTrue();
    }

    @Test
    void existTPersonWithIdWillReturnFalseIdNotPresent() {
        //Given
        int id = -1;
        //When
        var actual = underTest.existsPersonWithId(id);

        // Then
        assertThat(actual).isFalse();

    }

    @Test
    void deleteCustomerById() {
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        Customer customer = new Customer(
                FAKER.name().fullName(),
                email,
                20
        );
        underTest.insertCustomer(customer);

        int id = underTest.selectAllCustomers()
                .stream().filter(c -> c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst().orElseThrow();

        // When
        underTest.deleteCustomerById(id);

        //Then
        Optional<Customer>actual = underTest.selectCustomerById(id);
        assertThat(actual).isNotPresent();
    }

    @Test
    void updateCustomer() {
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        Customer customer = new Customer(
                FAKER.name().fullName(),
                email,
                20
        );
        underTest.insertCustomer(customer);

        int id = underTest.selectAllCustomers()
                .stream().filter(c-> c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst().orElseThrow();

        var newName = "house";
        //When
        Customer update = new Customer();
        update.setId(id);
        update.setName(newName);

        underTest.updateCustomer(update);

        //Then
        Optional<Customer> actual = underTest.selectCustomerById(id);
        assertThat(actual).isPresent().hasValueSatisfying(c -> {
            assertThat(c.getId()).isEqualTo(id);
            assertThat(c.getName()).isEqualTo(newName);
            assertThat(c.getEmail()).isEqualTo(customer.getEmail());
            assertThat(c.getAge()).isEqualTo(customer.getAge());
        });
    }

    @Test
    void updateCustomerEmail() {
        // Given
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        Customer customer = new Customer(
                FAKER.name().fullName(),
                email,
                20
        );

        underTest.insertCustomer(customer);

        int id = underTest.selectAllCustomers()
                .stream().filter(c ->c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst().orElseThrow();

        var newEmail = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();

        // When
        Customer update = new Customer();
        update.setId(id);
        update.setEmail(newEmail);

        underTest.updateCustomer(update);

        // Then
        Optional<Customer>  actual = underTest.selectCustomerById(id);

        assertThat(actual).isPresent().hasValueSatisfying(c -> {
            assertThat(c.getId()).isEqualTo(id);
            assertThat(c.getEmail()).isEqualTo(newEmail);
            assertThat(c.getName()).isEqualTo(customer.getName());
            assertThat(c.getAge()).isEqualTo(customer.getAge());
        });
    }
    @Test
    void updateCustomerAge() {
        // Given
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        Customer customer = new Customer(
                FAKER.name().fullName(),
                email,
                20
        );
        underTest.insertCustomer(customer);

        int id = underTest.selectAllCustomers()
                .stream().filter(c -> c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst().orElseThrow();

        var newAge = 99;

        //When
        Customer update = new Customer();
        update.setId(id);
        update.setAge(newAge);

        underTest.updateCustomer(update);

        // Then
        Optional<Customer> actual = underTest.selectCustomerById(id);
        assertThat(actual).isPresent().hasValueSatisfying(c -> {
            assertThat(c.getId()).isEqualTo(id);
            assertThat(c.getName()).isEqualTo(customer.getName());
            assertThat(c.getEmail()).isEqualTo(customer.getEmail());
            assertThat(c.getAge()).isEqualTo(newAge);
        });
    }

    @Test
    void willUpdateAllPropertiesCustomer() {
        // Given
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        Customer customer = new Customer(
                FAKER.name().fullName(),
                email,
                20
        );

        underTest.insertCustomer(customer);

        int id = underTest.selectAllCustomers()
                .stream().filter(c-> c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst().orElseThrow();

        // When update with new name, age and email
        Customer update = new Customer();
        update.setId(id);
        update.setName("house");
        update.setEmail(UUID.randomUUID().toString());
        update.setAge(21);

        underTest.updateCustomer(update);

        //Then
        Optional<Customer> actual = underTest.selectCustomerById(id);
        assertThat(actual).isPresent().hasValue(update);
    }

    @Test
    void willNotUpdateWhenNothingToUpdate() {
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        Customer customer = new Customer(
                FAKER.name().fullName(),
                email,
                20
        );
        underTest.insertCustomer(customer);

        int id = underTest.selectAllCustomers()
                .stream().filter(c-> c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst().orElseThrow();

        Customer update = new Customer();
        update.setId(id);

        underTest.updateCustomer(update);

        Optional<Customer> actual = underTest.selectCustomerById(id);
        assertThat(actual).isPresent().hasValueSatisfying(c -> {
            assertThat(c.getId()).isEqualTo(id);
            assertThat(c.getName()).isEqualTo(customer.getName());
            assertThat(c.getEmail()).isEqualTo(customer.getEmail());
            assertThat(c.getAge()).isEqualTo(customer.getAge());
        });
    }
}