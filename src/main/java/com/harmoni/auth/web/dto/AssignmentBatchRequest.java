package com.harmoni.auth.web.dto;

import lombok.Data;

import java.util.List;

/**
 * Request body for fetching assignments for several users at once.
 */
@Data
public class AssignmentBatchRequest {

    private List<Long> userIds;
}
