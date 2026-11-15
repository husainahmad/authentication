package com.harmoni.auth.application.port.in;

import com.harmoni.auth.web.dto.PermissionDto;

import java.util.List;

/**
 * Input port for permission administration.
 */
public interface PermissionManagementUseCase {

    List<PermissionDto> findAll();

    PermissionDto findById(Long id);

    PermissionDto create(PermissionDto permissionDto);

    PermissionDto update(Long id, PermissionDto permissionDto);

    void delete(Long id);
}
