package com.harmoni.auth.adapter.in;

import com.harmoni.auth.application.port.in.UserRoleManagementUseCase;
import com.harmoni.auth.web.dto.AssignmentBatchRequest;
import com.harmoni.auth.web.dto.AssignmentDto;
import com.harmoni.auth.web.dto.CommonDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for scoped role assignments.
 */
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class UserRoleController {

    private final UserRoleManagementUseCase userRoleManagementUseCase;

    @GetMapping("/users/{userId}/assignments")
    @PreAuthorize("hasAnyAuthority('employee:view', 'rbac:manage', 'user:read')")
    public ResponseEntity<CommonDto> findAssignments(@PathVariable Long userId) {
        return ok(userRoleManagementUseCase.findAssignments(userId));
    }

    @GetMapping("/users/ids")
    @PreAuthorize("hasAnyAuthority('employee:view', 'rbac:manage', 'user:read')")
    public ResponseEntity<CommonDto> findUserIds(
            @RequestParam(name = "roleId", required = false) Integer roleId,
            @RequestParam(name = "scopeType", required = false) String scopeType) {
        return ok(userRoleManagementUseCase.findUserIds(roleId, scopeType));
    }

    @PutMapping("/users/{userId}/assignments")
    @PreAuthorize("hasAnyAuthority('employee:edit', 'rbac:manage', 'user:write')")
    public ResponseEntity<CommonDto> replaceAssignments(@PathVariable Long userId,
                                                        @RequestBody List<AssignmentDto> assignments) {
        userRoleManagementUseCase.replaceAssignments(userId, assignments);
        return ok(null);
    }

    @PostMapping("/assignments/batch")
    @PreAuthorize("hasAnyAuthority('employee:view', 'rbac:manage', 'user:read')")
    public ResponseEntity<CommonDto> batch(@RequestBody AssignmentBatchRequest request) {
        return ok(userRoleManagementUseCase.findAssignments(request.getUserIds()));
    }

    private ResponseEntity<CommonDto> ok(Object data) {
        return ResponseEntity.ok(CommonDto.builder()
                .httpStatus(HttpStatus.OK.value())
                .data(data)
                .build());
    }
}
