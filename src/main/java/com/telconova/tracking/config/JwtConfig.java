package com.telconova.tracking.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
public class JwtConfig {
    // Debe ser la misma clave secreta que usa el servicio de autenticación
    @Value("${app.jwt.secret}")
    private String secret;
}
