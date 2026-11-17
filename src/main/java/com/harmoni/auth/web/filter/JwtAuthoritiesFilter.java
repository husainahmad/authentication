package com.harmoni.auth.web.filter;

import com.harmoni.auth.component.JwtUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Builds the security context from a signed bearer token.
 *
 * <p>The gateway forwards identity headers, but internal service-to-service
 * calls (for example the menu service creating an employee) only carry the
 * bearer token. Validating the token here keeps the access-control endpoints
 * protected even when they are reached without the gateway headers.</p>
 */
@Component
@RequiredArgsConstructor
public class JwtAuthoritiesFilter extends OncePerRequestFilter {

    private static final String HEADER_AUTHORIZATION = "Authorization";
    private static final String BEARER = "Bearer ";

    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String header = request.getHeader(HEADER_AUTHORIZATION);
        if (header != null && header.startsWith(BEARER)
                && SecurityContextHolder.getContext().getAuthentication() == null) {
            authenticate(header.substring(BEARER.length()));
        }
        filterChain.doFilter(request, response);
    }

    private void authenticate(String token) {
        try {
            Claims claims = Jwts.parser()
                    .verifyWith(Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtUtil.getSecretKey())))
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
            String username = claims.getSubject();
            if (username == null || username.isBlank()) {
                return;
            }
            List<GrantedAuthority> authorities = new ArrayList<>();
            Object permissions = claims.get("permissions");
            if (permissions instanceof Collection<?> values) {
                for (Object value : values) {
                    if (value != null) {
                        authorities.add(new SimpleGrantedAuthority(String.valueOf(value)));
                    }
                }
            }
            Object roles = claims.get("roles");
            if (roles instanceof Collection<?> values) {
                for (Object value : values) {
                    if (value instanceof Map<?, ?> role) {
                        addRole(authorities, role.get("code"));
                        addRole(authorities, role.get("name"));
                    }
                }
            }
            SecurityContextHolder.getContext().setAuthentication(
                    new UsernamePasswordAuthenticationToken(username, null, authorities));
        } catch (RuntimeException ex) {
            SecurityContextHolder.clearContext();
        }
    }

    private static void addRole(List<GrantedAuthority> authorities, Object role) {
        if (role != null && !String.valueOf(role).isBlank()) {
            authorities.add(new SimpleGrantedAuthority("ROLE_" + role));
        }
    }
}
