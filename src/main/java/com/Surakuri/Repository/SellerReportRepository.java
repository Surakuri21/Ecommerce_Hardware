package com.Surakuri.Repository;

import com.Surakuri.Model.entity.Other_Business_Entities.SellerReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface SellerReportRepository extends JpaRepository<SellerReport, Long> {

    // ==========================================
    // 1. SELLER DASHBOARD (My Stats)
    // ==========================================

    // Fetch the specific report for the logged-in Seller
    // Usage: When Seller loads home screen -> sellerReportRepo.findBySellerId(currentSellerId)
    Optional<SellerReport> findBySellerId(Long sellerId);


    // ==========================================
    // 2. ADMIN ANALYTICS (Platform Health)
    // ==========================================

    // Find the Top 10 Sellers by Total Sales (Highest Grossing)
    // Usage: Admin Dashboard "Top Performers" widget
    List<SellerReport> findTop10ByOrderByTotalSalesDesc();

    // Calculate Total Platform Gross Sales (Sum of all sellers' sales)
    // Usage: "How much money did the entire platform handle?"
    @Query("SELECT SUM(sr.totalSales) FROM SellerReport sr")
    BigDecimal calculateTotalPlatformSales();
}