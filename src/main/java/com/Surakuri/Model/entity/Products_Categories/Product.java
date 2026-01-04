package com.Surakuri.Model.entity.Products_Categories;

import com.Surakuri.Model.entity.Other_Business_Entities.Seller;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a parent product in the e-commerce system.
 *
 * <p>A {@code Product} acts as a container for general information common to all its variations,
 * such as its name, brand, and description. It is linked to one or more {@link ProductVariant}
 * entities, which represent the specific, purchasable versions of the product (e.g., different sizes or weights).</p>
 *
 * <p><b>Example:</b> A "Cement" product might have variants like "40kg Bag" and "50kg Bag".</p>
 */
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(exclude = {"variants", "seller", "category"})
@Table(name = "products")
public class Product {

    /**
     * The unique identifier for the product. This is the primary key.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Long id;

    /**
     * The name of the product (e.g., "Portland Cement").
     */
    @Column(nullable = false)
    private String name;

    /**
     * A detailed description of the product, suitable for display on a product page.
     */
    @Column(columnDefinition = "TEXT")
    private String description;

    /**
     * The brand or manufacturer of the product (e.g., "Holcim").
     */
    private String brand;

    /**
     * The URL for the main product image.
     */
    private String imageUrl;

    /**
     * The default or base price of the product. This may be overridden by a specific variant's price.
     */
    private BigDecimal price = BigDecimal.ZERO;

    /**
     * The Manufacturer's Suggested Retail Price (MRP). Used for displaying discounts.
     */
    private BigDecimal mrp = BigDecimal.ZERO;

    /**
     * The default weight of the product in kilograms. This may be overridden by a specific variant's weight.
     */
    @Column(name = "weight_kg")
    private BigDecimal weightKg = BigDecimal.ZERO;

    /**
     * A default Stock Keeping Unit (SKU) for the base product. Specific SKUs are typically managed by variants.
     */
    private String sku;

    /**
     * A text field for storing technical specifications, often in JSON or a formatted string.
     */
    @Column(columnDefinition = "TEXT")
    private String specifications;

    /**
     * A flag indicating whether the product is active and visible in the storefront.
     */
    private boolean isActive = true;

    /**
     * The timestamp when the product was first created.
     */
    private LocalDateTime createdAt;

    /**
     * The category to which this product belongs.
     */
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    /**
     * The seller who listed this product.
     * This relationship is ignored during JSON serialization to prevent circular dependencies.
     */
    @ManyToOne
    @JoinColumn(name = "seller_id")
    @JsonIgnore
    private Seller seller;

    /**
     * The list of specific variants available for this product.
     * This relationship is ignored during JSON serialization to prevent infinite loops.
     * {@code cascade = CascadeType.ALL} ensures that variants are saved/deleted along with the product.
     * {@code orphanRemoval = true} ensures that if a variant is removed from this list, it's deleted from the database.
     */
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<ProductVariant> variants = new ArrayList<>();

    /**
     * A lifecycle callback method that automatically sets the {@code createdAt} timestamp
     * before the entity is first persisted to the database.
     */
    @PrePersist
    protected void onCreate() { this.createdAt = LocalDateTime.now(); }
}