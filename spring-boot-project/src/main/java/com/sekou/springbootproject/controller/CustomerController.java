package com.sekou.springbootproject.controller;

import com.sekou.springbootproject.customers.Customer;
import com.sekou.springbootproject.customers.CustomerRegistrationRequest;
import com.sekou.springbootproject.service.CustomerService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/customers")
public class CustomerController {
    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping
    public List<Customer> getCustomers()
    {
        return customerService.getAllCustomers();
    }


    @GetMapping("{customerId}")
    public Customer getCustomer(
            @PathVariable("customerId") Integer customerId) {
        return customerService.getCustomer(customerId);

    }

    @PostMapping
    public void registerCustomer(
           @RequestBody CustomerRegistrationRequest request) {
        customerService.addCustomer(request);
    }
}
