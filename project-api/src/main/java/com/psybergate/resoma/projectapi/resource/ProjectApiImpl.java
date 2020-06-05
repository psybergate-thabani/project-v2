package com.psybergate.resoma.projectapi.resource;

import lombok.Getter;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;

@Getter
@Component
public class ProjectApiImpl implements ProjectApi {

    private final RestTemplate restTemplate;

    private static final String API_BASE_URL;

    static {
        API_BASE_URL = "http://localhost:8083";
    }

    public ProjectApiImpl() {
        this(new RestTemplate());
    }

    public ProjectApiImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public ValidationDTO validateProject(UUID projectId) {
        String uri = "/api/project/v1/project-entries/" + projectId + "/validate";
        return restTemplate.getForObject(API_BASE_URL + uri, ValidationDTO.class);
    }

    public ValidationDTO validateTask(UUID projectId, UUID taskId) {
        String uri = "/api/project/v1/project-entries/" + projectId + "/tasks/" + taskId + "/validate";
        return restTemplate.getForObject(API_BASE_URL + uri, ValidationDTO.class);
    }
}
