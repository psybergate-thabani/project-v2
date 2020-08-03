package com.psybergate.resoma.project.config;

import com.psybergate.people.api.resource.PeopleApi;
import com.psybergate.people.api.resource.impl.PeopleApiRestImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfiguration {

    @Bean
    public PeopleApi peopleApi() {
        return new PeopleApiRestImpl();
    }
}
