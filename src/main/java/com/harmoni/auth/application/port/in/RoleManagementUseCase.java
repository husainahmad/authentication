package com.harmoni.auth.application.port.in;

import com.harmoni.auth.web.dto.RoleDto;

import java.util.List;
import java.util.Set;

/**
 * Input port for role administration.
 */
public interface RoleManagementUseCase {

    List<RoleDto> findAll();

    RoleDto findById(Long id);

    RoleDto create(RoleDto roleDto);

    RoleDto update(Long id, RoleDto roleDto);

    void delete(Long id);

    RoleDto replacePermissions(Long id, Set<Long> permissionIds);

    RoleDto duplicate(Long id);
}
