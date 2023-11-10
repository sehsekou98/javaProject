package com.sekou.springbootproject;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

class TestcontainersTest extends AbstractUniteTestContainer{

	@Test
	void canStartPostgresDB() {
		assertThat(postgresSQLContainer.isRunning()).isTrue();
		assertThat(postgresSQLContainer.isCreated()).isTrue();
	}


}
