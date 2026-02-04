package com.Surakuri.shared.config;

import com.Surakuri.features.seller.User_Role;
import com.Surakuri.features.user.User;
import com.Surakuri.features.user.UserService; // Use UserService
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder; // Import PasswordEncoder
import org.springframework.stereotype.Component;

@Component
public class DataSeeder implements CommandLineRunner {

    @Autowired
    private UserService userService; // Use Service instead of Repository

    @Autowired
    private PasswordEncoder passwordEncoder; // Inject PasswordEncoder

    @Override
    public void run(String... args) throws Exception {
        System.out.println("---------------------------------------------");
        System.out.println("🚀 STARTING DATABASE CONNECTION TEST...");

        // 1. Check if we already have a test user (to avoid duplicates)
        if (!userService.existsByEmail("test@hardware.ph")) {

            User testUser = new User();
            testUser.setEmail("test@hardware.ph");
            testUser.setFirstName("Test");
            testUser.setLastName("Admin");
            testUser.setUsername("admin_test");
            testUser.setPassword(passwordEncoder.encode("password123")); // Hash the password!
            testUser.setRole(User_Role.ROLE_ADMIN);
            testUser.setActive(true); // Ensure user is active

            // Save to MySQL
            userService.saveUser(testUser);
            System.out.println("✅ SUCCESS: Test User saved to MySQL database!");
        } else {
            System.out.println("✅ SUCCESS: Database is connected (Test User already exists).");
        }

        System.out.println("---------------------------------------------");
    }
}