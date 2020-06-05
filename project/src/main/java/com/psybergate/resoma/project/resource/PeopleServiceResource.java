package com.psybergate.resoma.project.resource;


import com.psybergate.resoma.project.dto.ValidationDTO;

import java.util.UUID;

public interface PeopleServiceResource {
    ValidationDTO exist(UUID employeeId);

}
