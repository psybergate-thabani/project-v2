package com.psybergate.resoma.project.resource;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(name = "people-service", path = "api/people")
public interface PeopleServiceClient {

    @GetMapping(value = "/v1/employees/{employeeId}/valid")
    boolean validateEmployee(@PathVariable UUID employeeId);
}
