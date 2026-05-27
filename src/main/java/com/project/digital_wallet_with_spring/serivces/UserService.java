package com.project.digital_wallet_with_spring.serivces;

import com.project.digital_wallet_with_spring.dtos.user.RegisterUserRequest;
import com.project.digital_wallet_with_spring.dtos.user.UserResponseDto;

public interface UserService {

    UserResponseDto register(RegisterUserRequest request);
    UserResponseDto getUserByEmail(String username);
    UserResponseDto getUserById(Long id);
}
