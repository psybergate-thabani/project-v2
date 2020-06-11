package com.psybergate.resoma.projectv2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class ProjectV2Application {

	public static void main(String[] args) {
		SpringApplication.run(ProjectV2Application.class, args);
	}

}
