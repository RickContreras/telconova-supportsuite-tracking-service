package com.telconova.tracking;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

@TestConfiguration(proxyBeanMethods = false)
class TestcontainersConfiguration {

	@Bean(destroyMethod = "stop")
	@ServiceConnection(name = "postgres")
	PostgreSQLContainer<?> postgresContainer() {
		return new PostgreSQLContainer<>(DockerImageName.parse("postgres:16-alpine"))
				.withStartupTimeoutSeconds(120) // Increased timeout
				.withUsername("test").withPassword("test").withDatabaseName("test");
	}

	@Bean(destroyMethod = "stop")
	@ServiceConnection(name = "redis")
	GenericContainer<?> redisContainer() {
		return new GenericContainer<>(DockerImageName.parse("redis:7-alpine"))
				.withExposedPorts(6379);// .withStartupTimeoutSeconds(60);
	}
}
