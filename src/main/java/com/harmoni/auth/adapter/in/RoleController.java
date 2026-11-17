package com.harmoni.auth.adapter.in;

import com.harmoni.auth.application.port.in.RoleManagementUseCase;
import com.harmoni.auth.application.usecase.RbacGuard;
import com.harmoni.auth.web.dto.CommonDto;
import com.harmoni.auth.web.dto.RoleDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

/**
 * REST controller for role administration.
 */
@RestController
@RequestMapping("/api/v1/auth/roles")
@RequiredArgsConstructor
public class RoleController {

    private final RoleManagementUseCase roleManagementUseCase;

    @GetMapping
    @PreAuthorize("hasAnyAuthority('rbac:manage', 'rbac:read', 'employee:view')")
    public ResponseEntity<CommonDto> findAll() {
        return ok(visibleRoles());
    }

    /**
     * Hides the platform-only roles from operators who cannot manage them.
     * The Pemilik role is still enforced server-side when assigning, this only
     * keeps the picker from offering it.
     */
    private java.util.List<RoleDto> visibleRoles() {
        java.util.List<RoleDto> roles = roleManagementUseCase.findAll();
        if (RbacGuard.hasRoleCode("SYS")) {
            return roles;
        }
        return roles.stream()
                .filter(role -> !"OWN".equalsIgnoreCase(role.getCode())
                        && !"SYS".equalsIgnoreCase(role.getCode()))
                .toList();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('rbac:manage', 'rbac:read', 'employee:view')")
    public ResponseEntity<CommonDto> findById(@PathVariable Long id) {
        return ok(roleManagementUseCase.findById(id));
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('rbac:manage', 'rbac:write')")
    public ResponseEntity<CommonDto> create(@RequestBody RoleDto roleDto) {
        return created(roleManagementUseCase.create(roleDto));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('rbac:manage', 'rbac:write')")
    public ResponseEntity<CommonDto> update(@PathVariable Long id, @RequestBody RoleDto roleDto) {
        return ok(roleManagementUseCase.update(id, roleDto));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('rbac:manage', 'rbac:delete')")
    public ResponseEntity<CommonDto> delete(@PathVariable Long id) {
        roleManagementUseCase.delete(id);
        return ok(null);
    }

    @PutMapping("/{id}/permissions")
    @PreAuthorize("hasAnyAuthority('rbac:manage', 'rbac:write')")
    public ResponseEntity<CommonDto> replacePermissions(@PathVariable Long id,
                                                        @RequestBody Set<Long> permissionIds) {
        return ok(roleManagementUseCase.replacePermissions(id, permissionIds));
    }

    @PostMapping("/{id}/duplicate")
    @PreAuthorize("hasAnyAuthority('rbac:manage', 'rbac:write')")
    public ResponseEntity<CommonDto> duplicate(@PathVariable Long id) {
        return created(roleManagementUseCase.duplicate(id));
    }

    private ResponseEntity<CommonDto> ok(Object data) {
        return ResponseEntity.ok(CommonDto.builder()
                .httpStatus(HttpStatus.OK.value())
                .data(data)
                .build());
    }

    private ResponseEntity<CommonDto> created(Object data) {
        return new ResponseEntity<>(CommonDto.builder()
                .httpStatus(HttpStatus.CREATED.value())
                .data(data)
                .build(), HttpStatus.CREATED);
    }
}
