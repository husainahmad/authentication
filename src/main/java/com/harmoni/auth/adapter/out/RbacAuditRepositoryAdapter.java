package com.harmoni.auth.adapter.out;

import com.harmoni.auth.domain.model.RbacAudit;
import com.harmoni.auth.infrastructure.persistence.mybatis.mapper.RbacAuditMapper;
import com.harmoni.auth.infrastructure.persistence.mybatis.repository.RbacAuditRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

/**
 * Adapter implementing the {@link RbacAuditRepository} port with MyBatis.
 */
@Repository
@RequiredArgsConstructor
public class RbacAuditRepositoryAdapter implements RbacAuditRepository {

    private final RbacAuditMapper rbacAuditMapper;

    @Override
    public void save(RbacAudit audit) {
        rbacAuditMapper.insert(audit);
    }
}
