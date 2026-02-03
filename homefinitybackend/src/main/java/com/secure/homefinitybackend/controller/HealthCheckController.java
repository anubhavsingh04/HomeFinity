package com.secure.homefinitybackend.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/api")
public class HealthCheckController {
    @GetMapping("/health-check")
    public ResponseEntity<Map<String,Object>> healthCheck() {
        Map<String, Object> response = new HashMap<>();
        log.info("Health check accessed /api/health-check");
        response.put("Status", "UP");
        response.put("timestamp", LocalDateTime.now().toString());
        response.put("Service", "Homefinity Backend");
        return ResponseEntity.ok(response);
    }
}
