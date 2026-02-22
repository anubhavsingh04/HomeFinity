package com.secure.homefinitybackend.controller;

import com.secure.homefinitybackend.dto.*;
import com.secure.homefinitybackend.exception.BadRequestException;
import com.secure.homefinitybackend.exception.ResourceNotFoundException;
import com.secure.homefinitybackend.exception.UnauthorizedException;
import com.secure.homefinitybackend.models.PasswordResetToken;
import com.secure.homefinitybackend.models.User;
import com.secure.homefinitybackend.repositories.PasswordResetTokenRepository;
import com.secure.homefinitybackend.security.JwtUtils;
import com.secure.homefinitybackend.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.UUID;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final JwtUtils jwtUtils;
    private final PasswordEncoder passwordEncoder;
    private final PasswordResetTokenRepository passwordResetTokenRepository;

    @PostMapping("/register")
    public ResponseEntity<MessageResponse> register(
            @Valid @RequestBody RegisterRequest request) {
        log.info("Registration attempt for email: {}", request.email());
        userService.registerUser(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new MessageResponse("User registered successfully"));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(
            @Valid @RequestBody LoginRequest request) {
        log.info("Login attempt for email: {}", request.email());

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.email(), request.password()));

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        String accessToken = jwtUtils.generateAccessToken(userDetails);
        String refreshToken = jwtUtils.generateRefreshToken(userDetails);

        User user = userService.getUserByEmail(request.email())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        String roleName = user.getRole().getRoleName().name();

        return ResponseEntity.ok(new AuthResponse(
                accessToken, refreshToken,
                user.getEmail(), user.getFullName(), roleName));
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<AuthResponse> refreshToken(
            @Valid @RequestBody RefreshTokenRequest request) {
        String token = request.refreshToken();

        if (!jwtUtils.validateToken(token)) {
            throw new UnauthorizedException("Invalid or expired refresh token");
        }

        if (!jwtUtils.isRefreshToken(token)) {
            throw new BadRequestException("Provided token is not a refresh token");
        }

        String email = jwtUtils.getEmailFromToken(token);
        User user = userService.getUserByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        UserDetails userDetails = org.springframework.security.core.userdetails.User
                .withUsername(user.getEmail())
                .password(user.getPassword())
                .authorities(user.getRole().getRoleName().name())
                .build();

        String newAccessToken = jwtUtils.generateAccessToken(userDetails);
        String roleName = user.getRole().getRoleName().name();

        return ResponseEntity.ok(new AuthResponse(
                newAccessToken, token,
                user.getEmail(), user.getFullName(), roleName));
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<MessageResponse> forgotPassword(
            @Valid @RequestBody PasswordResetRequest request) {
        log.info("Password reset requested for email: {}", request.email());

        User user = userService.getUserByEmail(request.email())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No account found with that email"));

        String token = UUID.randomUUID().toString();
        Instant expiry = Instant.now().plusSeconds(3600);

        PasswordResetToken resetToken = new PasswordResetToken(token, expiry, user);
        passwordResetTokenRepository.save(resetToken);

        // TODO: Send email with reset link containing the token
        log.info("Password reset token generated for user: {}", user.getEmail());

        return ResponseEntity.ok(new MessageResponse(
                "Password reset link has been sent to your email"));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<MessageResponse> resetPassword(
            @Valid @RequestBody ChangePasswordRequest request) {
        PasswordResetToken resetToken = passwordResetTokenRepository
                .findByToken(request.token())
                .orElseThrow(() -> new BadRequestException("Invalid reset token"));

        if (resetToken.isUsed()) {
            throw new BadRequestException("Reset token has already been used");
        }

        if (resetToken.getExpiryDate().isBefore(Instant.now())) {
            throw new BadRequestException("Reset token has expired");
        }

        User user = resetToken.getUser();
        user.setPassword(passwordEncoder.encode(request.newPassword()));
        userService.updateUser(user);

        resetToken.setUsed(true);
        passwordResetTokenRepository.save(resetToken);

        return ResponseEntity.ok(new MessageResponse("Password has been reset successfully"));
    }
}
