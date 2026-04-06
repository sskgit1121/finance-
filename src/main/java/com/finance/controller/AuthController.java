package com.finance.controller;

// src/main/java/com/finance/controller/AuthController.java

import com.finance.dto.request.UserCreateRequest;
import com.finance.dto.response.LoginResponse;
import com.finance.dto.response.UserResponse;
import com.finance.model.User;
import com.finance.service.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@Valid @RequestBody UserCreateRequest request) {
        UserResponse user = userService.createUser(request);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody Map<String, String> credentials,
                                               HttpSession session) {
        String username = credentials.get("username");
        String password = credentials.get("password");

        User user = userService.authenticate(username, password);

        // Store user in session (mock authentication)
        session.setAttribute("currentUser", user);

        LoginResponse response = LoginResponse.builder()
                .userId(user.getId())
                .username(user.getUsername())
                .fullName(user.getFullName())
                .role(user.getRole())
                .message("Login successful")
                .build();

        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpSession session) {
        session.invalidate();
        return ResponseEntity.ok(Map.of("message", "Logout successful"));
    }

    @GetMapping("/me")
    public ResponseEntity<LoginResponse> getCurrentUser(HttpSession session) {
        User currentUser = (User) session.getAttribute("currentUser");
        if (currentUser == null) {
            throw new RuntimeException("Not logged in");
        }

        return ResponseEntity.ok(LoginResponse.builder()
                .userId(currentUser.getId())
                .username(currentUser.getUsername())
                .fullName(currentUser.getFullName())
                .role(currentUser.getRole())
                .message("Current user")
                .build());
    }
}
