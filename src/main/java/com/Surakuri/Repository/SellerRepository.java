package com.Surakuri.Repository;

import com.Surakuri.Domain.AccountStatus;
import com.Surakuri.Model.entity.Other_Business_Entities.Seller;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SellerRepository extends JpaRepository<Seller, Long> {

    Optional<Seller> findByEmail(String email);

    // For Admin to see all pending applications
    List<Seller> findByAccountStatus(AccountStatus status);

    // For checking duplicates during registration
    boolean existsByEmail(String email);
}