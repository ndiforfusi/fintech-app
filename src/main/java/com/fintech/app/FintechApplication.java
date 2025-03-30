package com.fintech.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import jakarta.annotation.PostConstruct; // ✅ Updated import

@SpringBootApplication
public class FintechApplication {

    public static void main(String[] args) {
        SpringApplication.run(FintechApplication.class, args);
        System.out.println("🚀 FinTech Expense Tracker started successfully.");
    }

    @PostConstruct
    public void init() {
        // 🔧 You can add initial seed logic here (e.g. default cards or demo data)
        System.out.println("✅ Initialization complete.");
    }
}
