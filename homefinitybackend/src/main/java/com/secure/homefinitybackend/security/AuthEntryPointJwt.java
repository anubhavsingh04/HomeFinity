package com.secure.homefinitybackend.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;

@Component
@Slf4j
public class AuthEntryPointJwt implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        log.error("Unauthorized error: {}", authException.getMessage());

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        String body = String.format(
                "{\"timestamp\":\"%s\",\"status\":401,\"message\":\"Unauthorized: %s\",\"path\":\"%s\"}",
                LocalDateTime.now(),
                authException.getMessage().replace("\"", "\\\""),
                request.getRequestURI().replace("\"", "\\\"")
        );

        response.getWriter().write(body);
    }
}
