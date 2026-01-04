package com.Surakuri.Repository;

import com.Surakuri.Model.entity.Other_Business_Entities.Verification;
import com.Surakuri.Model.entity.User_Cart.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VerificationRepository extends JpaRepository<Verification, Long> {

    // 1. Find by OTP (When user types the code)
    Optional<Verification> findByOtp(String otp);

    // 2. Find by User (To check if they already have a pending OTP)
    Optional<Verification> findByUser(User user);

    // 3. Find by Email (If verifying before login)
    Optional<Verification> findByEmail(String email);
}