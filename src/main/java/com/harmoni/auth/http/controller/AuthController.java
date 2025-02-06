package com.harmoni.auth.http.controller;

import com.harmoni.auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> request) {
        return authService.authenticate(request.get("username"), request.get("password"))
                .map(token -> ResponseEntity.ok(Map.of("token", token)))
                .blockOptional().orElse(ResponseEntity.badRequest().build());
    }
}
