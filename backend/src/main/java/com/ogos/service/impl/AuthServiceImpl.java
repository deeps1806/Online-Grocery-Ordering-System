package com.ogos.service.impl;

import com.ogos.dto.*;
import com.ogos.entity.Role;
import com.ogos.entity.User;
import com.ogos.exception.DuplicateEmailException;
import com.ogos.exception.InvalidCredentialsException;
import com.ogos.mapper.EntityMapper;
import com.ogos.repository.UserRepository;
import com.ogos.security.JwtService;
import com.ogos.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Override
    public AuthResponse register(RegisterRequest request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateEmailException("Email already exists: " + request.getEmail());
        }

        Role role = Role.ROLE_CUSTOMER;
        if (request.getRole() != null && !request.getRole().isEmpty()) {
            try {
                role = Role.valueOf(request.getRole());
                // Only allow CUSTOMER and VENDOR registration
                if (role == Role.ROLE_ADMIN) {
                    throw new IllegalArgumentException("Admin registration is not allowed");
                }
            } catch (IllegalArgumentException e) {
                if (e.getMessage().contains("Admin")) throw e;
                role = Role.ROLE_CUSTOMER;
            }
        }

        User user = User.builder()
                .firstName(request.getName())
                .lastName("")
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .phone(request.getPhone())
                .role(role)
                .build();

        User savedUser = userRepository.save(user);

        String token = jwtService.generateToken(savedUser);

        return AuthResponse.builder()
                .message("Registration Successful")
                .token(token)
                .user(EntityMapper.toUserResponse(savedUser))
                .build();
    }

    @Override
    public AuthResponse login(LoginRequest request) {

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );
        } catch (BadCredentialsException e) {
            throw new InvalidCredentialsException("Invalid email or password");
        }

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new InvalidCredentialsException("Invalid email or password"));

        String token = jwtService.generateToken(user);

        return AuthResponse.builder()
                .message("Login Successful")
                .token(token)
                .user(EntityMapper.toUserResponse(user))
                .build();
    }
}
