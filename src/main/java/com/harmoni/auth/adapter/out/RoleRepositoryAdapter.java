package com.harmoni.auth.adapter.out;

import com.harmoni.auth.domain.model.Role;
import com.harmoni.auth.infrastructure.persistence.mybatis.repository.RoleRepository;
import com.harmoni.auth.infrastructure.persistence.mybatis.mapper.RoleMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.HashSet;
import java.util.Set;

/**
 * Adapter implementing the RoleRepository port.
 * This is a secondary adapter (driven adapter) that connects the domain layer
 * to the existing MyBatis data access layer.
 */
@Repository
@RequiredArgsConstructor
public class RoleRepositoryAdapter implements RoleRepository {

    private final RoleMapper roleMapper;

    @Override
    public Role save(Role role) {
        roleMapper.insert(role);
        return role;
    }

    @Override
    public Role findById(Long id) {
        return roleMapper.selectByPrimaryKey(id);
    }

    @Override
    public Role findByName(String name) {
        return roleMapper.selectByName(name);
    }

    @Override
    public java.util.List<Role> findAll() {
        return roleMapper.selectAll();
    }

    @Override
    public boolean deleteById(Long id) {
        return roleMapper.deleteByPrimaryKey(id) > 0;
    }

    @Override
    public Set<Role> findRolesByUserId(Long userId) {
        // Assuming there's a method in RoleMapper to get roles by user ID
        // If not, we would need to use the UserRoleMapper
        return new HashSet<>(roleMapper.selectRolesByUserId(userId));
    }
}