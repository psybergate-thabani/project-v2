package com.psybergate.resoma.project.repository;

import com.psybergate.resoma.project.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ProjectRepository extends JpaRepository<Project, UUID> {

    Project findByIdAndDeleted(UUID id, boolean deleted);

    List<Project> findAllByDeleted(boolean deleted);

    @Query(value = "SELECT p FROM Project p LEFT JOIN Allocation a ON p.id = a.projectId WHERE a.employeeId = :employeeId" )
    List<Project> findByAllocationsContainingEmployeeId(UUID employeeId);
}
