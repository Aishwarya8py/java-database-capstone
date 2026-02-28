package com.smartcare.cms.controller;

import com.smartcare.cms.dto.ApiResponse;
import com.smartcare.cms.dto.JwtResponse;
import com.smartcare.cms.dto.LoginRequest;
import com.smartcare.cms.dto.RegisterRequest;
import com.smartcare.cms.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Authentication controller.
 *
 * POST /api/auth/register  – create a new user account
 * POST /api/auth/login     – authenticate and receive a JWT
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<JwtResponse>> register(
            @Valid @RequestBody RegisterRequest request) {
        JwtResponse jwt = authService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Registration successful", jwt));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<JwtResponse>> login(
            @Valid @RequestBody LoginRequest request) {
        JwtResponse jwt = authService.login(request);
        return ResponseEntity.ok(ApiResponse.ok("Login successful", jwt));
    }
}
