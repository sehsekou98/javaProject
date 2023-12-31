package com.sekou.springbootproject.service;

import com.sekou.springbootproject.customers.Customer;
import com.sekou.springbootproject.customers.CustomerDb;
import com.sekou.springbootproject.customers.CustomerRegistrationRequest;
import com.sekou.springbootproject.customers.CustomerUpdateRequest;
import com.sekou.springbootproject.exception.DuplicateResourceException;
import com.sekou.springbootproject.exception.RequestValidationException;
import com.sekou.springbootproject.exception.ResourceNotFound;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;


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
    void willThrowWhenEmailExistAddingCustomer() {
        String email = "seh@gmail.com";
        when(customerDb.existsPersonWithEmail(email)).thenReturn(true);

        CustomerRegistrationRequest request = new CustomerRegistrationRequest(
                "Sekou", email, 18
        );
       assertThatThrownBy(() -> underTest.addCustomer(request))
               .isInstanceOf(DuplicateResourceException.class)
               .hasMessage("email already taken");


        verify(customerDb, never()).insertCustomer(any());


    }

    @Test
    void deleteCustomerById() {
        int id = 10;
        when(customerDb.existsPersonWithId(id)).thenReturn(true);

        underTest.deleteCustomerById(id);

        verify(customerDb).deleteCustomerById(id);
    }
    @Test
    void willThrowWhenDeletedIdNotExistCustomerById() {
        int id = 10;
        when(customerDb.existsPersonWithId(id)).thenReturn(false);

       assertThatThrownBy(() -> underTest.deleteCustomerById(id))
               .isInstanceOf(ResourceNotFound.class)
               .hasMessage("customer with id [%s] not found".formatted(id));

        verify(customerDb, never()).deleteCustomerById(id);
    }

    @Test
    void updateAllCustomerProperties() {
        int id = 10;
        Customer customer = new Customer(
                id, "Sekou", "seh@gmail.com", 18
        );
        when(customerDb.selectCustomerById(id)).thenReturn(Optional.of(customer));

        String newEmail = "kalasco@gmail.com";

        CustomerUpdateRequest updateRequest  = new CustomerUpdateRequest(
                 "Kalasco", newEmail, 24);

        when(customerDb.existsPersonWithEmail(newEmail)).thenReturn(false);

         underTest.updateCustomer(id, updateRequest);

         ArgumentCaptor<Customer> customerArgumentCaptor = ArgumentCaptor.forClass(Customer.class);
         verify(customerDb).updateCustomer(customerArgumentCaptor.capture());
         Customer capturedCustomer = customerArgumentCaptor.getValue();

         assertThat(capturedCustomer.getName()).isEqualTo(updateRequest.name());
        assertThat(capturedCustomer.getEmail()).isEqualTo(updateRequest.email());
        assertThat(capturedCustomer.getAge()).isEqualTo(updateRequest.age());

    }

    @Test
    void updateAllOnlyCustomerName() {
        int id = 10;
        Customer customer = new Customer(
                id, "Sekou", "seh@gmail.com", 18
        );
        when(customerDb.selectCustomerById(id)).thenReturn(Optional.of(customer));


        CustomerUpdateRequest updateRequest  = new CustomerUpdateRequest(
                "Kalasco", null, null);


        underTest.updateCustomer(id, updateRequest);

        ArgumentCaptor<Customer> customerArgumentCaptor = ArgumentCaptor.forClass(Customer.class);
        verify(customerDb).updateCustomer(customerArgumentCaptor.capture());
        Customer capturedCustomer = customerArgumentCaptor.getValue();

        assertThat(capturedCustomer.getName()).isEqualTo(updateRequest.name());
        assertThat(capturedCustomer.getEmail()).isEqualTo(customer.getEmail());
        assertThat(capturedCustomer.getAge()).isEqualTo(customer.getAge());

    }

    @Test
    void updateAllOnlyCustomerEmail() {
        int id = 10;
        Customer customer = new Customer(
                id, "Sekou", "seh@gmail.com", 18
        );
        when(customerDb.selectCustomerById(id)).thenReturn(Optional.of(customer));
        String newEmail = "kalasco@gmail.com";


        CustomerUpdateRequest updateRequest  = new CustomerUpdateRequest(
                null, newEmail, null);


        underTest.updateCustomer(id, updateRequest);

        ArgumentCaptor<Customer> customerArgumentCaptor = ArgumentCaptor.forClass(Customer.class);
        verify(customerDb).updateCustomer(customerArgumentCaptor.capture());
        Customer capturedCustomer = customerArgumentCaptor.getValue();

        assertThat(capturedCustomer.getName()).isEqualTo(customer.getName());
        assertThat(capturedCustomer.getEmail()).isEqualTo(newEmail);
        assertThat(capturedCustomer.getAge()).isEqualTo(customer.getAge());

    }

    @Test
    void updateAllOnlyCustomerAge() {
        int id = 10;
        Customer customer = new Customer(
                id, "Sekou", "seh@gmail.com", 18
        );
        when(customerDb.selectCustomerById(id)).thenReturn(Optional.of(customer));


        CustomerUpdateRequest updateRequest  = new CustomerUpdateRequest(
                null, null, 24);


        underTest.updateCustomer(id, updateRequest);

        ArgumentCaptor<Customer> customerArgumentCaptor = ArgumentCaptor.forClass(Customer.class);
        verify(customerDb).updateCustomer(customerArgumentCaptor.capture());
        Customer capturedCustomer = customerArgumentCaptor.getValue();

        assertThat(capturedCustomer.getName()).isEqualTo(customer.getName());
        assertThat(capturedCustomer.getEmail()).isEqualTo(customer.getEmail());
        assertThat(capturedCustomer.getAge()).isEqualTo(updateRequest.age());

    }

    @Test
    void willThrowWhenCustomerEmailExist() {
        int id = 10;
        Customer customer = new Customer(
                id, "Sekou", "seh@gmail.com", 18
        );
        when(customerDb.selectCustomerById(id)).thenReturn(Optional.of(customer));
        String newEmail = "kalasco@gmail.com";


        CustomerUpdateRequest updateRequest  = new CustomerUpdateRequest(
                null, newEmail, null);

        when(customerDb.existsPersonWithEmail(newEmail)).thenReturn(true);


        assertThatThrownBy(() -> underTest.updateCustomer(id, updateRequest))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessage("email already taken");

        verify(customerDb, never()).updateCustomer(any());

    }

    @Test
    void willThrowWhenCustomerUpdateHasNoChanges() {
        int id = 10;
        Customer customer = new Customer(
                id, "Sekou", "seh@gmail.com", 18
        );
        when(customerDb.selectCustomerById(id)).thenReturn(Optional.of(customer));



        CustomerUpdateRequest updateRequest  = new CustomerUpdateRequest(
                customer.getName(), customer.getEmail(), customer.getAge());


        assertThatThrownBy(() -> underTest.updateCustomer(id, updateRequest))
                .isInstanceOf(RequestValidationException.class)
                .hasMessage("no data changes found");

        verify(customerDb, never()).updateCustomer(any());

    }
}