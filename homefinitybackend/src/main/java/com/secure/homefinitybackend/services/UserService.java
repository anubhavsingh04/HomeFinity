package com.secure.homefinitybackend.services;

import com.secure.homefinitybackend.dto.RegisterRequest;
import com.secure.homefinitybackend.models.User;

import java.util.Optional;

public interface UserService {
    User registerUser(RegisterRequest request);
    Optional<User> getUserByEmail(String email);
    User updateUser(User user);
}
