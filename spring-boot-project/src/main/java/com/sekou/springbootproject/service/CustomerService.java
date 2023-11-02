package com.sekou.springbootproject.service;


import com.sekou.springbootproject.customers.Customer;
import com.sekou.springbootproject.customers.CustomerDb;
import org.springframework.stereotype.Service;

import java.util.List;
@Service

public class CustomerService {

    private final CustomerDb customerDb;

    public CustomerService(CustomerDb customerDb) {
        this.customerDb = customerDb;
    }

    public List<Customer> getAllCustomers() {
        return customerDb.selectAllCustomers();
    }

    public Customer getCustomer(Integer id) {
        return customerDb.selectCustomerById(id).orElseThrow(
                () -> new IllegalArgumentException("customer with id [%s] not found".formatted(id))
        );
    }
}
