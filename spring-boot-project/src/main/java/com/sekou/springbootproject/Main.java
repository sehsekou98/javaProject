package com.sekou.springbootproject;

import com.sekou.springbootproject.customers.Customer;
import com.sekou.springbootproject.repository.CustomerRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;

@SpringBootApplication
public class Main {


	public static void main(String[] args) {

		SpringApplication.run(Main.class, args);
	}

	@Bean
	CommandLineRunner runner(CustomerRepository customerRepository) {
		return args -> {

			Customer mary = new Customer(

					"Mary John",
					"mary@gmail.com",
					28

			);

			Customer doe = new Customer(

					"Doe Blama",
					"doe@gmail.com",
					18

			);
			List<Customer> customers = List.of(mary, doe);
			//customerRepository.saveAll(customers);

		};


	}

}










