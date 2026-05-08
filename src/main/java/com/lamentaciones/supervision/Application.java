package com.lamentaciones.supervision;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.context.annotation.ComponentScan;
@EnableScheduling
@SpringBootApplication
@ComponentScan(basePackages = "com.lamentaciones.supervision")
@EnableMongoRepositories(basePackages = "com.lamentaciones.supervision.domain.repository")
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}