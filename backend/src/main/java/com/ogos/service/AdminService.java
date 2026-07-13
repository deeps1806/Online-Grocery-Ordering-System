package com.ogos.service;

import com.ogos.dto.UserResponse;

import java.util.List;

public interface AdminService {

    List<UserResponse> getAllUsers();

    UserResponse getUserById(Long id);

    UserResponse updateUserStatus(Long id, String status);

    void deleteUser(Long id);
}
