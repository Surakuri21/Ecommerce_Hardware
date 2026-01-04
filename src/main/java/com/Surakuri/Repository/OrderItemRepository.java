package com.Surakuri.Repository;

import com.Surakuri.Model.entity.Payment_Orders.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    // ==========================================
    // 1. VERIFIED PURCHASE CHECK (For Reviews)
    // ==========================================

    // Usage: Before allowing a User to post a Review, check if they actually bought the item.
    // We join OrderItem -> Order -> User to verify.
    @Query("SELECT CASE WHEN COUNT(oi) > 0 THEN true ELSE false END " +
            "FROM OrderItem oi " +
            "WHERE oi.order.user.id = :userId AND oi.variant.product.id = :productId")
    boolean existsByUserIdAndProductId(@Param("userId") Long userId,
                                       @Param("productId") Long productId);


    // ==========================================
    // 2. BEST SELLERS ANALYTICS
    // ==========================================

    // Usage: "How many units of this specific SKU (e.g., 40kg Cement) have been sold total?"
    // Useful for inventory forecasting.
    @Query("SELECT SUM(oi.quantity) FROM OrderItem oi WHERE oi.variant.id = :variantId")
    Long getTotalUnitsSoldByVariant(@Param("variantId") Long variantId);

    // Usage: Find all items belonging to a specific Order ID
    List<OrderItem> findByOrderId(Long orderId);
}