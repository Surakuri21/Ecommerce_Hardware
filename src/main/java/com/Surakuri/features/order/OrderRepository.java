package com.Surakuri.features.order;

import com.Surakuri.features.payment.PaymentOrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
interface OrderRepository extends JpaRepository<Order, Long> {

    // ==========================================
    // 1. CUSTOMER QUERIES (My Orders)
    // ==========================================

    List<Order> findByUserIdOrderByCreatedAtDesc(Long userId);

    Optional<Order> findByOrderReferenceNumber(String orderReferenceNumber);


    // ==========================================
    // 2. SELLER QUERIES (Seller Dashboard)
    // ==========================================

    /**
     * Finds all orders that contain at least one product sold by a specific seller.
     * This is the core query for the "My Incoming Orders" feature on the seller dashboard.
     * @param sellerId The ID of the seller.
     * @return A list of distinct orders for the seller, sorted by most recent first.
     */
    @Query("SELECT DISTINCT o FROM Order o JOIN o.orderItems oi JOIN oi.variant v JOIN v.product p WHERE p.seller.id = :sellerId ORDER BY o.createdAt DESC")
    List<Order> findOrdersBySellerId(@Param("sellerId") Long sellerId);

    /**
     * Checks if a specific order contains any products sold by a specific seller.
     * This is used for security checks when a seller tries to update an order.
     */
    @Query("SELECT COUNT(o) > 0 FROM Order o JOIN o.orderItems oi JOIN oi.variant v JOIN v.product p WHERE o.id = :orderId AND p.seller.id = :sellerId")
    boolean existsByOrderIdAndSellerId(@Param("orderId") Long orderId, @Param("sellerId") Long sellerId);


    // ==========================================
    // 3. ADMIN QUERIES (Dashboard)
    // ==========================================

    List<Order> findByStatus(PaymentOrderStatus status);

    @Query("SELECT SUM(o.totalAmount) FROM Order o WHERE o.status = 'PAID'")
    Double calculateTotalSales();
}