package com.lamentaciones.supervision.infrastructure.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(basePackages = 
    "com.lamentaciones.supervision.infrastructure.persistence.mongo.repositories")
public class MongoConfig {
}