package com.telconova.tracking.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.TestPropertySource;

@Configuration
@Profile("test")
@TestPropertySource(properties = {"spring.datasource.url=jdbc:tc:postgresql:///testdb",
        "spring.jpa.hibernate.ddl-auto=create-drop",
        "spring.security.oauth2.resourceserver.jwt.public-key-location=classpath:test-public.pem",})
public class TestPropertiesConfig {
    // Sin contenido
}
