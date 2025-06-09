package com.telconova.tracking;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

@TestConfiguration(proxyBeanMethods = false)
public class TestcontainersConfiguration {

	@Bean(destroyMethod = "stop")
	@ServiceConnection(name = "postgres")
	public PostgreSQLContainer<?> postgreSQLContainer() {
		return new PostgreSQLContainer<>(DockerImageName.parse("postgres:16-alpine"))
				.withStartupTimeoutSeconds(120).withDatabaseName("testdb").withUsername("test")
				.withPassword("test");
	}

	@Bean(destroyMethod = "stop")
	@ServiceConnection(name = "redis")
	public GenericContainer<?> redisContainer() {
		GenericContainer<?> redisContainer =
				new GenericContainer<>(DockerImageName.parse("redis:7-alpine"))
						.withExposedPorts(6379);
		redisContainer.start();

		System.setProperty("spring.data.redis.host", redisContainer.getHost());
		System.setProperty("spring.data.redis.port",
				String.valueOf(redisContainer.getMappedPort(6379)));

		return redisContainer;
	}
}
