package com.ogos.service.impl;

import com.ogos.dto.ChangePasswordRequest;
import com.ogos.dto.UserProfileRequest;
import com.ogos.dto.UserResponse;
import com.ogos.entity.User;
import com.ogos.exception.InvalidCredentialsException;
import com.ogos.mapper.EntityMapper;
import com.ogos.repository.UserRepository;
import com.ogos.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserResponse getProfile(User user) {
        return EntityMapper.toUserResponse(user);
    }

    @Override
    @Transactional
    public UserResponse updateProfile(User user, UserProfileRequest request) {
        if (request.getName() != null && !request.getName().isBlank()) {
            user.setFirstName(request.getName());
        }
        if (request.getPhone() != null && !request.getPhone().isBlank()) {
            user.setPhone(request.getPhone());
        }
        User saved = userRepository.save(user);
        return EntityMapper.toUserResponse(saved);
    }

    @Override
    @Transactional
    public void changePassword(User user, ChangePasswordRequest request) {
        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new InvalidCredentialsException("Current password is incorrect");
        }
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
    }
}
