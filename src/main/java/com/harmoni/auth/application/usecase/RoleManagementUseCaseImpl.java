package com.harmoni.auth.application.usecase;

import com.harmoni.auth.application.port.in.RoleManagementUseCase;
import com.harmoni.auth.domain.model.Permission;
import com.harmoni.auth.domain.model.Role;
import com.harmoni.auth.infrastructure.persistence.mybatis.repository.PermissionRepository;
import com.harmoni.auth.infrastructure.persistence.mybatis.repository.RolePermissionRepository;
import com.harmoni.auth.infrastructure.persistence.mybatis.repository.RoleRepository;
import com.harmoni.auth.web.dto.RoleDto;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Application service for role administration.
 */
@RequiredArgsConstructor
public class RoleManagementUseCaseImpl implements RoleManagementUseCase {

    private final RoleRepository roleRepository;
    private final RolePermissionRepository rolePermissionRepository;
    private final PermissionRepository permissionRepository;
    private final RbacAuditRecorder auditRecorder;

    @Override
    public List<RoleDto> findAll() {
        return roleRepository.findAll().stream()
                .map(this::withPermissions)
                .toList();
    }

    @Override
    public RoleDto findById(Long id) {
        Role role = requireRole(id);
        return withPermissions(role);
    }

    @Override
    public RoleDto create(RoleDto roleDto) {
        validate(roleDto);
        if (roleRepository.findByName(roleDto.getName()) != null) {
            throw new IllegalArgumentException("Role already exists: " + roleDto.getName());
        }
        Role role = roleDto.toRole();
        if (role.getScopeType() == null || role.getScopeType().isBlank()) {
            role.setScopeType("STORE");
        }
        role.setSystem(false);
        Role saved = roleRepository.save(role);
        auditRecorder.record("CREATE", "ROLE", saved.getId(), saved.getName());
        return withPermissions(saved);
    }

    @Override
    public RoleDto update(Long id, RoleDto roleDto) {
        validate(roleDto);
        Role existing = requireRole(id);
        Role duplicate = roleRepository.findByName(roleDto.getName());
        if (duplicate != null && !duplicate.getId().equals(id)) {
            throw new IllegalArgumentException("Role already exists: " + roleDto.getName());
        }
        if (existing.isSystem()) {
            if (!Objects.equals(existing.getName(), roleDto.getName())
                    || !Objects.equals(existing.getScopeType(), roleDto.getScopeType())
                    || !Objects.equals(existing.getCode(), roleDto.getCode())) {
                throw new IllegalArgumentException("System role identity cannot be changed");
            }
            existing.setDescription(roleDto.getDescription());
        } else {
            existing.setName(roleDto.getName());
            existing.setDescription(roleDto.getDescription());
            existing.setScopeType(roleDto.getScopeType() == null || roleDto.getScopeType().isBlank()
                    ? existing.getScopeType() : roleDto.getScopeType());
            existing.setCode(roleDto.getCode());
        }
        Role updated = roleRepository.update(existing);
        auditRecorder.record("UPDATE", "ROLE", updated.getId(), updated.getName());
        return withPermissions(updated);
    }

    @Override
    public void delete(Long id) {
        Role role = requireRole(id);
        if (role.isSystem()) {
            throw new IllegalArgumentException("System roles cannot be deleted");
        }
        roleRepository.deleteById(id);
        auditRecorder.record("DELETE", "ROLE", id, role.getName());
    }

    @Override
    public RoleDto replacePermissions(Long id, Set<Long> permissionIds) {
        Role role = requireRole(id);
        Set<Permission> requested = permissionIds == null ? Set.of()
                : permissionIds.stream()
                        .map(permissionRepository::findById)
                        .filter(Objects::nonNull)
                        .collect(Collectors.toSet());
        RbacGuard.requireCanAssign(requested);

        Set<Long> effective = new java.util.LinkedHashSet<>();
        if (permissionIds != null) {
            effective.addAll(permissionIds);
        }
        rolePermissionRepository.findPermissionsByRoleId(id).stream()
                .filter(permission -> permission.getModule() == null)
                .map(Permission::getId)
                .forEach(effective::add);

        rolePermissionRepository.replacePermissions(id, effective);
        auditRecorder.record("UPDATE_PERMISSIONS", "ROLE", id, role.getName());
        return withPermissions(role);
    }

    @Override
    public RoleDto duplicate(Long id) {
        Role source = requireRole(id);
        Set<Permission> sourcePermissions = rolePermissionRepository.findPermissionsByRoleId(id);
        RbacGuard.requireCanAssign(sourcePermissions);

        Role copy = new Role();
        copy.setName(uniqueCopyName(source.getName()));
        copy.setDescription(source.getDescription());
        copy.setScopeType(source.getScopeType());
        copy.setSystem(false);
        Role saved = roleRepository.save(copy);

        Set<Long> permissionIds = sourcePermissions.stream()
                .map(Permission::getId)
                .collect(Collectors.toSet());
        rolePermissionRepository.replacePermissions(saved.getId(), permissionIds);
        auditRecorder.record("DUPLICATE", "ROLE", saved.getId(), source.getName());
        return withPermissions(saved);
    }

    private String uniqueCopyName(String name) {
        String base = name + " (Copy)";
        String candidate = base;
        int index = 2;
        while (roleRepository.findByName(candidate) != null) {
            candidate = base + " " + index++;
        }
        return candidate;
    }

    private RoleDto withPermissions(Role role) {
        Set<Permission> permissions = rolePermissionRepository.findPermissionsByRoleId(role.getId());
        return RoleDto.from(role, permissions);
    }

    private Role requireRole(Long id) {
        Role role = roleRepository.findById(id);
        if (role == null) {
            throw new IllegalArgumentException("Role not found: " + id);
        }
        return role;
    }

    private void validate(RoleDto roleDto) {
        if (roleDto == null || roleDto.getName() == null || roleDto.getName().isBlank()) {
            throw new IllegalArgumentException("Role name is required");
        }
    }
}
