package com.harmoni.auth.domain.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.Date;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import lombok.Data;

/**
 * User domain entity representing a user in the authentication system.
 * This is a core domain entity that encapsulates user state and behavior.
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class User {
    private Integer id;
    private String username;
    private String email;
    private String password;
    private boolean enabled;
    private Date createdAt;
    private Date updatedAt;
    private final Set<Role> roles = new HashSet<>();

    public Set<Role> getRoles() {
        return roles;
    }

    public void addRole(Role role) {
        this.roles.add(role);
    }

    public void removeRole(Role role) {
        this.roles.remove(role);
    }

    public boolean hasRole(String roleName) {
        return roles.stream()
                .anyMatch(role -> role.getName().equalsIgnoreCase(roleName));
    }

    public Set<String> getRoleNames() {
        return roles.stream()
                .map(Role::getName)
                .collect(Collectors.toSet());
    }
}
