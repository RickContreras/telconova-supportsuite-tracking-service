package com.telconova.tracking.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
        @Value("${spring.application.name}")
        private String applicationName;

        @Value("${server.port:8080}")
        private String serverPort;

        @Value("${swagger.production-server-url:https://api.example.com}")
        private String productionServerUrl;

        @Bean
        public OpenAPI customOpenAPI() {
                return new OpenAPI()
                                .addServersItem(new Server().url("http://localhost:" + serverPort))
                                .addServersItem(new Server().url(productionServerUrl))
                                .components(new Components().addSecuritySchemes("bearerAuth",
                                                new SecurityScheme().type(SecurityScheme.Type.HTTP)
                                                                .scheme("bearer")
                                                                .bearerFormat("JWT")))
                                .addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
                                .info(new Info().title(
                                                "API de Tracking Service - TelcoNova SupportSuite")
                                                .description("Microservicio para gestión de avances técnicos")
                                                .version("1.0.0")
                                                .contact(new Contact().name("Equipo de TelcoNova")
                                                                .email("dev@telconova.com")
                                                                .url("https://telconova.com"))
                                                .license(new License().name("Licencia Privada").url(
                                                                "https://telconova.com/licencia")));
        }
}
