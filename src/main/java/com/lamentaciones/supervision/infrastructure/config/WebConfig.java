package com.lamentaciones.supervision.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**") 
                        .allowedOrigins(
                            "http://localhost:5173",
                            "https://supervision-lamentaciones-d8d3a5bpd6gvcva8.canadacentral-01.azurewebsites.net",
                            "https://lamentaciones-frontend-git-preprod-juan-caballeros-projects.vercel.app",
                            "https://lamentaciones-frontend-r0g3i6enb-juan-caballeros-projects.vercel.app",
                            "https://lamentaciones-frontend.vercel.app",
                            "https://lamentaciones-frontend-juan-caballeros-projects.vercel.app"
                        )
                        .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")
                        .allowedHeaders("*")
                        .allowCredentials(true)
                        .maxAge(3600); 
            }
        };
    }
}