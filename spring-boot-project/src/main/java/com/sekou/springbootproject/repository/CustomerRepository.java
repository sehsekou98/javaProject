package com.sekou.springbootproject.repository;

import com.sekou.springbootproject.customers.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository
        extends JpaRepository<Customer, Integer> {
    boolean existsCustomerByEmail(String email);
}
