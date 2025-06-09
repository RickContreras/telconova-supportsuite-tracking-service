package com.telconova.tracking.config;

import org.hibernate.id.uuid.UuidGenerator;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JpaConfig {

    @Bean
    public HibernatePropertiesCustomizer hibernatePropertiesCustomizer() {
        return hibernateProperties -> {
            hibernateProperties.put("hibernate.id.new_generator_mappings", "true");
            hibernateProperties.put("hibernate.id.generator_mappings", "true");
            hibernateProperties.put("hibernate.id.UUID", UuidGenerator.class.getName());
        };
    }
}
