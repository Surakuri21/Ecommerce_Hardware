package com.Surakuri.Model.entity.Other_Business_Entities;

import com.Surakuri.Domain.PaymentStatus;
import com.Surakuri.Model.entity.Payment_Orders.Order;
import com.Surakuri.Model.entity.User_Cart.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Represents a single financial transaction in the system, acting as a ledger entry.
 *
 * <p>This entity records every movement of money, such as a customer payment for an order.
 * It links the customer, the seller, and the specific order involved, providing a clear
 * audit trail for all financial activities.</p>
 */
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(exclude = {"customer", "order", "seller"}) // Exclude relationships
@Table(name = "transactions")
public class Transaction {

    /**
     * The unique identifier for the transaction. This is the primary key.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // --- RELATIONSHIPS ---

    /**
     * The customer who initiated the transaction (the payer).
     * This relationship is ignored during JSON serialization to prevent circular dependencies.
     */
    @ManyToOne
    @JoinColumn(name = "customer_id")
    @JsonIgnore
    private User customer;

    /**
     * The specific order for which this transaction was made.
     */
    @OneToOne
    @JoinColumn(name = "order_id")
    private Order order;

    /**
     * The seller who will receive the funds from this transaction (the payee).
     * This is crucial for multi-vendor platforms.
     */
    @ManyToOne
    @JoinColumn(name = "seller_id")
    private Seller seller;

    // --- FINANCIAL DETAILS ---

    /**
     * The unique reference ID from the payment gateway (e.g., GCash reference, Stripe Payment ID).
     */
    @Column(name = "transaction_reference")
    private String transactionId;

    /**
     * The amount of money involved in the transaction.
     */
    @Column(nullable = false)
    private BigDecimal amount;

    /**
     * The current status of the transaction (e.g., PENDING, COMPLETED, FAILED, REFUNDED).
     */
    @Enumerated(EnumType.STRING)
    private PaymentStatus status = PaymentStatus.PENDING;

    /**
     * The timestamp when the transaction occurred.
     */
    private LocalDateTime date = LocalDateTime.now();
}