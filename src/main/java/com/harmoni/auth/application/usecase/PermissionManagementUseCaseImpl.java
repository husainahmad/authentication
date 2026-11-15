package com.harmoni.auth.application.usecase;

import com.harmoni.auth.application.port.in.PermissionManagementUseCase;
import com.harmoni.auth.domain.model.Permission;
import com.harmoni.auth.infrastructure.persistence.mybatis.repository.PermissionRepository;
import com.harmoni.auth.web.dto.PermissionDto;
import lombok.RequiredArgsConstructor;

import java.util.List;

/**
 * Application service for permission administration.
 */
@RequiredArgsConstructor
public class PermissionManagementUseCaseImpl implements PermissionManagementUseCase {

    private final PermissionRepository permissionRepository;
    private final RbacAuditRecorder auditRecorder;

    @Override
    public List<PermissionDto> findAll() {
        return permissionRepository.findAll().stream()
                .map(PermissionDto::from)
                .toList();
    }

    @Override
    public PermissionDto findById(Long id) {
        return PermissionDto.from(requirePermission(id));
    }

    @Override
    public PermissionDto create(PermissionDto permissionDto) {
        validate(permissionDto);
        if (permissionRepository.findByName(permissionDto.getName()) != null) {
            throw new IllegalArgumentException("Permission already exists: " + permissionDto.getName());
        }
        Permission saved = permissionRepository.save(permissionDto.toPermission());
        auditRecorder.record("CREATE", "PERMISSION", saved.getId(), saved.getName());
        return PermissionDto.from(saved);
    }

    @Override
    public PermissionDto update(Long id, PermissionDto permissionDto) {
        validate(permissionDto);
        Permission existing = requirePermission(id);
        Permission duplicate = permissionRepository.findByName(permissionDto.getName());
        if (duplicate != null && !duplicate.getId().equals(id)) {
            throw new IllegalArgumentException("Permission already exists: " + permissionDto.getName());
        }
        existing.setName(permissionDto.getName());
        existing.setDescription(permissionDto.getDescription());
        existing.setModule(permissionDto.getModule());
        existing.setAction(permissionDto.getAction());
        existing.setCategory(permissionDto.getCategory());
        existing.setLabel(permissionDto.getLabel());
        Permission updated = permissionRepository.update(existing);
        auditRecorder.record("UPDATE", "PERMISSION", updated.getId(), updated.getName());
        return PermissionDto.from(updated);
    }

    @Override
    public void delete(Long id) {
        Permission permission = requirePermission(id);
        permissionRepository.deleteById(id);
        auditRecorder.record("DELETE", "PERMISSION", id, permission.getName());
    }

    private Permission requirePermission(Long id) {
        Permission permission = permissionRepository.findById(id);
        if (permission == null) {
            throw new IllegalArgumentException("Permission not found: " + id);
        }
        return permission;
    }

    private void validate(PermissionDto permissionDto) {
        if (permissionDto == null || permissionDto.getName() == null || permissionDto.getName().isBlank()) {
            throw new IllegalArgumentException("Permission name is required");
        }
    }
}
