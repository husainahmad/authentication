package com.harmoni.auth.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserRoleKey {
    private Integer userId;
    private Integer roleId;
    private Role role;
}