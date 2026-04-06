package com.finance.config;

// src/main/java/com/finance/config/DataInitializer.java

import com.finance.dto.request.UserCreateRequest;
import com.finance.model.Role;
import com.finance.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {
    private final UserService userService;

    @Override
    public void run(String... args) {
        try {
            // Create admin user
            UserCreateRequest admin = new UserCreateRequest();
            admin.setUsername("admin");
            admin.setEmail("admin@finance.com");
            admin.setPassword("admin123");
            admin.setFullName("System Administrator");
            admin.setRole(Role.ADMIN);
            userService.createUser(admin);
            log.info("Admin user created - username: admin, password: admin123");

            // Create analyst user
            UserCreateRequest analyst = new UserCreateRequest();
            analyst.setUsername("analyst");
            analyst.setEmail("analyst@finance.com");
            analyst.setPassword("analyst123");
            analyst.setFullName("Financial Analyst");
            analyst.setRole(Role.ANALYST);
            userService.createUser(analyst);
            log.info("Analyst user created - username: analyst, password: analyst123");

            // Create viewer user
            UserCreateRequest viewer = new UserCreateRequest();
            viewer.setUsername("viewer");
            viewer.setEmail("viewer@finance.com");
            viewer.setPassword("viewer123");
            viewer.setFullName("Dashboard Viewer");
            viewer.setRole(Role.VIEWER);
            userService.createUser(viewer);
            log.info("Viewer user created - username: viewer, password: viewer123");

        } catch (Exception e) {
            log.warn("Users already exist or error creating: {}", e.getMessage());
        }
    }
}
