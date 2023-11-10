package com.sekou.springbootproject;

import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.assertThat;


@Testcontainers
class TestcontainersTest {
	@Container
private static final PostgreSQLContainer<?> postgresSQLContainer =
		new PostgreSQLContainer<>("postgres:latest")
				.withDatabaseName("customer-db-unit-test")
				.withUsername("sehsekou98")
				.withPassword("passwor");
	@Test
	void canStartPostgresDB() {
		assertThat(postgresSQLContainer.isRunning()).isTrue();
		assertThat(postgresSQLContainer.isCreated()).isTrue();
	}
}
