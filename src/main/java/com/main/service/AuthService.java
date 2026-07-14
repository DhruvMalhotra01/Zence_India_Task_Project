package com.main.service;

import com.main.dto.JwtResponse;
import com.main.dto.LoginRequest;
import com.main.dto.RegisterRequest;

public interface AuthService {

    String register(RegisterRequest request);

    JwtResponse login(LoginRequest request);

}