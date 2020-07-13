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

    @NotNull(message = "{projectid.notnull}")
    private UUID projectId;

    @NotNull(message = "{employeeid.notblank}")
    private UUID employeeId;

    @NotNull(message = "{startdate.notblank}")
    private LocalDate startDate;

    @NotNull(message = "{expectedenddate.notblank}")
    private LocalDate expectedEndDate;

    private LocalDate endDate;

}
