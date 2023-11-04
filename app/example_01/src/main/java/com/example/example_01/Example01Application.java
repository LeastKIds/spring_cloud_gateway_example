package com.example.example_01;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

@SpringBootApplication
@EnableConfigServer
public class Example01Application {

    public static void main(String[] args) {
        SpringApplication.run(Example01Application.class, args);
    }

}
