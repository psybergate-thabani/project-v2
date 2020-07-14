package com.psybergate.resoma.project.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class AllocationDTO {

    private UUID projectId;

    private UUID employeeId;

    private LocalDate startDate;

    private LocalDate expectedEndDate;

    private LocalDate endDate;

}
