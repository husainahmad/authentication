package com.harmoni.auth.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

/**
 * Transport object describing the roles and permissions granted to a user.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthoritiesDto {

    private Integer userId;
    private String username;
    private Set<String> roles;
    private Set<String> roleCodes;
    private Set<String> permissions;
    private List<AssignmentDto> scopes;
}
