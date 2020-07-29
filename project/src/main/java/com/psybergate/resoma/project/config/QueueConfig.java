package com.psybergate.resoma.project.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class QueueConfig {

    @Bean
    public TopicExchange projectExchange(@Value("${queue.project.exchange}") String projectExchangeName) {
        return new TopicExchange(projectExchangeName);
    }

    @Bean
    public Queue projectErrorQueue(@Value("${queue.project.error}") String queueName, @Value("${queue.durable}") String durableStr) {
        boolean durable = Boolean.valueOf(durableStr);
        return new Queue(queueName, durable);
    }

    @Bean
    public Binding projectErrorQueueBinding(Queue projectErrorQueue, TopicExchange projectChange, @Value("${queue.project.route}") String route) {
        return BindingBuilder.bind(projectErrorQueue).to(projectChange).with(route);
    }
}

