package com.psybergate.resoma.projectv2.controller.dto;

import com.psybergate.resoma.projectv2.entity.Task;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@Data
@NoArgsConstructor
public class TaskDTO {

    @NotNull(message = "{task.notnull}")
    private Task task;

    @NotNull(message = "{projectid.notnull}")
    private UUID projectId;
}
