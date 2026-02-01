package com.Surakuri.Model.entity.Payment_Orders;

import com.Surakuri.Model.entity.Products_Categories.ProductVariant;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(exclude = "order") // Excludes the 'order' field from equals and hashCode to prevent circular dependencies and potential stack overflow errors.
@Table(name = "order_items")
public class OrderItem {

    /**
     * The unique identifier for the order item.
     * This is the primary key.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_item_id")
    private Long id;

    /**
     * The order to which this item belongs.
     * This creates a many-to-one relationship with the Order entity.
     * It's ignored during JSON serialization to prevent infinite recursion between Order and OrderItem.
     */
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    // --- FIX START ---
    // OLD CODE: private Product product;
    /**
     * The specific product variant (e.g., a specific size or color of a product) for this order item.
     * This creates a many-to-one relationship with the ProductVariant entity.
     */
    @ManyToOne
    @JoinColumn(name = "variant_id", nullable = false)
    private ProductVariant variant;
    // --- FIX END ---



    /**
     * The number of units of the product variant purchased in this order item.
     */
    private int quantity;

    /**
     * The Maximum Retail Price (MRP) of a single unit of the product variant at the time the order was placed.
     * This is stored to maintain a historical record of the price.
     */
    @Column(name = "mrp_price")
    private BigDecimal mrpPrice;

    /**
     * The actual selling price of a single unit of the product variant at the time the order was placed.
     * This might be different from the MRP due to discounts or promotions.
     */
    @Column(name = "selling_price")
    private BigDecimal sellingPrice;

    /**
     * The subtotal for this order item, calculated as (sellingPrice * quantity).
     * Storing this value can simplify reporting and invoice generation.
     */
    @Column(name = "subtotal")
    private BigDecimal subtotal;
}