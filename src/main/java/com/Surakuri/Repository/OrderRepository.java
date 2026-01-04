package com.Surakuri.Repository;

import com.Surakuri.Domain.PaymentOrderStatus;
import com.Surakuri.Model.entity.Payment_Orders.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    // ==========================================
    // 1. CUSTOMER QUERIES (My Orders)
    // ==========================================

    // Find all orders for a specific user (e.g., "Order History" page)
    // Orders are sorted by newest first (OrderByCreatedAtDesc)
    List<Order> findByUserIdOrderByCreatedAtDesc(Long userId);

    // Find a specific order by its Reference Number (e.g., User searching "ORD-2025-123")
    Optional<Order> findByOrderReferenceNumber(String orderReferenceNumber);


    // ==========================================
    // 2. ADMIN QUERIES (Dashboard)
    // ==========================================

    // Find orders by Status (e.g., "Show me all PENDING orders to pack")
    List<Order> findByStatus(PaymentOrderStatus status);

    // Total Sales Calculation (Sum of all PAID orders)
    // Useful for an "Earnings Report" widget
    @Query("SELECT SUM(o.totalAmount) FROM Order o WHERE o.status = 'PAID'")
    Double calculateTotalSales();
}