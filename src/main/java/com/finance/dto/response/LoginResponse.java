package com.finance.dto.response;


// src/main/java/com/finance/dto/response/LoginResponse.java

import lombok.Builder;
import lombok.Data;
import com.finance.model.Role;

@Data
@Builder
public class LoginResponse {
    private Long userId;
    private String username;
    private String fullName;
    private Role role;
    private String message;
}
