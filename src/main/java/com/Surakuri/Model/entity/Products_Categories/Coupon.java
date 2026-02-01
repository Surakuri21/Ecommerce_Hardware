package com.Surakuri.Model.entity.Products_Categories;

// NOTE: Consider moving this to a package like 'com.Surakuri.Model.entity.Other_Business_Entities' for better organization.
import com.Surakuri.Model.entity.User_Cart.User; // Import User
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * Represents a discount coupon that can be applied by users to an order.
 * It defines the discount's value, type, validity period, and usage constraints.
 */
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
// FIX: Exclude the 'users' relationship from equals and hashCode to prevent potential infinite loops.
@EqualsAndHashCode(exclude = "users")
@Table(name = "coupons")
public class Coupon {

    /**
     * The unique identifier for the coupon. This is the primary key.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "coupon_id")
    private Long id;

    /**
     * The unique code that users enter to apply the coupon (e.g., "SALE10", "FREESHIP").
     */
    @Column(unique = true, nullable = false)
    private String code;

    // --- IMPROVEMENT: Flexible Discount System ---
    /**
     * The type of discount offered by the coupon.
     * Examples: "PERCENTAGE", "FIXED_AMOUNT". This makes the system more flexible.
     */
    @Column(name = "discount_type", nullable = false)
    private String discountType; // e.g., "PERCENTAGE" or "FIXED_AMOUNT"

    /**
     * The value of the discount.
     * For PERCENTAGE type, this would be 10 for 10%.
     * For FIXED_AMOUNT type, this would be the monetary value (e.g., 100.00).
     * Using BigDecimal is crucial for financial accuracy.
     */
    @Column(name = "discount_value", nullable = false)
    private BigDecimal discountValue;

    /**
     * The date from which the coupon is valid.
     */
    @Column(name = "valid_from", nullable = false)
    private LocalDate validFrom;

    /**
     * The date until which the coupon is valid (inclusive).
     */
    @Column(name = "valid_until", nullable = false)
    private LocalDate validUntil;

    /**
     * The minimum total order amount required to be eligible for this coupon.
     */
    @Column(name = "min_order_amount")
    private BigDecimal minOrderAmount;

    // --- IMPROVEMENT: Usage Tracking ---
    /**
     * The maximum number of times this coupon can be used in total across all users. Null means unlimited.
     */
    @Column(name = "usage_limit")
    private Integer usageLimit;

    /**
     * The current number of times this coupon has been used.
     */
    @Column(name = "times_used")
    private int timesUsed = 0;

    /**
     * A flag to indicate if the coupon is currently active and can be used.
     */
    @Column(name = "is_active")
    private boolean isActive = true;

    /**
     * The set of users who have used this coupon.
     * This is the inverse side of the many-to-many relationship defined in the User entity.
     * `mappedBy` indicates that the `usedCoupons` field in the `User` entity owns the relationship.
     */
    @ManyToMany(mappedBy = "usedCoupons")
    @JsonIgnore
    private Set<User> users = new HashSet<>();
}