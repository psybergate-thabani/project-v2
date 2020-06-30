package com.psybergate.resoma.project.service;

import com.psybergate.resoma.project.dto.ValidationDTO;
import com.psybergate.resoma.project.entity.Allocation;
import com.psybergate.resoma.project.entity.Project;
import com.psybergate.resoma.project.entity.Task;
import com.psybergate.resoma.project.repository.ProjectRepository;
import com.psybergate.resoma.project.resource.PeopleServiceClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.validation.ValidationException;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ProjectServiceImpl implements ProjectService {

    private ProjectRepository projectRepository;

//    private PeopleApi peopleApi;

    private PeopleServiceClient peopleServiceClient;

    public ProjectServiceImpl(ProjectRepository projectRepository, PeopleServiceClient peopleServiceClient) {
        this.projectRepository = projectRepository;
        this.peopleServiceClient = peopleServiceClient;
    }

    @Override
    @Transactional
    public Project captureProject(@Valid Project newProject) {
        return projectRepository.save(newProject);
    }

    @Override
    @Transactional
    public Project retrieveProject(UUID id, boolean deleted) {
        Project project = projectRepository.findByIdAndDeleted(id, deleted);
        checkNull(Objects.isNull(project), "Project with id \"" + id + "\" does not exists");
        return project;
    }

    @Override
    @Transactional
    public List<Project> retrieveProjects(boolean deleted) {
        return projectRepository.findAllByDeleted(deleted);
    }

    @Override
    @Transactional
    public Project updateProject(@Valid Project project) {
        Project savedProject = projectRepository.getOne(project.getId());
        savedProject.copyUpdatableFields(project);
        return projectRepository.save(savedProject);
    }

    @Override
    @Transactional
    public void deleteProject(UUID id) {
        Project project = retrieveProject(id, false);
        project.setDeleted(true);
        projectRepository.save(project);
    }

    @Override
    @Transactional
    public Task addTaskToProject(@Valid Task newTask, UUID projectId) {
        Project project = retrieveProject(projectId, false);
        project.addTask(newTask);
        project = projectRepository.save(project);
        for (Task task : project.getTasks()) {
            if (task.getCode().equals(newTask.getCode())) {
                newTask = task;
                break;
            }
        }
        return newTask;
    }

    @Override
    @Transactional
    public Set<Task> retrieveTasks(@Valid UUID projectId, boolean deleted) {
        Project project = retrieveProject(projectId, deleted);
        Set<Task> tasks = project.getTasks();
        tasks = tasks.stream().filter(task -> task.isDeleted() == deleted)
                .collect(Collectors.toSet());
        return tasks;
    }

    @Override
    @Transactional
    public void deleteTask(UUID projectId, UUID taskId) {
        Project project = retrieveProject(projectId, false);
        project.removeTask(taskId);
        projectRepository.save(project);
    }

    @Override
    public Task retrieveTask(UUID projectId, UUID taskId) {
        Project project = retrieveProject(projectId, false);
        return project.getTask(taskId);
    }

    @Transactional
    @Override
    public Set<Allocation> retrieveAllocations(UUID projectId) {
        Project project = projectRepository.getOne(projectId);
        return project.getAllocations();
    }

    @Transactional
    @Override
    public Allocation allocateEmployee(UUID projectId, @Valid Allocation allocation) {
//        Boolean isValid = peopleServiceClient.validateEmployee(allocation.getEmployeeId(), "http://localhost:8083");
        Boolean isValid = peopleServiceClient.validateEmployee(allocation.getEmployeeId());
        if (!isValid) {
            throw new ValidationException(String.format("Employee id %s is invalid", allocation.getEmployeeId()));
        }

        Project project = projectRepository.getOne(projectId);
        project.addAllocation(allocation);
        project = projectRepository.save(project);
        for (Allocation tempAllocation : project.getAllocations()) {
            if (allocation.getEmployeeId().equals(tempAllocation.getEmployeeId())) {
                allocation = tempAllocation;
            }
        }
        return allocation;
    }

    @Transactional
    @Override
    public void deallocateEmployee(UUID projectId, UUID allocationId) {
        Project project = retrieveProject(projectId, false);
        project.removeAllocation(allocationId);
        projectRepository.save(project);
    }

    @Override
    @Transactional
    public Allocation retrieveAllocation(UUID projectId, UUID allocationId) {
        Project project = retrieveProject(projectId, false);
        return project.getAllocation(allocationId);
    }

    @Override
    @Transactional
    public Set<Allocation> retrieveAllocations(UUID projectId, Boolean deleted) {
        Project project = retrieveProject(projectId, deleted);
        Set<Allocation> allocations = project.getAllocations().stream()
                .filter(allocation -> allocation.isDeleted() == deleted)
                .collect(Collectors.toSet());
        return allocations;
    }

    @Override
    @Transactional
    public Allocation reallocateEmployee(UUID projectId, @Valid UUID allocationId) {
        Project project = retrieveProject(projectId, false);
        project.getAllocation(allocationId).setDeleted(false);
        projectRepository.save(project);
        return null;
    }

    @Override
    @Transactional
    public ValidationDTO validateTask(UUID projectId, UUID taskId) {
        Project project = projectRepository.findByIdAndDeleted(projectId, false);
        ValidationDTO validationDTO = new ValidationDTO();
        validationDTO.setExist(project.getTask(taskId) != null);
        return validationDTO;
    }

    @Override
    public ValidationDTO validateProject(UUID projectId) {
        Project project = retrieveProject(projectId, false);
        return new ValidationDTO(project != null);
    }

    private void checkNull(boolean aNull, String s) {
        if (aNull) {
            throw new ValidationException(s);
        }
    }
}
