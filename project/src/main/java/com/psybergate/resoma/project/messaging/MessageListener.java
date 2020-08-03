package com.psybergate.resoma.project.messaging;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.psybergate.resoma.project.service.ProjectService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Slf4j
public class MessageListener {

    private ProjectService projectService;

    private Gson gson;

    @Autowired
    public MessageListener(Gson gson, ProjectService projectService) {
        this.gson = gson;
        this.projectService = projectService;
    }

    @RabbitListener(queues = "${queue.people.change.name}")
    public void receiveEmployeeMessage(String message) {
        log.info("Received employee event {}", message);
        try {
            JsonObject messageJsonObject = gson.fromJson(message, JsonObject.class);
            EventType eventType = gson.fromJson(messageJsonObject.get("eventType"), EventType.class);
            JsonObject employeeJson = gson.fromJson(messageJsonObject.get("employee"), JsonObject.class);
            UUID employeeId = gson.fromJson(employeeJson.get("id"), UUID.class);
            switch (eventType) {
                case TERMINATED:
                    projectService.updateAllocationsEndDate(employeeId);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
