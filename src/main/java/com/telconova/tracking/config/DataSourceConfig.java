package com.telconova.tracking.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = "com.telconova.tracking.repository")
@EnableJpaAuditing
public class DataSourceConfig {
    // La configuración específica proviene de application.yml
}
