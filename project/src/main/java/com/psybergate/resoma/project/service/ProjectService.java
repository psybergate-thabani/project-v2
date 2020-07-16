package com.psybergate.resoma.project.service;

import com.psybergate.resoma.project.dto.ValidationDTO;
import com.psybergate.resoma.project.entity.Allocation;
import com.psybergate.resoma.project.entity.Project;
import com.psybergate.resoma.project.entity.Task;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface ProjectService {

    Project captureProject(Project newProject);

    Project retrieveProject(UUID id, boolean deleted);

    List<Project> retrieveProjects(boolean deleted);

    Project updateProject(Project project);

    void deleteProject(UUID id);

    Task addTaskToProject(Task newTask, UUID projectId);

    Set<Task> retrieveTasks(UUID projectId, boolean deleted);

    void deleteTask(UUID projectId, UUID taskId);

    Task retrieveTask(UUID projectId, UUID taskId);

    Set<Allocation> retrieveAllocations(UUID projectId);

    Set<Allocation> getAllocations(UUID employeeId);

    Allocation allocateEmployee(UUID projectId, Allocation allocation);

    void deallocateEmployee(UUID projectId, UUID allocationId);

    Allocation retrieveAllocation(UUID projectId, UUID allocationId);

    Set<Allocation> retrieveAllocations(UUID projectId, Boolean deleted);

    Allocation reallocateEmployee(UUID projectId, UUID allocationId);

    ValidationDTO validateTask(UUID projectId, UUID taskId);

    ValidationDTO validateProject(UUID projectId);
}
