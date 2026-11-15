package com.harmoni.auth.adapter.out;

import com.harmoni.auth.domain.model.Role;
import com.harmoni.auth.domain.model.UserRoleKey;
import com.harmoni.auth.infrastructure.persistence.mybatis.mapper.UserRoleMapper;
import com.harmoni.auth.infrastructure.persistence.mybatis.repository.UserRoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * Adapter implementing the {@link UserRoleRepository} port with MyBatis.
 */
@Repository
@RequiredArgsConstructor
public class UserRoleRepositoryAdapter implements UserRoleRepository {

    private final UserRoleMapper userRoleMapper;

    @Override
    public void assign(Long userId, Long roleId, String scopeType, Long scopeId) {
        userRoleMapper.insert(new UserRoleKey()
                .setUserId(userId)
                .setRoleId(roleId)
                .setScopeType(scopeType)
                .setScopeId(scopeId));
    }

    @Override
    public void remove(Long userId, Long roleId) {
        userRoleMapper.deleteByPrimaryKey(new UserRoleKey().setUserId(userId).setRoleId(roleId));
    }

    @Override
    public void replaceAssignments(Long userId, List<UserRoleKey> assignments) {
        userRoleMapper.deleteByUserId(userId);
        if (assignments == null) {
            return;
        }
        assignments.stream()
                .filter(Objects::nonNull)
                .filter(assignment -> assignment.getRoleId() != null)
                .forEach(assignment -> assign(userId, assignment.getRoleId(),
                        assignment.getScopeType(), assignment.getScopeId()));
    }

    @Override
    public List<UserRoleKey> findAssignmentsByUserId(Long userId) {
        return userRoleMapper.selectRolesByUserId(Math.toIntExact(userId));
    }

    @Override
    public Map<Long, List<UserRoleKey>> findAssignmentsByUserIds(Collection<Long> userIds) {
        Map<Long, List<UserRoleKey>> result = new LinkedHashMap<>();
        if (userIds == null || userIds.isEmpty()) {
            return result;
        }
        for (UserRoleKey assignment : userRoleMapper.selectAssignmentsByUserIds(userIds)) {
            result.computeIfAbsent(assignment.getUserId(), key -> new ArrayList<>()).add(assignment);
        }
        return result;
    }

    @Override
    public Set<Role> findRolesByUserId(Long userId) {
        Set<Role> roles = new HashSet<>();
        findAssignmentsByUserId(userId).stream()
                .map(UserRoleKey::getRole)
                .filter(Objects::nonNull)
                .forEach(roles::add);
        return roles;
    }
}
