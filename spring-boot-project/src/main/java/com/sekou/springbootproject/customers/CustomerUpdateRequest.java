package com.sekou.springbootproject.customers;

public record CustomerUpdateRequest(
        String name,
        String email,
        Integer age
) {
}

