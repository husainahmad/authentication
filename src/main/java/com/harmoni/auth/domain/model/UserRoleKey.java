package com.harmoni.auth.domain.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * Composite key for user-role relationship.
 * Used in MyBatis mapping for user-role associations.
 */
@Data
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserRoleKey {
    private Long userId;
    private Long roleId;
    private Role role;
}