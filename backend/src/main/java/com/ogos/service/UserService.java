package com.ogos.service;

import com.ogos.dto.ChangePasswordRequest;
import com.ogos.dto.UserProfileRequest;
import com.ogos.dto.UserResponse;
import com.ogos.entity.User;

public interface UserService {

    UserResponse getProfile(User user);

    UserResponse updateProfile(User user, UserProfileRequest request);

    void changePassword(User user, ChangePasswordRequest request);
}
