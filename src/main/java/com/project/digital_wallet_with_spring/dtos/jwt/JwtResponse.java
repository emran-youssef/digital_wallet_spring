package com.project.digital_wallet_with_spring.dtos.jwt;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class JwtResponse {
    private String token;
}
