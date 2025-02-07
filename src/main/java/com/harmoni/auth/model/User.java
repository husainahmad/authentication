package com.harmoni.auth.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

@Data
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class User {
    private Integer id;
    private String username;
    private String email;
    private String password;
    private boolean enabled;
    private Date createdAt;
    private Date updatedAt;
}