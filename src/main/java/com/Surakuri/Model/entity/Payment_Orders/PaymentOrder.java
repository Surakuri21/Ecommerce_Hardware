package com.Surakuri.Model.entity.Payment_Orders;

import com.Surakuri.Domain.PaymentOrderStatus;
import com.Surakuri.Model.dto.PaymentDetails;
import com.Surakuri.Model.entity.User_Cart.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * Represents a top-level payment transaction that groups one or more {@link Order}s.
 *
 * <p>In a scenario where a single customer checkout can result in multiple orders
 * (e.g., items from different sellers), the {@code PaymentOrder} acts as the master record
 * for the entire financial transaction. It holds the total amount paid and the payment details.</p>
 */
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "payment_orders")
public class PaymentOrder {

    /**
     * The unique identifier for the payment order. This is the primary key.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_order_id")
    private Long id;

    /**
     * The total amount for the entire transaction, aggregating the totals of all associated orders.
     */
    @Column(name = "amount", nullable = false)
    private BigDecimal amount;

    /**
     * The overall status of the payment (e.g., PENDING, COMPLETED, FAILED).
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private PaymentOrderStatus status = PaymentOrderStatus.PENDING;

    /**
     * An embedded object containing details about the payment method, transaction ID, and amount.
     * {@link AttributeOverride} is used to rename the columns from the {@link PaymentDetails} embeddable
     * to avoid naming conflicts with columns in this entity (e.g., "amount" is renamed to "payment_amount").
     */
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "paymentMethod", column = @Column(name = "payment_method")),
            @AttributeOverride(name = "paymentId", column = @Column(name = "transaction_reference_id")),
            @AttributeOverride(name = "status", column = @Column(name = "payment_status")),
            @AttributeOverride(name = "amount", column = @Column(name = "payment_amount"))
    })
    private PaymentDetails paymentDetails;

    /**
     * The user who initiated the payment order.
     * This relationship is ignored during JSON serialization to prevent circular dependencies.
     */
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private User user;

    /**
     * The set of individual orders that are covered by this single payment transaction.
     * This relationship is ignored during JSON serialization to prevent infinite loops.
     */
    @OneToMany(mappedBy = "paymentOrder", cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<Order> orders = new HashSet<>();

    /**
     * The timestamp when the payment order was first created.
     */
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    /**
     * The timestamp of the last update to the payment order.
     */
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    /**
     * A lifecycle callback method that automatically sets the {@code createdAt} timestamp
     * before the entity is first persisted. This method is invoked by JPA, not directly.
     */
    @PrePersist
    protected void onCreate() { this.createdAt = LocalDateTime.now(); }

    /**
     * A lifecycle callback method that automatically updates the {@code updatedAt} timestamp
     * whenever the entity is updated. This method is invoked by JPA, not directly.
     */
    @PreUpdate
    protected void onUpdate() { this.updatedAt = LocalDateTime.now(); }
}