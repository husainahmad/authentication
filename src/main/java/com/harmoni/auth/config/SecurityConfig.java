package com.harmoni.auth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Configuration class for Spring Security setup.
 * <p>
 * Defines authentication manager and password encoder for the application.
 * JWT validation is handled by the gateway, so no JWT filter is registered here.
 * </p>
 */
@Configuration
public class SecurityConfig {

    /**
     * Provides a password encoder using BCrypt hashing algorithm.
     *
     * @return the configured {@link PasswordEncoder}
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Exposes the Spring-managed {@link AuthenticationManager}, which is required
     * by Spring Security for performing authentication operations.
     *
     * @param authConfig the Spring authentication configuration
     * @return the authentication manager bean
     * @throws Exception if an error occurs while creating the bean
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    /**
     * Configures the Spring Security filter chain to:
     * <ul>
     *     <li>Disable CSRF (not needed for REST APIs)</li>
     *     <li>Permit all {@code /api/v1/**} requests (JWT validation is done by the gateway)</li>
     *     <li>Use stateless session management</li>
     * </ul>
     *
     * @param http the {@link HttpSecurity} object
     * @return the configured {@link SecurityFilterChain}
     * @throws Exception if an error occurs while building the filter chain
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/v1/**").permitAll()
                        .anyRequest().permitAll())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        return http.build();
    }
}
