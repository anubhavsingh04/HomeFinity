package com.secure.homefinitybackend.services.impl;

import com.secure.homefinitybackend.dto.RegisterRequest;
import com.secure.homefinitybackend.exception.BadRequestException;
import com.secure.homefinitybackend.exception.ResourceNotFoundException;
import com.secure.homefinitybackend.models.AppRole;
import com.secure.homefinitybackend.models.Role;
import com.secure.homefinitybackend.models.User;
import com.secure.homefinitybackend.repositories.RoleRepository;
import com.secure.homefinitybackend.repositories.UserRepository;
import com.secure.homefinitybackend.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public User registerUser(RegisterRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new BadRequestException("Email is already registered");
        }
        if (userRepository.existsByMobile(request.mobile())) {
            throw new BadRequestException("Mobile number is already registered");
        }

        User user = new User(request.fullName(), request.email(),
                request.mobile(), passwordEncoder.encode(request.password()));

        final AppRole appRole;
        if (request.roleName() != null) {
            try {
                AppRole requested = AppRole.valueOf(request.roleName());
                if (requested == AppRole.ROLE_ADMIN) {
                    throw new BadRequestException("Cannot self-assign admin role");
                }
                appRole = requested;
            } catch (IllegalArgumentException e) {
                throw new BadRequestException("Invalid role: " + request.roleName());
            }
        } else {
            appRole = AppRole.ROLE_TENANT;
        }

        Role role = roleRepository.findByRoleName(appRole)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Role not found: " + appRole.name()));

        user.setRole(role);
        return userRepository.save(user);
    }

    @Override
    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    @Transactional
    public User updateUser(User user) {
        return userRepository.save(user);
    }
}
