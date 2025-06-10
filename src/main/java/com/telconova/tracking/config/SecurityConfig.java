package com.telconova.tracking.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.telconova.tracking.security.JwtAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

        private final JwtAuthenticationFilter jwtAuthenticationFilter;

        public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
                this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        }

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
                http.csrf(csrf -> csrf.disable())
                                .sessionManagement(session -> session.sessionCreationPolicy(
                                                SessionCreationPolicy.STATELESS))
                                .authorizeHttpRequests(authorize -> authorize
                                                // Endpoints públicos
                                                .requestMatchers("/actuator/health",
                                                                "/actuator/health/liveness",
                                                                "/actuator/health/readiness")
                                                .permitAll()
                                                .requestMatchers("/", "/swagger-ui/**",
                                                                "/swagger-ui.html", "/api-docs/**")
                                                .permitAll()
                                                // Endpoints que requieren roles específicos
                                                .requestMatchers("/actuator/**").hasRole("ADMIN")
                                                .requestMatchers("/api/v1/avances")
                                                .hasAnyAuthority("TECNICO", "SUPERVISOR", "ADMIN")
                                                .requestMatchers("/api/v1/avances/*/editar-tiempo")
                                                .hasAuthority("TECNICO").anyRequest()
                                                .authenticated());

                // Agregar filtro JWT antes del filtro de autenticación estándar
                http.addFilterBefore(jwtAuthenticationFilter,
                                UsernamePasswordAuthenticationFilter.class);

                return http.build();
        }

}
