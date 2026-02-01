package com.Surakuri.Model.entity.Products_Categories;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

/**
 * Represents a specific, purchasable variation of a {@link Product}.
 *
 * <p>A {@code ProductVariant} holds the unique details for a version of a product,
 * such as its specific price, weight, and SKU. It also directly manages the inventory
 * level for that specific variant.</p>
 *
 * <p><b>Example:</b> For a "Cement" product, a variant could be a "40kg Bag" with its own
 * price, SKU ("CEM-40KG"), and stock quantity.</p>
 */
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(exclude = "product") // Exclude parent to prevent recursion
@Table(name = "product_variants")
public class ProductVariant {

    /**
     * The unique identifier for the product variant. This is the primary key.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "variant_id")
    private Long id;

    /**
     * The unique Stock Keeping Unit (SKU) for this specific variant.
     * It is a critical identifier for inventory management and order processing.
     */
    @Column(nullable = false, unique = true)
    private String sku; // e.g., "CEM-40KG"

    /**
     * A generated name for the variant, used for display purposes.
     * (e.g., "40kg Bag", "1/2 inch Diameter / 10 feet").
     */
    @Column(name = "variant_name")
    private String variantName;

    /**
     * The selling price for this specific variant.
     * Using {@link BigDecimal} is crucial for accurate financial calculations.
     */
    @Column(nullable = false)
    private BigDecimal price;

    /**
     * The weight of this specific variant in kilograms.
     * This is essential for calculating shipping costs, especially for heavy hardware.
     */
    @Column(name = "weight_kg")
    private BigDecimal weightKg;

    /**
     * A JSON string representing all unique attributes of this variant.
     * This provides a flexible way to store specifications like size, color, material, etc.
     */
    @Column(columnDefinition = "TEXT")
    private String specifications;

    /**
     * The current number of units available in stock for this variant.
     * This field serves as the single source of truth for inventory.
     */
    @Column(name = "stock_quantity")
    private int stockQuantity = 0;

    /**
     * The minimum stock level before a re-order warning should be triggered.
     * This is used for inventory management and reporting.
     */
    @Column(name = "min_stock_level")
    private int minStockLevel = 10;

    /**
     * The parent {@link Product} to which this variant belongs.
     * This creates a many-to-one relationship (many variants can belong to one product).
     * It is ignored during JSON serialization to prevent infinite loops.
     */
    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    @JsonIgnore
    private Product product;
}