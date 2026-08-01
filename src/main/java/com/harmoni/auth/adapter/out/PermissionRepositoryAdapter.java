package com.harmoni.auth.adapter.out;

import com.harmoni.auth.domain.model.Permission;
import com.harmoni.auth.infrastructure.persistence.mybatis.repository.PermissionRepository;
import com.harmoni.auth.infrastructure.persistence.mybatis.mapper.PermissionMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.HashSet;
import java.util.Set;

/**
 * Adapter implementing the PermissionRepository port.
 * This is a secondary adapter (driven adapter) that connects the domain layer
 * to the existing MyBatis data access layer.
 */
@Repository
@RequiredArgsConstructor
public class PermissionRepositoryAdapter implements PermissionRepository {

    private final PermissionMapper permissionMapper;

    @Override
    public Permission save(Permission permission) {
        permissionMapper.insert(permission);
        return permission;
    }

    @Override
    public Permission findById(Long id) {
        return permissionMapper.selectByPrimaryKey(id);
    }

    @Override
    public Permission findByName(String name) {
        return permissionMapper.selectByName(name);
    }

    @Override
    public java.util.List<Permission> findAll() {
        return permissionMapper.selectAll();
    }

    @Override
    public boolean deleteById(Long id) {
        return permissionMapper.deleteByPrimaryKey(id) > 0;
    }

    @Override
    public Set<Permission> findPermissionsByRoleId(Long roleId) {
        return new HashSet<>(permissionMapper.selectPermissionsByRoleId(roleId));
    }

    @Override
    public Set<Permission> findPermissionsByUserId(Long userId) {
        return new HashSet<>(permissionMapper.selectPermissionsByUserId(userId));
    }
}