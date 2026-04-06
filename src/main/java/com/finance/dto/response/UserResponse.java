package com.finance.dto.response;


// src/main/java/com/finance/dto/response/UserResponse.java

import lombok.Builder;
import lombok.Data;
import com.finance.model.Role;
import java.time.LocalDateTime;

@Data
@Builder
public class UserResponse {
    private Long id;
    private String username;
    private String email;
    private String fullName;
    private Role role;
    private boolean active;
    private LocalDateTime createdAt;
}

