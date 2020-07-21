package com.psybergate.resoma.project.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity(name = "Allocation")
@Table(name = "allocation", uniqueConstraints = @UniqueConstraint(columnNames = {"project_id", "employee_id"}))
public class Allocation extends BaseEntity {

    @NotNull(message = "{projectid.notnull}")
    @Column(name = "project_id", nullable = false)
    private UUID projectId;

    @NotNull(message = "{employeeid.notblank}")
    @Column(name = "employee_id", nullable = false)
    private UUID employeeId;

    @NotNull(message = "{startdate.notblank}")
    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @NotNull(message = "{expectedenddate.notblank}")
    @Column(name = "expected_end_date", nullable = false)
    private LocalDate expectedEndDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    public Allocation(UUID projectId, UUID employeeId, LocalDate startDate, LocalDate expectedEndDate) {
        this.projectId = projectId;
        this.employeeId = employeeId;
        this.startDate = startDate;
        this.expectedEndDate = expectedEndDate;
    }
}
