package com.harmoni.auth.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * Audit record for an access-control change.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RbacAudit {

    private Long id;
    private String actor;
    private String action;
    private String targetType;
    private Long targetId;
    private String detail;
    private Date createdAt;
}
