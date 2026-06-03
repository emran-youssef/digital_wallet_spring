package com.project.digital_wallet_with_spring.dtos.user;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequest {

    @NotBlank(message = "Email is required")
    private final String email;

    @NotBlank(message = "Password is required")
    private final String password;

}
