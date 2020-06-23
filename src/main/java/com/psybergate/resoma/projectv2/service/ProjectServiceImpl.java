package com.psybergate.resoma.projectv2.service;


import com.psybergate.people.api.PeopleApi;
import com.psybergate.resoma.projectv2.entity.Allocation;
import com.psybergate.resoma.projectv2.entity.Project;
import com.psybergate.resoma.projectv2.entity.Task;
import com.psybergate.resoma.projectv2.repository.ProjectRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

    private final PeopleApi peopleApi;

    @Value("${server.people.domain}")
    private String peopleServerDomain;

    @Autowired
    public ProjectServiceImpl(ProjectRepository projectRepository, PeopleApi peopleApi) {
        this.projectRepository = projectRepository;
        this.peopleApi = peopleApi;
    }

    @Override
    @Transactional
    public Project captureProject(@Valid Project newProject) {
        return projectRepository.save(newProject);
    }

    @Override
    public Project retrieveProject(UUID id, boolean deleted) {
        return projectRepository.findByIdAndDeleted(id, deleted);
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
        if (Objects.isNull(project))
            throw new ValidationException("Project does not exist");

        project.setDeleted(true);
        projectRepository.save(project);
    }

    @Override
    @Transactional
    public Task addTaskToProject(@Valid Task newTask, UUID projectId) {
        Project project = projectRepository.findByIdAndDeleted(projectId, false);
        if (Objects.isNull(project))
            throw new ValidationException("Project with id \"" + projectId + "\" does not exist");
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
        Project project = projectRepository.findByIdAndDeleted(projectId, deleted);
        return project.getTasks().stream().filter(task -> task.isDeleted() == deleted)
                .collect(Collectors.toSet());
    }

    @Override
    @Transactional
    public void deleteTask(UUID taskId) {
        Project project = projectRepository.findFirstByTasks_id(taskId);
        project.removeTask(taskId);
        projectRepository.save(project);
    }

    @Override
    public Task retrieveTask(UUID taskId) {
        Project project = projectRepository.findFirstByTasks_id(taskId);
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
        Project project = projectRepository.getOne(projectId);
        if (!peopleApi.validateEmployee(allocation.getEmployeeId(), peopleServerDomain))
            throw new ValidationException("Employee does not exist");
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
    public void deallocateEmployee(UUID allocationId) {
        Project project = projectRepository.findFirstByAllocationsId(allocationId);
        project.removeAllocation(allocationId);
        projectRepository.save(project);
    }

    @Override
    @Transactional
    public Allocation retrieveAllocation(UUID allocationId) {
        Project project = projectRepository.findFirstByAllocationsId(allocationId);
        return project.getAllocation(allocationId);
    }

    @Override
    @Transactional
    public Set<Allocation> retrieveAllocations(UUID projectId, Boolean deleted) {
        Project project = projectRepository.getOne(projectId);
        return project.getAllocations().stream()
                .filter(allocation -> allocation.isDeleted() == deleted)
                .collect(Collectors.toSet());
    }

    @Override
    public void reallocateEmployee(@Valid UUID allocationId) {
        Project project = projectRepository.findFirstByAllocationsId(allocationId);
        checkNull(Objects.isNull(project), "Project does not exists");
        project.removeAllocation(allocationId);
        projectRepository.save(project);
    }

    private void checkNull(boolean aNull, String s) {
        if (aNull) {
            throw new ValidationException(s);
        }
    }
}
