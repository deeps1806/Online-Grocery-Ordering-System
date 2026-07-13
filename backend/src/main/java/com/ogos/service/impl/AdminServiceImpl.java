package com.ogos.service.impl;

import com.ogos.dto.UserResponse;
import com.ogos.entity.User;
import com.ogos.entity.UserStatus;
import com.ogos.exception.ResourceNotFoundException;
import com.ogos.mapper.EntityMapper;
import com.ogos.repository.UserRepository;
import com.ogos.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final UserRepository userRepository;

    @Override
    public List<UserResponse> getAllUsers() {
        return userRepository.findAll().stream()
                .map(EntityMapper::toUserResponse)
                .collect(Collectors.toList());
    }

    @Override
    public UserResponse getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
        return EntityMapper.toUserResponse(user);
    }

    @Override
    @Transactional
    public UserResponse updateUserStatus(Long id, String status) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
        try {
            user.setStatus(UserStatus.valueOf(status.toUpperCase()));
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid status. Use ACTIVE or BLOCKED");
        }
        User saved = userRepository.save(user);
        return EntityMapper.toUserResponse(saved);
    }

    @Override
    @Transactional
    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
        userRepository.delete(user);
    }
}
