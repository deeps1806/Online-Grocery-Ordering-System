package com.ogos.service;

import com.ogos.dto.AuthResponse;
import com.ogos.dto.LoginRequest;
import com.ogos.dto.RegisterRequest;

public interface AuthService {

    AuthResponse register(RegisterRequest request);

    AuthResponse login(LoginRequest request);
}