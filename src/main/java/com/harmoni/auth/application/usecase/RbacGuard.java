package com.harmoni.auth.application.usecase;

import com.harmoni.auth.domain.model.Permission;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Enforces least privilege for access-control changes: an operator may only
 * grant or assign permissions they already hold.
 */
public final class RbacGuard {

    private RbacGuard() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Returns the permission names held by the signed-in operator.
     *
     * @return the granted permission names, never {@code null}
     */
    public static Set<String> currentPermissions() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return Set.of();
        }
        return authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .filter(authority -> authority != null && !authority.startsWith("ROLE_"))
                .collect(Collectors.toSet());
    }

    /**
     * Whether the signed-in operator holds the given permission.
     *
     * @param permission the permission name
     * @return {@code true} when granted
     */
    public static boolean has(String permission) {
        return permission != null && currentPermissions().contains(permission);
    }

    /**
     * Rejects the change when the operator tries to grant a permission they do
     * not hold.
     *
     * @param permissionNames the permission names being granted
     * @throws AccessDeniedException when at least one permission is not held
     */
    public static void requireCanGrant(Collection<String> permissionNames) {
        if (permissionNames == null || permissionNames.isEmpty()) {
            return;
        }
        Set<String> granted = currentPermissions();
        for (String name : permissionNames) {
            if (name != null && !granted.contains(name)) {
                throw new AccessDeniedException("Cannot grant permission not held: " + name);
            }
        }
    }

    /**
     * Rejects the change when the operator tries to assign a role carrying a
     * permission they do not hold.
     *
     * @param permissions the permissions carried by the role
     * @throws AccessDeniedException when at least one permission is not held
     */
    public static void requireCanAssign(Collection<Permission> permissions) {
        if (permissions == null || permissions.isEmpty()) {
            return;
        }
        requireCanGrant(permissions.stream().map(Permission::getName).toList());
    }
}
