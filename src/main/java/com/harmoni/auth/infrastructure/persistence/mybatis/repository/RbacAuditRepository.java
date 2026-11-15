package com.harmoni.auth.infrastructure.persistence.mybatis.repository;

import com.harmoni.auth.domain.model.RbacAudit;

/**
 * Repository port for the RBAC audit trail.
 */
public interface RbacAuditRepository {

    void save(RbacAudit audit);
}
