package com.psybergate.resoma.projectapi.resource;


import java.util.UUID;

public interface ProjectApi {
    ValidationDTO validateProject(UUID employeeId);

    ValidationDTO validateTask(UUID projectId, UUID taskId);
}
