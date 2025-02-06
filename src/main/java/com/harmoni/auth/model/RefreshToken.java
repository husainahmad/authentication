package com.harmoni.auth.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

@Data
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RefreshToken {
    private Long id;
    private Long userId;
    private String token;
    private Date expiresAt;
    private Date createdAt;
}