package com.harmoni.auth.config;

import com.harmoni.auth.component.JwtUtil;
import com.harmoni.auth.http.filter.JwtFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Configuration class for Spring Security setup.
 * <p>
 * Defines authentication manager, password encoder, JWT filter, and HTTP security rules
 * for securing API endpoints using stateless JWT-based authentication.
 * </p>
 */
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtUtil jwtUtil;

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
     *     <li>Permit unauthenticated access to {@code /api/v1/auth/**} endpoints</li>
     *     <li>Require authentication for all other requests</li>
     *     <li>Use stateless session management</li>
     *     <li>Add a custom JWT filter before {@link UsernamePasswordAuthenticationFilter}</li>
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
                        .requestMatchers("/api/v1/auth/**").permitAll()
                        .anyRequest().authenticated())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(new JwtFilter(jwtUtil), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}
