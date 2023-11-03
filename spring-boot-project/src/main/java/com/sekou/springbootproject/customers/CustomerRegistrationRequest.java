package com.sekou.springbootproject.customers;

public record CustomerRegistrationRequest(
        String name,
        String email,
        Integer age
) {


}
