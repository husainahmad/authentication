package com.harmoni.auth.infrastructure.persistence.mybatis.mapper;

import com.harmoni.auth.domain.model.RbacAudit;
import org.apache.ibatis.annotations.Mapper;

/**
 * MyBatis mapper for the RBAC audit trail.
 */
@Mapper
public interface RbacAuditMapper {

    int insert(RbacAudit row);
}
