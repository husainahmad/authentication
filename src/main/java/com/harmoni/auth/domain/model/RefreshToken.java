package com.harmoni.auth.domain.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.Instant;
import java.util.Objects;

import lombok.Data;

/**
 * RefreshToken domain entity representing a refresh token used to obtain new access tokens.
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RefreshToken {
    private Long id;
    private String token;
    private Long userId;
    private Instant expiryDate;

    /**
     * Checks if the refresh token has expired.
     *
     * @return true if the token is expired, false otherwise
     */
    public boolean isExpired() {
        return Instant.now().isAfter(expiryDate);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RefreshToken that = (RefreshToken) o;
        return Objects.equals(id, that.id) && Objects.equals(token, that.token);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, token);
    }

    @Override
    public String toString() {
        return "RefreshToken{" +
                "id=" + id +
                ", token='" + token + '\'' +
                ", userId=" + userId +
                ", expiryDate=" + expiryDate +
                '}';
    }
}
