package com.finance;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class FinanceApplication {

	public static void main(String[] args) {

        SpringApplication.run(FinanceApplication.class, args);
        System.out.println("Main Application running...");
        // src/main/java/com/finance/FinanceApplication.java

                System.out.println("========================================");
                System.out.println("Finance Backend is Running!");
                System.out.println("API Base URL: http://localhost:8080/api");
                System.out.println("H2 Console: http://localhost:8080/h2-console");
                System.out.println("========================================");
            }
        }

