package com.finance.controller;


// src/main/java/com/finance/controller/UserController.java

import com.finance.dto.response.UserResponse;
import com.finance.model.Role;
import com.finance.model.User;
import com.finance.service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

        import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<UserResponse>> getAllUsers(HttpSession session) {
        User currentUser = (User) session.getAttribute("currentUser");

        // Only admins can list all users
        if (currentUser == null || currentUser.getRole() != Role.ADMIN) {
            throw new RuntimeException("Only admins can list all users");
        }

        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUser(@PathVariable Long id, HttpSession session) {
        User currentUser = (User) session.getAttribute("currentUser");

        // Users can see their own info, admins can see anyone
        if (currentUser == null) {
            throw new RuntimeException("Not authenticated");
        }

        if (currentUser.getRole() != Role.ADMIN && !currentUser.getId().equals(id)) {
            throw new RuntimeException("You can only view your own profile");
        }

        User user = userService.getUserById(id);
        UserResponse response = UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .fullName(user.getFullName())
                .role(user.getRole())
                .active(user.isActive())
                .createdAt(user.getCreatedAt())
                .build();

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deactivateUser(@PathVariable Long id, HttpSession session) {
        User currentUser = (User) session.getAttribute("currentUser");

        if (currentUser == null) {
            throw new RuntimeException("Not authenticated");
        }

        userService.deactivateUser(id, currentUser);
        return ResponseEntity.ok(Map.of("message", "User deactivated successfully"));
    }
}

