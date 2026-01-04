package com.Surakuri.Repository;

import com.Surakuri.Domain.User_Role; // Import your Enum
import com.Surakuri.Model.entity.User_Cart.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // ==========================================
    // 1. AUTHENTICATION (Login)
    // ==========================================

    // Find by Email (Common for e-commerce login)
    // Returns Optional<> to avoid NullPointerExceptions if user isn't found
    Optional<User> findByEmail(String email);

    // Find by Username (For Admin/Staff login)
    Optional<User> findByUsername(String username);

    // Find by Username OR Email (Allow users to log in with either)
    @Query("SELECT u FROM User u WHERE u.username = :input OR u.email = :input")
    Optional<User> findByUsernameOrEmail(@Param("input") String input);


    // ==========================================
    // 2. REGISTRATION VALIDATION (Prevent Duplicates)
    // ==========================================

    // Used during Sign Up to check if email is taken
    boolean existsByEmail(String email);

    // Used to ensure unique usernames
    boolean existsByUsername(String username);


    // ==========================================
    // 3. ADMIN DASHBOARD (Manage Users)
    // ==========================================

    // Find all users with a specific role (e.g., "Show me all Admins")
    List<User> findByRole(User_Role role);

    // Search functionality for Admin Panel
    // Finds users by First Name, Last Name, or Email matching the keyword
    @Query("SELECT u FROM User u WHERE " +
            "LOWER(u.firstName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(u.lastName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(u.email) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<User> searchUsers(@Param("keyword") String keyword);

}