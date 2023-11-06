package com.example.userservice.vo;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

// import lombok.AllArgsConstructor;
import lombok.Data;

@Component
@Data
// @AllArgsConstructor
public class Greeting {
    
    @Value("${greeting.message}")
    private String message;
}
