package com.Surakuri.Repository;

import com.Surakuri.Model.entity.Other_Business_Entities.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    // ==========================================
    // 1. SELLER DASHBOARD (Earnings)
    // ==========================================

    // Find all transactions for a specific Seller
    // Usage: "Show me all the money I made this month"
    List<Transaction> findBySellerId(Long sellerId);

    // Sort by newest first (Show latest sales at the top)
    List<Transaction> findBySellerIdOrderByDateDesc(Long sellerId);


    // ==========================================
    // 2. ORDER LINKING
    // ==========================================

    // Find the payment record for a specific Order
    // Usage: "Is Order #1002 paid yet?"
    Optional<Transaction> findByOrderId(Long orderId);


    // ==========================================
    // 3. SEARCH & SUPPORT
    // ==========================================

    // Find by the external Reference ID (e.g., GCash Ref No.)
    // Usage: Customer complains "I paid via GCash ref 12345". You search "12345" here.
    Optional<Transaction> findByTransactionId(String transactionId);
}