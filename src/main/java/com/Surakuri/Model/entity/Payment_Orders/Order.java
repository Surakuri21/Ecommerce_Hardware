package com.Surakuri.Model.entity.Payment_Orders;

import com.Surakuri.Domain.PaymentOrderStatus;
import com.Surakuri.Model.entity.User_Cart.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal; // IMPORT THIS for accurate money math
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * Represents a customer's order. It contains summary information like total amount,
 * shipping details, and status, and links to the specific items included in the order.
 */
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(exclude = {"user", "orderItems", "paymentOrder"})
@Table(name = "orders") // Matches SQL table 'orders'
public class Order {

    /**
     * The unique identifier for the order. This is the primary key.
     */
    @Id
    // FIX: Use IDENTITY for MySQL. AUTO can cause issues with existing tables.
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id") // Maps to SQL Primary Key
    private Long id;

    // --- NEW FIELDS FOR HARDWARE E-COMMERCE ---

    /**
     * A unique, human-readable reference number for the order (e.g., "ORD-2025-8821").
     * Useful for customer service and tracking.
     */
    @Column(name = "order_reference_number", unique = true, nullable = false)
    private String orderReferenceNumber;

    /**
     * The cost of shipping for this order. Stored separately for clear invoicing.
     */
    @Column(name = "shipping_fee")
    private BigDecimal shippingFee;

    /**
     * A snapshot of the shipping address at the time of order placement.
     * Stored as text to prevent issues if the user later updates or deletes the original Address entity.
     */
    @Column(name = "shipping_address_snapshot", columnDefinition = "TEXT")
    private String shippingAddressSnapshot;

    // --- EXISTING FIELDS (Optimized) ---

    // FIX: Changed Double to BigDecimal
    /**
     * The total cost of the order, including all items and shipping fees.
     * Using BigDecimal is crucial for accurate financial calculations.
     */
    @Column(name = "total_amount", nullable = false)
    private BigDecimal totalAmount;

    /**
     * The current status of the order (e.g., PENDING, PROCESSING, SHIPPED, DELIVERED).
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private PaymentOrderStatus status = PaymentOrderStatus.PENDING;

    /**
     * The date and time when the order was created.
     * Managed automatically by the `@PrePersist` lifecycle hook.
     */
    @Column(name = "order_date") // Replaces createdAt to match DB standard
    private LocalDateTime createdAt;

    /**
     * The date and time when the order was last updated.
     * Managed automatically by the `@PreUpdate` lifecycle hook.
     */
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // --- RELATIONSHIPS ---

    /**
     * Many-to-one relationship with the User. Many orders can belong to a single user.
     * This is the owning side of the relationship.
     */
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false) // Maps to SQL Foreign Key
    @JsonIgnore
    private User user;

    /**
     * One-to-many relationship with OrderItem. An order is composed of multiple items.
     * `cascade = CascadeType.ALL` ensures that order items are saved/deleted along with the order.
     * `orphanRemoval = true` ensures that if an OrderItem is removed from this set, it's deleted from the database.
     */
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<OrderItem> orderItems = new HashSet<>();

    /**
     * Many-to-one relationship with PaymentOrder. This links the order to its specific payment transaction details.
     */
    @ManyToOne
    @JoinColumn(name = "payment_order_id")
    @JsonIgnore
    private PaymentOrder paymentOrder;

    // --- LIFECYCLE HOOKS ---
    /**
     * Automatically sets the `createdAt` timestamp before the entity is first persisted.
     */
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    /**
     * Automatically updates the `updatedAt` timestamp before the entity is updated.
     */
    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}