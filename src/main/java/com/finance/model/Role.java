package com.finance.model;

// src/main/java/com/finance/model/Role.java

public enum Role {
    VIEWER,    // Can only view data
    ANALYST,   // Can view and create/edit their own transactions
    ADMIN      // Full access to everything
}