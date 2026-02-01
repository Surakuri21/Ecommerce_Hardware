package com.Surakuri.Model.entity.Other_Business_Entities;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal; // Standard for Money
import java.time.LocalDateTime;

/**
 * Represents a summary report of a seller's performance and financial statistics.
 *
 * <p>This entity provides a snapshot of a seller's key metrics, such as total sales,
 * earnings, refunds, and order counts. It is designed to be updated periodically
 * to give sellers an overview of their business activity on the platform.</p>
 */
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "seller_reports")
public class SellerReport {

    /**
     * The unique identifier for the report. This is the primary key.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The seller to whom this report belongs. This establishes a one-to-one relationship.
     */
    @OneToOne
    @JoinColumn(name = "seller_id", referencedColumnName = "seller_id")
    private Seller seller;

    // --- FINANCIAL STATS ---

    /**
     * The total earnings credited to the seller after deductions (e.g., commission, fees).
     */
    @Column(name = "total_earnings")
    private BigDecimal totalEarnings = BigDecimal.ZERO;

    /**
     * The gross revenue generated from all sales before any deductions.
     */
    @Column(name = "total_sales")
    private BigDecimal totalSales = BigDecimal.ZERO;

    /**
     * The total value of all refunds processed for the seller's orders.
     */
    @Column(name = "total_refunds")
    private BigDecimal totalRefunds = BigDecimal.ZERO;

    /**
     * The total amount of tax (e.g., VAT, Withholding Tax) calculated on the seller's sales.
     */
    @Column(name = "total_tax")
    private BigDecimal totalTax = BigDecimal.ZERO;

    /**
     * The net earnings calculated as (Total Sales - Total Refunds - Total Tax).
     */
    @Column(name = "net_earnings")
    private BigDecimal netEarnings = BigDecimal.ZERO;

    // --- ORDER COUNTS ---

    /**
     * The total number of orders received by the seller.
     */
    @Column(name = "total_orders")
    private Integer totalOrders = 0;

    /**
     * The total number of orders that were canceled.
     */
    @Column(name = "canceled_orders")
    private Integer canceledOrders = 0;

    /**
     * The total number of financial transactions processed (e.g., payments, refunds).
     */
    @Column(name = "total_transactions")
    private Integer totalTransactions = 0;

    // --- METADATA ---

    /**
     * The timestamp of the last time this report was updated.
     */
    @Column(name = "last_updated")
    private LocalDateTime lastUpdated = LocalDateTime.now();
}