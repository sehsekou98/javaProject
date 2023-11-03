package com.sekou.springbootproject.service;


import com.sekou.springbootproject.customers.Customer;
import com.sekou.springbootproject.customers.CustomerDb;
import com.sekou.springbootproject.exception.ResourceNotFound;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
@Service

public class CustomerService {

    private final CustomerDb customerDb;

    public CustomerService(@Qualifier("jpa") CustomerDb customerDb)
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
}
