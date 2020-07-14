package com.psybergate.resoma.project.service;

import com.psybergate.people.api.PeopleApi;
import com.psybergate.resoma.project.dto.ValidationDTO;
import com.psybergate.resoma.project.entity.Allocation;
import com.psybergate.resoma.project.entity.Project;
import com.psybergate.resoma.project.entity.ProjectType;
import com.psybergate.resoma.project.entity.Task;
import com.psybergate.resoma.project.repository.ProjectRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.validation.ValidationException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProjectServiceTest {

    @Mock
    private ProjectRepository projectRepository;
    private ProjectService projectService;
    private Project project;
    @Mock
    private PeopleApi peopleApi;

    @BeforeEach
    void setUp() {
        projectService = new ProjectServiceImpl(projectRepository, peopleApi);
        project = new Project("proj1", "First Project", "client1", LocalDate.now(), null, ProjectType.BILLABLE);
    }

    @Test
    void shouldCreateProject_whenProjectIsCaptured() {
        //Arrange
        when(projectRepository.save(project)).thenReturn(project);

        //Act
        Project resultProject = projectService.captureProject(project);

        //Assert
        assertNotNull(resultProject);
        assertEquals(project, resultProject);
    }

    @Test
    void shouldReturnProject_whenProjectIsRetrieved() {
        //Arrange
        UUID id = project.getId();
        when(projectRepository.findByIdAndDeleted(id, false)).thenReturn(project);

        //Act
        Project resultProject = projectService.retrieveProject(id, false);

        //Assert
        assertNotNull(resultProject);
        assertEquals(project, resultProject);
    }

    @Test
    void shouldThrowValidationException_whenInvalidProjectIsRetrieved() {
        //Arrange
        UUID id = UUID.randomUUID();
        when(projectRepository.findByIdAndDeleted(id, false)).thenReturn(null);

        //Assert
        assertThrows(ValidationException.class, () ->
                // Act
                projectService.retrieveProject(id, false)
        );
    }

    @Test
    void shouldReturnListOfProjects_whenProjectsAreRetrieved() {
        //Arrange
        Project projectA = new Project("projA", "First Project", "client1", LocalDate.now(), null, ProjectType.BILLABLE);
        Project projectB = new Project("projB", "Second Project", "client1", LocalDate.now(), null, ProjectType.BILLABLE);
        Project projectC = new Project("projC", "Third Project", "client1", LocalDate.now(), null, ProjectType.BILLABLE);
        List<Project> projects = Arrays.asList(projectA, projectB, projectC);
        when(projectRepository.findAllByDeleted(false)).thenReturn(projects);

        //Act
        List<Project> resultProjects = projectService.retrieveProjects(false);

        //Assert
        assertEquals(3, resultProjects.size());
        assertEquals(projects, resultProjects);
        resultProjects.forEach(p -> assertTrue(resultProjects.contains(p)));
        verify(projectRepository, times(1)).findAllByDeleted(false);
    }

    @Test
    void shouldUpdateProject_whenProjectIsUpdated() {
        //Arrange
        when(projectRepository.getOne(project.getId())).thenReturn(project);
        when(projectRepository.save(project)).thenReturn(project);
        //Act
        Project resultProject = projectService.updateProject(project);

        //Assert
        assertNotNull(resultProject);
        assertEquals(project, resultProject);

    }

    @Test
    void shouldDeleteProject_whenProjectIsDeleted() {
        //Arrange
        UUID id = project.getId();
        Task task = new Task("task1", "First Task", false);
        Project project = new Project();
        project.getTasks().add(task);
        when(projectRepository.findByIdAndDeleted(id, false)).thenReturn(project);
        when(projectRepository.save(project)).thenReturn(project);

        //Act
        projectService.deleteProject(id);

        //Assert
        verify(projectRepository, times(1)).findByIdAndDeleted(id, false);
        verify(projectRepository, times(1)).save(project);
        assertTrue(project.isDeleted());
        assertTrue(task.isDeleted());
    }

    @Test
    void shouldThrowValidationException_whenProjectIsDeletedAndProjectDoesNotExist() {
        //Arrange
        UUID id = project.getId();
        when(projectRepository.findByIdAndDeleted(id, false)).thenReturn(null);

        //Act
        assertThrows(ValidationException.class, () -> {
            projectService.deleteProject(id);
        });
    }

    @Test
    void shouldAddTaskToProject_whenTaskIsAddedToProject() {
        //Arrange
        Task task = new Task("task1", "First Task", false);
        UUID id = project.getId();

        when(projectRepository.findByIdAndDeleted(id, false)).thenReturn(project);
        when(projectRepository.save(any(Project.class))).thenReturn(project);

        //Act
        Task resultTask = projectService.addTaskToProject(task, id);

        //Assert
        assertNotNull(resultTask);
        assertEquals(task, resultTask);
        verify(projectRepository, times(1)).findByIdAndDeleted(id, false);
        verify(projectRepository, times(1)).save(any(Project.class));

    }

    @Test
    void shouldThrowValidationException_whenTaskIsAddedToProjectThatDoesNotExist() {
        //Arrange
        Task task = new Task("task1", "First Task", false);
        UUID id = project.getId();
        when(projectRepository.findByIdAndDeleted(id, false)).thenReturn(null);

        //Act And Assert
        assertThrows(ValidationException.class, () -> {
            Task resultTask = projectService.addTaskToProject(task, id);
        });
    }

    @Test
    void shouldRetrieveTasks_whenTasksAreRetrieved() {
        //Arrange
        Task task = new Task("task1", "First Task", false);
        Project project = new Project();
        project.getTasks().add(task);
        when(projectRepository.findByIdAndDeleted(project.getId(), false)).thenReturn(project);

        //Act
        Set<Task> resultTasks = projectService.retrieveTasks(project.getId(), false);

        //Assert
        assertNotNull(resultTasks);
        assertEquals(1, resultTasks.size());
    }

    @Test
    void shouldDeleteTask_whenTaskIsDeleted() {
        //Arrange
        Task task = new Task("task1", "First Task", false);
        task.setId(UUID.randomUUID());
        UUID id = task.getId();
        Project project = new Project();
        project.setId(UUID.randomUUID());
        project.getTasks().add(task);
        when(projectRepository.findByIdAndDeleted(project.getId(), false)).thenReturn(project);
        when(projectRepository.save(project)).thenReturn(project);

        //Act
        projectService.deleteTask(project.getId(), id);

        //Assert
        verify(projectRepository, times(1)).findByIdAndDeleted(project.getId(), false);
        verify(projectRepository, times(1)).save(project);
        assertTrue(task.isDeleted());
    }

    @Test
    void shouldReturnTask_whenTaskIsRetrievedById() {
        //Arrange
        Task task = new Task("task1", "First Task", false);
        Project project = new Project();
        project.setId(UUID.randomUUID());
        project.getTasks().add(task);
        UUID taskId = task.getId();
        when(projectRepository.findByIdAndDeleted(project.getId(), false)).thenReturn(project);

        //Act
        Task resultTask = projectService.retrieveTask(project.getId(), taskId);

        //Assert
        assertNotNull(resultTask);
        assertEquals(task, resultTask);
        verify(projectRepository, times(1)).findByIdAndDeleted(project.getId(), false);
    }

    @Test
    void shouldReturnAllocation_whenAllocationIsRetrievedById() {
        //Arrange
        Allocation allocation = new Allocation(UUID.randomUUID(), LocalDate.now(), LocalDate.now());
        allocation.setId(UUID.randomUUID());
        UUID allocationId = allocation.getId();
        Project project = new Project();
        project.setId(UUID.randomUUID());
        project.getAllocations().add(allocation);
        when(projectRepository.findByIdAndDeleted(project.getId(), false)).thenReturn(project);

        //Act
        Allocation resultAllocation = projectService.retrieveAllocation(project.getId(), allocationId);

        //Assert
        assertNotNull(resultAllocation);
        assertEquals(allocation, resultAllocation);
        verify(projectRepository, times(1)).findByIdAndDeleted(project.getId(), false);
    }

    @Test
    void shouldDeallocate_whenEmployeeIsDeallocated() {
        //Arrange
        Allocation allocation = new Allocation();
        allocation.setId(UUID.randomUUID());
        Project project = new Project();
        project.setId(UUID.randomUUID());
        project.getAllocations().add(allocation);
        when(projectRepository.findByIdAndDeleted(project.getId(), false)).thenReturn(project);
        when(projectRepository.save(project)).thenReturn(project);

        //Act
        projectService.deallocateEmployee(project.getId(), allocation.getId());

        //Assert
        verify(projectRepository, times(1)).findByIdAndDeleted(project.getId(), false);
        verify(projectRepository, times(1)).save(project);
    }

    @Test
    void shouldRetrieveAllocations_whenAllocationsAreRetrievedByProject() {
        //Arrange
        Allocation allocation = new Allocation();
        Project project = new Project();
        project.getAllocations().add(allocation);
        when(projectRepository.getOne(project.getId()))
                .thenReturn(project);

        //Act
        Set<Allocation> resultAllocations = projectService.retrieveAllocations(project.getId());

        //Assert
        assertNotNull(resultAllocations);
        assertEquals(1, resultAllocations.size());
    }

    @Test
    void shouldAllocateEmployeeToProject_whenEmployeeIsAllocatedToProject() {
        //Arrange
        Allocation allocation = new Allocation();
        allocation.setId(UUID.randomUUID());
        allocation.setEmployeeId(UUID.randomUUID());
        Project project = new Project();
        project.getAllocations().add(allocation);
        project.setId(UUID.randomUUID());
        when(projectRepository.getOne(project.getId())).thenReturn(project);
        when(projectRepository.save(project)).thenReturn(project);
        when(peopleApi.validateEmployee(allocation.getEmployeeId(), "http://localhost:8083")).thenReturn(true);

        //Act
        Allocation resultAllocation = projectService.allocateEmployee(project.getId(), allocation);

        //Assert
        assertNotNull(resultAllocation);
        assertEquals(allocation, resultAllocation);
        verify(projectRepository, times(1)).getOne(project.getId());
        verify(projectRepository, times(1)).save(project);
        verify(peopleApi).validateEmployee(allocation.getEmployeeId(), "http://localhost:8083");
    }

    @Test
    void shouldRetrieveAllocations_whenAllocationsAreRetrievedByProjectId() {
        //Arrange
        Allocation allocation = new Allocation();
        Project project = new Project();
        project.setId(UUID.randomUUID());
        project.getAllocations().add(allocation);
        when(projectRepository.getOne(project.getId()))
                .thenReturn(project);

        //Act
        Set<Allocation> resultAllocations = projectService.retrieveAllocations(project.getId());

        //Assert
        assertNotNull(resultAllocations);
        assertEquals(1, resultAllocations.size());
        verify(projectRepository, times(1)).getOne(project.getId());
    }

    @Test
    public void shouldReturnValidationDTO_whenValidatingProject() {
        UUID id = UUID.randomUUID();
        when(projectRepository.findByIdAndDeleted(id, false)).thenReturn(project);

        ValidationDTO validProject = projectService.validateProject(id);

        assertTrue(validProject.getExist());
    }

    @Test
    public void shouldReturnValidationDTO_whenValidatingTask() {
        UUID id = UUID.randomUUID();
        Project project = new Project();
        project.setId(id);
        Task task = new Task();
        task.setId(id);
        project.addTask(task);
        when(projectRepository.findByIdAndDeleted(id, false)).thenReturn(project);

        ValidationDTO validTask = projectService.validateTask(project.getId(), task.getId());
        ValidationDTO invalidTask = projectService.validateTask(project.getId(), null);

        assertTrue(validTask.getExist());
        assertFalse(invalidTask.getExist());
    }


}
