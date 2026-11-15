package com.harmoni.auth.adapter.out;

import com.harmoni.auth.domain.model.Permission;
import com.harmoni.auth.domain.model.RolePermissionKey;
import com.harmoni.auth.infrastructure.persistence.mybatis.mapper.PermissionMapper;
import com.harmoni.auth.infrastructure.persistence.mybatis.mapper.RolePermissionMapper;
import com.harmoni.auth.infrastructure.persistence.mybatis.repository.RolePermissionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.HashSet;
import java.util.Set;

/**
 * Adapter implementing the {@link RolePermissionRepository} port with MyBatis.
 */
@Repository
@RequiredArgsConstructor
public class RolePermissionRepositoryAdapter implements RolePermissionRepository {

    private final RolePermissionMapper rolePermissionMapper;
    private final PermissionMapper permissionMapper;

    @Override
    public void assign(Long roleId, Long permissionId) {
        rolePermissionMapper.insert(new RolePermissionKey().setRoleId(roleId).setPermissionId(permissionId));
    }

    @Override
    public void remove(Long roleId, Long permissionId) {
        rolePermissionMapper.deleteByPrimaryKey(
                new RolePermissionKey().setRoleId(roleId).setPermissionId(permissionId));
    }

    @Override
    public void replacePermissions(Long roleId, Set<Long> permissionIds) {
        rolePermissionMapper.deleteByRoleId(roleId);
        if (permissionIds == null) {
            return;
        }
        permissionIds.stream()
                .filter(java.util.Objects::nonNull)
                .distinct()
                .forEach(permissionId -> assign(roleId, permissionId));
    }

    @Override
    public Set<Permission> findPermissionsByRoleId(Long roleId) {
        return new HashSet<>(permissionMapper.selectPermissionsByRoleId(roleId));
    }
}
