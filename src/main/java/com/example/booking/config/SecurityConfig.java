// src/main/java/com/example/booking/config/SecurityConfig.java
package com.example.booking.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Security configuration for enabling HTTP Basic authentication.
 * Configures CSRF, request authorization, and logs security setup.
 */
@Configuration
public class SecurityConfig {
    private static final Logger logger = LoggerFactory.getLogger(SecurityConfig.class);

    /**
     * Configures the security filter chain with HTTP Basic authentication.
     *
     * @param http the {@link HttpSecurity} to modify
     * @return the configured {@link SecurityFilterChain}
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) {
        try {
            logger.info("Initializing security filter chain with HTTP Basic authentication.");
            http
                    .csrf(csrf -> {
                        csrf.disable();
                        logger.debug("CSRF protection disabled.");
                    })
                    .authorizeHttpRequests(auth -> {
                        auth.anyRequest().authenticated();
                        logger.debug("All requests require authentication.");
                    })
                    .httpBasic(Customizer.withDefaults());
            logger.info("Security filter chain initialized successfully.");
            return http.build();
        } catch (Exception ex) {
            logger.error("Failed to initialize security filter chain.", ex);
            throw new IllegalStateException("Security configuration failed", ex);
        }
    }
}