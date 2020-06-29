package com.psybergate.resoma.project.controller;

import com.psybergate.resoma.project.dto.AllocationDTO;
import com.psybergate.resoma.project.dto.TaskDTO;
import com.psybergate.resoma.project.dto.ValidationDTO;
import com.psybergate.resoma.project.entity.Allocation;
import com.psybergate.resoma.project.entity.Project;
import com.psybergate.resoma.project.entity.Task;
import com.psybergate.resoma.project.service.ProjectService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.ValidationException;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping(path = "/api/project")
public class ProjectController {

    private ProjectService projectService;

    @Value("${eureka.instance.instance-id}")
    private String instanceId;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @PostMapping("v1/project-entries")
    public ResponseEntity<Project> captureProject(@RequestBody @Valid Project project) {
        Project savedProject = projectService.captureProject(project);
        return ResponseEntity.ok(savedProject);
    }

    @GetMapping(value = "v1/project-entries/{projectId}", params = {"deleted"})
    public ResponseEntity<Project> retrieveProject(@PathVariable UUID projectId, @RequestParam("deleted") boolean deleted) {
        return ResponseEntity.ok(projectService.retrieveProject(projectId, deleted));
    }

    @GetMapping(value = "v1/project-entries", params = {"deleted"})
    public ResponseEntity<List<Project>> retrieveProjects(@RequestParam("deleted") Boolean deleted) {
        List<Project> body = projectService.retrieveProjects(deleted);
        return ResponseEntity.ok(body);
    }

    @PutMapping("v1/project-entries/{projectId}")
    public ResponseEntity<Project> updateProject(@PathVariable UUID projectId, @RequestBody @Valid Project project) {
        if (!projectId.equals(project.getId()))
            throw new ValidationException("Id in request body does not match Id in url path");
        return ResponseEntity.ok(projectService.updateProject(project));
    }

    @PutMapping(value = "v1/project-entries/{projectId}/tasks")
    public ResponseEntity<Task> addTaskToTheProject(@RequestBody TaskDTO newTask,
                                                    @PathVariable UUID projectId) {

        if (!projectId.equals(newTask.getProjectId()))
            throw new ValidationException("Project id and projectId in taskDTO does not match.");

        return ResponseEntity.ok(projectService.addTaskToProject(newTask.getTask(), projectId));
    }

    @GetMapping(value = "v1/project-entries/{projectId}/tasks", params = {"deleted"})
    public ResponseEntity<Set<Task>> retrieveTasksByProjectId(@PathVariable UUID projectId,
                                                              @RequestParam("deleted") Boolean deleted) {
        return ResponseEntity.ok(projectService.retrieveTasks(projectId, deleted));
    }

    @DeleteMapping("v1/project-entries/{projectId}")
    public ResponseEntity<Void> deleteProject(@PathVariable UUID projectId) {
        projectService.deleteProject(projectId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("v1/project-entries/{projectId}/tasks/{taskId}")
    public ResponseEntity<Void> deleteTask(@PathVariable UUID projectId, @PathVariable UUID taskId) {
        projectService.deleteTask(projectId, taskId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("v1/project-entries/{projectId}/allocations")
    public ResponseEntity<Set<Allocation>> retrieveProjectAllocations(@PathVariable UUID projectId) {
        return ResponseEntity.ok(projectService.retrieveAllocations(projectId));
    }

    @GetMapping(value = "v1/project-entries/{projectId}/allocations", params = "deleted")
    public ResponseEntity<Set<Allocation>> retrieveProjectAllocations(@PathVariable UUID projectId, Boolean deleted) {
        return ResponseEntity.ok(projectService.retrieveAllocations(projectId, deleted));
    }

    @GetMapping("v1/project-entries/{projectId}/allocations/{allocationId}")
    public ResponseEntity<Allocation> retrieveAllocation(@PathVariable UUID projectId, @PathVariable UUID allocationId) {
        return ResponseEntity.ok(projectService.retrieveAllocation(projectId, allocationId));
    }

    @PostMapping("v1/project-entries/{projectId}/allocations")
    public ResponseEntity<Allocation> allocateEmployee(@PathVariable UUID projectId, @RequestBody AllocationDTO allocationDTO) {
        if (!projectId.equals(allocationDTO.getProjectId()))
            throw new ValidationException("Project id and projectId in AllocationDTO does not match.");
        Allocation allocation = new Allocation(allocationDTO.getEmployeeId());
        return ResponseEntity.ok(projectService.allocateEmployee(projectId, allocation));
    }

    @PostMapping("v1/project-entries/{projectId}/allocations/{allocationId}")
    public ResponseEntity<Allocation> reallocateEmployee(@PathVariable UUID projectId, @PathVariable UUID allocationId) {
        return ResponseEntity.ok(projectService.reallocateEmployee(projectId, allocationId));
    }

    @DeleteMapping("v1/project-entries/{projectId}/allocations/{allocationId}")
    public ResponseEntity<Void> deallocateEmployee(@PathVariable UUID projectId, @PathVariable UUID allocationId) {
        projectService.deallocateEmployee(projectId, allocationId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("v1/project-entries/{projectId}/tasks/{taskId}/validate")
    public ResponseEntity<ValidationDTO> validateTask(@PathVariable UUID projectId, @PathVariable UUID taskId) {
        return ResponseEntity.ok(projectService.validateTask(projectId, taskId));
    }

    @GetMapping("v1/project-entries/{projectId}/validate")
    public ResponseEntity<ValidationDTO> validateProject(@PathVariable UUID projectId) {
        log.info("Instance Id: {}", instanceId);
        return ResponseEntity.ok(projectService.validateProject(projectId));
    }
}
