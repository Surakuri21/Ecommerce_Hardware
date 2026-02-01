package com.Surakuri.shared.config;

import com.Surakuri.features.seller.User_Role;
import com.Surakuri.features.user.User;
import com.Surakuri.features.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataSeeder implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Override
    public void run(String... args) throws Exception {
        System.out.println("---------------------------------------------");
        System.out.println("🚀 STARTING DATABASE CONNECTION TEST...");

        // 1. Check if we already have a test user (to avoid duplicates)
        if (!userRepository.existsByEmail("test@hardware.ph")) {

            User testUser = new User();
            testUser.setEmail("test@hardware.ph");
            testUser.setFirstName("Test");
            testUser.setLastName("Admin");
            testUser.setUsername("admin_test"); // Added since we updated User entity
            testUser.setPassword("password123"); // In real app, this must be hashed!
            testUser.setRole(User_Role.ROLE_ADMIN);

            // Save to MySQL
            userRepository.save(testUser);
            System.out.println("✅ SUCCESS: Test User saved to MySQL database!");
        } else {
            System.out.println("✅ SUCCESS: Database is connected (Test User already exists).");
        }

        System.out.println("---------------------------------------------");
    }
}