package com.harmoni.auth.application.port.in;

import com.harmoni.auth.web.dto.AssignmentDto;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Input port for assigning scoped roles to users.
 */
public interface UserRoleManagementUseCase {

    List<AssignmentDto> findAssignments(Long userId);

    Map<Long, List<AssignmentDto>> findAssignments(Collection<Long> userIds);

    void replaceAssignments(Long userId, List<AssignmentDto> assignments);
}
