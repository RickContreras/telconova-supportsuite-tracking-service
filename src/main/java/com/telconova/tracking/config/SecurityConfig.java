package com.telconova.tracking.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

        private final Environment env;

        public SecurityConfig(Environment env) {
                this.env = env;
        }

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
                http.csrf(csrf -> csrf.disable()).authorizeHttpRequests(authorize -> authorize
                                // Endpoints públicos
                                .requestMatchers("/actuator/health", "/actuator/health/liveness",
                                                "/actuator/health/readiness")
                                .permitAll()
                                .requestMatchers("/", "/swagger-ui/**", "/swagger-ui.html",
                                                "/api-docs/**")
                                .permitAll()
                                // Endpoints que requieren autenticación
                                .requestMatchers("/actuator/**").hasRole("ADMIN")
                                .requestMatchers("/api/**").authenticated().anyRequest()
                                .authenticated())
                                .httpBasic(org.springframework.security.config.Customizer
                                                .withDefaults()); // Habilitar autenticación HTTP
                                                                  // Basic

                return http.build();
        }

        @Bean
        public InMemoryUserDetailsManager userDetailsService() {
                String username = env.getProperty("spring.security.user.name", "admin");
                String password = env.getProperty("spring.security.user.password", "admin");
                String roles = env.getProperty("spring.security.user.roles", "ADMIN");

                UserDetails user = User.builder().username(username)
                                .password(passwordEncoder().encode(password))
                                .roles(roles.split(",")).build();

                return new InMemoryUserDetailsManager(user);
        }

        @Bean
        public PasswordEncoder passwordEncoder() {
                return new BCryptPasswordEncoder();
        }
}
