package com.psybergate.resoma.project.messaging.impl;

import com.google.gson.Gson;
import com.psybergate.resoma.project.messaging.ErrorMessage;
import com.psybergate.resoma.project.messaging.ProjectMessageResource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import javax.validation.constraints.NotNull;

@Slf4j
@Component
public class ProjectMessageResourceImpl implements ProjectMessageResource {

    private final RabbitTemplate rabbitTemplate;

    private final String projectExchangeName;

    private final String projectRoute;

    private final Gson gson;

    @Autowired
    public ProjectMessageResourceImpl(final RabbitTemplate rabbitTemplate,
                                      @Value("${queue.project.exchange}") final String projectExchangeName,
                                      @Value("${queue.project.route}") final String projectRoute,
                                      final Gson gson) {
        this.rabbitTemplate = rabbitTemplate;
        this.projectExchangeName = projectExchangeName;
        this.projectRoute = projectRoute;
        this.gson = gson;
    }

    @Override
    @Transactional
    public void broadcastErrorMessage(ErrorMessage message) {
        send(projectExchangeName, projectRoute, gson.toJson(message));
    }

    private void send(@NotNull final String exchangeName, @NotNull final String route, @NotNull final String message) {
        rabbitTemplate.convertAndSend(exchangeName, route, message);
    }
}
