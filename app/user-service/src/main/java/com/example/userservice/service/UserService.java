package com.example.userservice.service;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.example.userservice.dto.UserDto;
import com.example.userservice.jpa.UserEntity;

import jakarta.servlet.http.HttpServletResponse;

public interface UserService {
    UserDto createUser(UserDto userDto);

    UserDto getUserByUserId(String userId);
    Iterable<UserEntity> getUserByAll();

    UserEntity getUserByEmail(UserDto userDto, HttpServletResponse response);
}
