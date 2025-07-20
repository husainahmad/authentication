package com.harmoni.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;

/**
 * Main entry point for the Harmoni Authentication Service application.
 * <p>
 * This class bootstraps the Spring Boot application context.
 * It explicitly excludes several auto-configurations:
 * </p>
 * <ul>
 *     <li>{@link SecurityAutoConfiguration} – to use a custom security setup</li>
 *     <li>{@link UserDetailsServiceAutoConfiguration} – to avoid default user setup</li>
 *     <li>{@link HibernateJpaAutoConfiguration} – because MyBatis is used instead of JPA</li>
 * </ul>
 *
 * <p>Make sure any excluded configurations are properly replaced or handled manually.</p>
 *
 * @author
 */
@SpringBootApplication(
		exclude = {
				SecurityAutoConfiguration.class,
				UserDetailsServiceAutoConfiguration.class
		}
)
@EnableAutoConfiguration(
		exclude = {
				HibernateJpaAutoConfiguration.class
		}
)
public class AuthApplication {

	/**
	 * Main method to start the Spring Boot application.
	 *
	 * @param args command-line arguments passed to the application
	 */
	public static void main(String[] args) {
		SpringApplication.run(AuthApplication.class, args);
	}
}
