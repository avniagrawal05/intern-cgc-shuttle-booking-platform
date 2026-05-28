package com.hms;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * Main application class for Hospital Management System
 * Spring Boot Application Entry Point
 */
@SpringBootApplication
@ComponentScan(basePackages = {"com.hms"})
@OpenAPIDefinition(
    info = @Info(
        title = "Hospital Management System API",
        version = "1.0.0",
        description = "REST APIs for HMS - Patient, Doctor, Appointment and Billing Management"
    )
)
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
