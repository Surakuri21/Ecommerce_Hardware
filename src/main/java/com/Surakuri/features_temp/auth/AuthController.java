package com.Surakuri.features.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    // ==========================================
    // 1. REGISTER ENDPOINT
    // URL: http://localhost:2121/auth/signup
    // ==========================================
    @PostMapping("/signup")
    public ResponseEntity<String> createUserHandler(@RequestBody SignupRequest req) {
        authService.registerUser(req);
        return new ResponseEntity<>("Registration Successful!", HttpStatus.CREATED);
    }

    // ==========================================
    // 2. LOGIN ENDPOINT
    // URL: http://localhost:2121/auth/signin
    // ==========================================
    @PostMapping("/signin")
    public ResponseEntity<AuthResponse> loginUserHandler(@RequestBody LoginRequest req) {
        AuthResponse response = authService.loginUser(req);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}