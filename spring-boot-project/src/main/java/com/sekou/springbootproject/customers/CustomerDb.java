package com.sekou.springbootproject.customers;

import java.util.List;
import java.util.Optional;

public interface CustomerDb {

    List<Customer> selectAllCustomers();
    Optional<Customer> selectCustomerById(Integer id);
}
