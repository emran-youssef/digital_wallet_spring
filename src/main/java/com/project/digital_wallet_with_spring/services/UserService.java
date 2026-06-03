package com.project.digital_wallet_with_spring.services;

import com.project.digital_wallet_with_spring.dtos.jwt.JwtResponse;
import com.project.digital_wallet_with_spring.dtos.user.LoginRequest;
import com.project.digital_wallet_with_spring.dtos.user.RegisterUserRequest;
import com.project.digital_wallet_with_spring.dtos.user.UserResponseDto;

public interface UserService {

    UserResponseDto register(RegisterUserRequest request);
    JwtResponse login(LoginRequest request);
    UserResponseDto getUserByEmail(String email);
    UserResponseDto getUserById(Long id);
}
