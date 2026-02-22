package com.secure.homefinitybackend.dto;

public record AuthResponse(
        String accessToken,
        String refreshToken,
        String tokenType,
        String email,
        String fullName,
        String role
) {
    public AuthResponse(String accessToken, String refreshToken,
                        String email, String fullName, String role) {
        this(accessToken, refreshToken, "Bearer", email, fullName, role);
    }
}
