package com.sekou.springbootproject.service;

import com.sekou.springbootproject.customers.Customer;
import com.sekou.springbootproject.customers.CustomerDb;
import com.sekou.springbootproject.customers.CustomerRegistrationRequest;
import com.sekou.springbootproject.exception.ResourceNotFound;
import com.sekou.springbootproject.repository.CustomerRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {
    @Mock
    private CustomerDb customerDb;
    private CustomerService underTest;

    @BeforeEach
    void setUp() {

        underTest = new CustomerService(customerDb);

    }
    
    @Test
    void getAllCustomers() {
        underTest.getAllCustomers();
        
        verify(customerDb).selectAllCustomers();
    }

    @Test
    void canGetCustomer() {
        int id = 10;
        Customer customer = new Customer(
                id, "Sekou", "seh@gmail.com", 18
        );
        when(customerDb.selectCustomerById(id)).thenReturn(Optional.of(customer));

        Customer actual = underTest.getCustomer(10);

        assertThat(actual).isEqualTo(customer);
    }
    @Test
    void willThrowWhenGetCustomerIsEmpty() {
        int id = 10;
        when(customerDb.selectCustomerById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> underTest.getCustomer(id))
                .isInstanceOf(ResourceNotFound.class)
                .hasMessage("customer with id [%s] not found".formatted(id));

    }

    @Test
    void addCustomer() {
        String email = "seh@gmail.com";
        when(customerDb.existsPersonWithEmail(email)).thenReturn(false);

        CustomerRegistrationRequest request = new CustomerRegistrationRequest(
                "Sekou", email, 18
        );
        underTest.addCustomer(request);

        ArgumentCaptor<Customer> customerArgumentCaptor = ArgumentCaptor.forClass(
                Customer.class
        );
        verify(customerDb).insertCustomer(customerArgumentCaptor.capture());

        Customer capturedCustomer = customerArgumentCaptor.getValue();

        assertThat(capturedCustomer.getId()).isNull();
        assertThat(capturedCustomer.getName()).isEqualTo(request.name());
        assertThat(capturedCustomer.getEmail()).isEqualTo(request.email());
        assertThat(capturedCustomer.getAge()).isEqualTo(request.age());

    }

    @Test
    void deleteCustomerById() {
    }

    @Test
    void updateCustomer() {
    }
}