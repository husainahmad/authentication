package com.harmoni.auth.adapter.in;

import com.harmoni.auth.application.port.in.PermissionManagementUseCase;
import com.harmoni.auth.web.dto.CommonDto;
import com.harmoni.auth.web.dto.PermissionDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for permission administration.
 */
@RestController
@RequestMapping("/api/v1/auth/permissions")
@RequiredArgsConstructor
public class PermissionController {

    private final PermissionManagementUseCase permissionManagementUseCase;

    @GetMapping
    @PreAuthorize("hasAnyAuthority('rbac:manage', 'rbac:read', 'employee:view')")
    public ResponseEntity<CommonDto> findAll() {
        return ok(permissionManagementUseCase.findAll());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('rbac:manage', 'rbac:read', 'employee:view')")
    public ResponseEntity<CommonDto> findById(@PathVariable Long id) {
        return ok(permissionManagementUseCase.findById(id));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('rbac:manage')")
    public ResponseEntity<CommonDto> create(@RequestBody PermissionDto permissionDto) {
        return created(permissionManagementUseCase.create(permissionDto));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('rbac:manage')")
    public ResponseEntity<CommonDto> update(@PathVariable Long id,
                                            @RequestBody PermissionDto permissionDto) {
        return ok(permissionManagementUseCase.update(id, permissionDto));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('rbac:manage')")
    public ResponseEntity<CommonDto> delete(@PathVariable Long id) {
        permissionManagementUseCase.delete(id);
        return ok(null);
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
