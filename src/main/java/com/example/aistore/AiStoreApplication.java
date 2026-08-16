package com.example.aistore;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class AiStoreApplication {

    public static void main(String[] args) {
        SpringApplication.run(AiStoreApplication.class, args);
    }
}
