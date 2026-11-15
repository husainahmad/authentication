package com.harmoni.auth.application.usecase;

import com.harmoni.auth.domain.model.RbacAudit;
import com.harmoni.auth.infrastructure.persistence.mybatis.repository.RbacAuditRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * Writes an audit row for an access-control change, attributing it to the
 * signed-in operator forwarded by the gateway.
 */
@RequiredArgsConstructor
public class RbacAuditRecorder {

    private final RbacAuditRepository rbacAuditRepository;

    public void record(String action, String targetType, Long targetId, String detail) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String actor = authentication == null || authentication.getName() == null
                ? "system" : authentication.getName();
        rbacAuditRepository.save(RbacAudit.builder()
                .actor(actor)
                .action(action)
                .targetType(targetType)
                .targetId(targetId)
                .detail(detail)
                .build());
    }
}
