package com.harmoni.auth.component;

import com.harmoni.auth.model.UserRoleKey;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.io.Decoders;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Date;
import java.util.List;

/**
 * Utility class for generating, parsing, and validating JSON Web Tokens (JWT) used for authentication.
 * <p>
 * It uses HMAC SHA encryption and supports embedding user roles as claims.
 * </p>
 */
@Getter
@Component
public class JwtUtil {

    private static final long EXPIRATION_TIME = 86400000; // 1 day (fallback)

    @Value("${harmoni.auth.jwt.secret}")
    private String secretKey;

    @Value("${harmoni.auth.jwt.expired.time}")
    private long expiredTime;

    /**
     * Generates a signed JWT token containing the username and user roles.
     *
     * @param username the username to include in the token subject
     * @param roles    the list of roles associated with the user
     * @return a signed JWT token as a String
     */
    public String generateToken(String username, List<UserRoleKey> roles) {
        Instant now = Instant.now();
        return Jwts.builder()
                .subject(username)
                .issuedAt(Date.from(now))
                .claim("roles", roles)
                .expiration(Date.from(now.plusMillis(expiredTime)))
                .signWith(Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey)))
                .compact();
    }

    /**
     * Extracts the username from a JWT token.
     *
     * @param token the JWT token
     * @return the subject (username) from the token
     */
    public String extractUsername(String token) {
        return Jwts.parser()
                .verifyWith(Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey)))
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    /**
     * Validates the token by checking the username match and expiration time.
     *
     * @param token    the JWT token to validate
     * @param username the expected username
     * @return {@code true} if the token is valid, otherwise {@code false}
     */
    public boolean validateToken(String token, String username) {
        return extractUsername(token).equals(username) && !isTokenExpired(token);
    }

    /**
     * Checks if a JWT token is expired.
     *
     * @param token the JWT token
     * @return {@code true} if expired, {@code false} otherwise
     */
    private boolean isTokenExpired(String token) {
        return Jwts.parser()
                .verifyWith(Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey)))
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getExpiration()
                .before(new Date());
    }
}
