package com.psybergate.resoma.project.messaging;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.psybergate.resoma.project.service.ProjectService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class MessageListener {

    private ProjectService projectService;

    private Gson gson;

    @Autowired
    public MessageListener(Gson gson) {
        this.gson = gson;
    }

    @RabbitListener(queues = "${queue.people.change.name}")
    public void receiveEmployeeMessage(String message) {
        log.info("Received employee event {}", message);
        JsonObject messageJsonObject = gson.fromJson(message, JsonObject.class);
        EventType eventType = gson.fromJson(messageJsonObject.get("eventType"), EventType.class);
        JsonObject employee = messageJsonObject.get("employee").getAsJsonObject();
        switch (eventType) {
            case TERMINATED:
                log.info("Employee Update: " + employee);
                log.info(employee.get("endDate").getAsString());
                break;
        }
    }
}
