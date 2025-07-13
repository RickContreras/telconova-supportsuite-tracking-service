package com.telconova.tracking.config;

import com.telconova.tracking.security.SecurityExceptionHandler;
import com.telconova.tracking.security.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

        private final JwtAuthenticationFilter jwtAuthenticationFilter;
        private final SecurityExceptionHandler securityExceptionHandler;

        public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter,
                        SecurityExceptionHandler securityExceptionHandler) {
                this.jwtAuthenticationFilter = jwtAuthenticationFilter;
                this.securityExceptionHandler = securityExceptionHandler;
        }

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
                http.csrf(AbstractHttpConfigurer::disable)
                                .sessionManagement(session -> session.sessionCreationPolicy(
                                                SessionCreationPolicy.STATELESS))
                                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
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
                                                .requestMatchers("/actuator/**").permitAll()
                                                .requestMatchers("/api/v1/avances")
                                                .hasAnyRole("TECNICO", "SUPERVISOR", "ADMIN")
                                                .requestMatchers("/api/v1/avances/*/editar-tiempo")
                                                .hasRole("TECNICO")
                                                .requestMatchers("/api/v1/avances/reportes/*")
                                                .hasRole("ADMIN")
                                                .requestMatchers("/api/v1/avances/*/evidencias")
                                                .hasAnyRole("TECNICO", "SUPERVISOR").anyRequest()
                                                .authenticated())
                                .exceptionHandling(exceptions -> exceptions
                                                .authenticationEntryPoint(securityExceptionHandler)
                                                .accessDeniedHandler(securityExceptionHandler));

                // Agregar filtro JWT
                http.addFilterBefore(jwtAuthenticationFilter,
                                UsernamePasswordAuthenticationFilter.class);

                return http.build();
        }

        @Bean
        public UrlBasedCorsConfigurationSource corsConfigurationSource() {
                CorsConfiguration config = new CorsConfiguration();
                config.addAllowedOriginPattern("*");
                config.addAllowedHeader("*");
                config.addAllowedMethod("*");
                config.setAllowCredentials(true);

                UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
                source.registerCorsConfiguration("/**", config);
                return source;
        }
}
