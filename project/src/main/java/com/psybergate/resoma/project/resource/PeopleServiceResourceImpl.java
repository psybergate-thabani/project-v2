package com.psybergate.resoma.project.resource;

import com.psybergate.resoma.project.dto.ValidationDTO;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;

@Getter
@Component
public class PeopleServiceResourceImpl implements PeopleServiceResource {

    private final RestTemplate restTemplate;

    private final String apiUrl;

    public PeopleServiceResourceImpl(RestTemplate restTemplate, @Value("${service.people.apiUrl}") String apiUrl) {
        this.apiUrl = apiUrl;
        this.restTemplate = restTemplate;
    }

    public ValidationDTO exist(UUID employeeId) {
        String uri = getApiUrl() + "/v1/employees/" + employeeId + "/validate";
        return restTemplate.getForObject(uri, ValidationDTO.class);
    }
}
