package com.psybergate.resoma.projectv2.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "allocation", uniqueConstraints = @UniqueConstraint(columnNames = {"project_id", "employee_id"}))
public class Allocation extends BaseEntity {

    @NotNull(message = "{employee.notblank}")
    @Column(name = "employee_id")
    private UUID employeeId;
}
