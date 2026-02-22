package com.secure.homefinitybackend.dto;

import java.time.LocalDate;

public record UserInfoResponse(
        Long userId,
        String fullName,
        String email,
        String mobile,
        String role,
        String gender,
        LocalDate dateOfBirth,
        String address,
        String district,
        String state,
        String pinCode,
        boolean enabled
) {}
