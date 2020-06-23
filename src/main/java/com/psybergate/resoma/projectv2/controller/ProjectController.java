package com.psybergate.resoma.projectv2.controller;

import com.psybergate.resoma.projectv2.controller.dto.AllocationDTO;
import com.psybergate.resoma.projectv2.controller.dto.TaskDTO;
import com.psybergate.resoma.projectv2.entity.Allocation;
import com.psybergate.resoma.projectv2.entity.Project;
import com.psybergate.resoma.projectv2.entity.Task;
import com.psybergate.resoma.projectv2.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.ValidationException;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@RestController
@RequestMapping(path = "/api/project")
public class ProjectController {

    @Autowired
    private ProjectService projectService;

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
        return ResponseEntity.ok(projectService.retrieveProjects(deleted));
    }

    @PutMapping("v1/project-entries/{projectId}")
    public ResponseEntity<Project> updateProject(@PathVariable UUID projectId, @RequestBody @Valid Project project) {
        if (!projectId.equals(project.getId()))
            throw new ValidationException("Id in request body does not match Id in url path");
        return ResponseEntity.ok(projectService.updateProject(project));
    }

    @PutMapping(value = "v1/project-entries/{projectId}/tasks")
    public ResponseEntity<Task> addTaskToTheProject(@RequestBody @Valid TaskDTO newTask,
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
    public ResponseEntity<Void> deleteTask(@PathVariable UUID taskId) {
        projectService.deleteTask(taskId);
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
    public ResponseEntity<Allocation> retrieveAllocation(@PathVariable UUID allocationId) {
        return ResponseEntity.ok(projectService.retrieveAllocation(allocationId));
    }

    @PostMapping("v1/project-entries/{projectId}/allocations")
    public ResponseEntity<Allocation> allocateEmployee(@PathVariable UUID projectId, @RequestBody @Valid AllocationDTO allocationDTO) {
        if (!projectId.equals(allocationDTO.getProjectId()))
            throw new ValidationException("Project id and projectId in AllocationDTO does not match.");
        return ResponseEntity.ok(projectService.allocateEmployee(projectId, new Allocation(projectId)));
    }

    @PostMapping("v1/project-entries/{projectId}/allocations/{allocationId}")
    public ResponseEntity<Void> reallocateEmployee(@PathVariable UUID projectId, @PathVariable UUID allocationId) {
        projectService.reallocateEmployee(allocationId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("v1/project-entries/{projectId}/allocations/{allocationId}")
    public ResponseEntity<Void> deallocateEmployee(@PathVariable UUID allocationId) {
        projectService.deallocateEmployee(allocationId);
        return ResponseEntity.ok().build();
    }
}
