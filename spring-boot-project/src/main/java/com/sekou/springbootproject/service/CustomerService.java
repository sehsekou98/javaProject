package com.sekou.springbootproject.service;


import com.sekou.springbootproject.customers.Customer;
import com.sekou.springbootproject.customers.CustomerDb;
import com.sekou.springbootproject.customers.CustomerRegistrationRequest;
import com.sekou.springbootproject.customers.CustomerUpdateRequest;
import com.sekou.springbootproject.exception.DuplicateResourceException;
import com.sekou.springbootproject.exception.ResourceNotFound;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class CustomerService {

    private final CustomerDb customerDb;

    public CustomerService(@Qualifier("jdbc") CustomerDb customerDb)
    {
        this.customerDb = customerDb;
    }

    public List<Customer> getAllCustomers() {

        return customerDb.selectAllCustomers();
    }

    public Customer getCustomer(Integer id) {
        return customerDb.selectCustomerById(id).orElseThrow(
                () -> new ResourceNotFound("customer with id [%s] not found".formatted(id))
        );
    }
    public void addCustomer(CustomerRegistrationRequest customerRegistrationRequest) {
        // check if email exist
        String email = customerRegistrationRequest.email();
        if (customerDb.existsPersonWithEmail(email)) {
            throw new DuplicateResourceException("email already taken"
            );
        }

        //add
        Customer customer = new Customer(
                customerRegistrationRequest.name(),
                customerRegistrationRequest.email(),
                customerRegistrationRequest.age()
        );
        customerDb.insertCustomer(customer);

    }

    public void deleteCustomerById(Integer customerId) {
        if (!customerDb.existsPersonWithId(customerId)) {
            throw new ResourceNotFound(
                    "customer with id [%s] not found".formatted(customerId)
            );
        }
        customerDb.deleteCustomerById(customerId);
    }

    public void updateCustomer(Integer customerId, CustomerUpdateRequest updateRequest) {
        Customer customer = getCustomer(customerId);

        boolean changes = false;

        if (updateRequest.name() != null && !updateRequest.name().equals(customer.getName())) {
          customer.setName(updateRequest.name());
          customerDb.insertCustomer(customer);
          changes = true;
        }

        if (updateRequest.email() != null && !updateRequest.email().equals(customer.getEmail())) {
            customer.setEmail(updateRequest.email());
            customerDb.insertCustomer(customer);
            changes = true;
        }

        if (updateRequest.age() != null && !updateRequest.age().equals(customer.getAge())) {
            customer.setAge(updateRequest.age());
            customerDb.insertCustomer(customer);
            changes = true;
        }
    }

 }

