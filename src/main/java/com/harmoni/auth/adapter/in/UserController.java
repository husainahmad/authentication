package com.harmoni.auth.adapter.in;

import com.harmoni.auth.application.port.in.UserManagementUseCase;
import com.harmoni.auth.web.dto.CommonDto;
import com.harmoni.auth.web.dto.UserDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for handling user management operations such as registration,
 * update, and deactivation.
 */
@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserManagementUseCase userManagementUseCase;

    /**
     * Registers a new user.
     *
     * @param userDto the user details to be registered
     * @return {@link ResponseEntity} with HTTP 201 status on success
     */
    @PostMapping("")
    @PreAuthorize("hasAnyAuthority('user:write', 'employee:edit', 'rbac:manage')")
    public ResponseEntity<CommonDto> register(@Valid @RequestBody UserDto userDto) {
        int row = userManagementUseCase.register(userDto);
        log.debug("User Created : {}", row);

        CommonDto restAPIResponse = CommonDto.builder()
                .httpStatus(HttpStatus.CREATED.value())
                .data(row)
                .build();

        return new ResponseEntity<>(restAPIResponse, HttpStatus.CREATED);
    }

    /**
     * Deactivates a user by username.
     *
     * @param username the username of the user to deactivate
     * @return {@link ResponseEntity} with HTTP 200 status on success
     */
    @DeleteMapping("/{username}")
    @PreAuthorize("hasAnyAuthority('user:delete', 'employee:edit', 'rbac:manage')")
    public ResponseEntity<CommonDto> deactivate(@PathVariable String username) {
        int row = userManagementUseCase.deactivate(username);
        log.debug("User Deactivated : {}", row);

        CommonDto restAPIResponse = CommonDto.builder()
                .httpStatus(HttpStatus.OK.value())
                .build();

        return new ResponseEntity<>(restAPIResponse, HttpStatus.OK);
    }

    /**
     * Updates the user details for the given username.
     *
     * @param username the username to update
     * @param userDto  the updated user information
     * @return {@link ResponseEntity} with HTTP 200 status on success
     */
    @PutMapping("/{username}")
    @PreAuthorize("hasAnyAuthority('user:write', 'employee:edit', 'rbac:manage')")
    public ResponseEntity<CommonDto> update(@PathVariable String username, @Valid @RequestBody UserDto userDto) {
        userDto.setUsername(username);
        int row = userManagementUseCase.update(userDto);
        log.debug("User Updated : {}", row);

        CommonDto restAPIResponse = CommonDto.builder()
                .httpStatus(HttpStatus.OK.value())
                .build();

        return new ResponseEntity<>(restAPIResponse, HttpStatus.OK);
    }
}
