package com.main.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class JwtResponse {

    private String token;

    private String tokenType;

    private String username;

    private String role;
}