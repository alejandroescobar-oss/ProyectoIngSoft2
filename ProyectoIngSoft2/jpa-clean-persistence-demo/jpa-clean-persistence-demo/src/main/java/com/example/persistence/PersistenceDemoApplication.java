package com.example.persistence;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/** Boots the layered JPA persistence demonstration. */
@SpringBootApplication
public class PersistenceDemoApplication {
    /** Starts the Spring Boot application. @param args command-line arguments */
    public static void main(String[] args) {
        SpringApplication.run(PersistenceDemoApplication.class, args);
    }
}
