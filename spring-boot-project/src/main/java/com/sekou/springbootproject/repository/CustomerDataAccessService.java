package com.sekou.springbootproject.repository;

import com.sekou.springbootproject.customers.Customer;
import com.sekou.springbootproject.customers.CustomerDb;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository("list")
public class CustomerDataAccessService implements CustomerDb {

    static final List<Customer> customers;

    static {
        customers = new ArrayList<>();
        Customer john = new Customer(
                1,
                "John Mary",
                "john@gmail.com",
                23

        );
        customers.add(john);

        Customer mary = new Customer(
                2,
                "Mary John",
                "mary@gmail.com",
                18

        );
        customers.add(mary);

        Customer doe = new Customer(
                3,
                "Doe Blama",
                "mary@gmail.com",
                18

        );
        customers.add(doe);

        Customer blama = new Customer(
                4,
                "Blama Doe",
                "mary@gmail.com",
                18

        );
        customers.add(blama);

        Customer moses = new Customer(
                5,
                "Moses Chato",
                "moses@gmail.com",
                12
        );
        customers.add(moses);

        Customer matu = new Customer(
                6,
                "Matu Matu",
                "matu@gmail.com",
                17
        );
        customers.add(matu);


    }


    @Override
    public List<Customer> selectAllCustomers() {
        return customers;
    }

    @Override
    public Optional<Customer> selectCustomerById(Integer id) {
       return customers.stream()
                .filter(c -> c.getId().equals(id))
                .findFirst();

    }

    @Override
    public void insertCustomer(Customer customer) {
       customer.add(customer);
    }

    @Override
    public boolean existsPersonWithEmail(String email) {
        return customers.stream()
                .allMatch(c -> c.getEmail().equals(email) );
    }

    @Override
    public boolean existsPersonWithId(Integer id) {
         return customers.stream()
                 .anyMatch(c ->c.getId().equals(id));
    }

    @Override
    public void deleteCustomerById(Integer customerId) {
       customers.stream()
               .filter(c->c.getId().equals(customerId))
               .findFirst()
               .ifPresent(customers::remove);
    }




}
