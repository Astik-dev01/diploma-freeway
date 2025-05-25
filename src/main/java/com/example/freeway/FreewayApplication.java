package com.example.freeway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class FreewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(FreewayApplication.class, args);
    }

}
