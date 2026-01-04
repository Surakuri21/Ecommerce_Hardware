package com.Surakuri.Model.entity.User_Cart;

import com.Surakuri.Model.entity.Products_Categories.ProductVariant;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

/**
 * Represents a single item within a shopping cart.
 * It links a specific product variant to a cart and defines the quantity desired by the user.
 * This entity is transient; its data is typically moved to an OrderItem upon checkout.
 */
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(exclude = {"cart", "variant"}) // Prevents infinite loops in relationships
@Table(name = "cart_items")
public class CartItem {

    /**
     * The unique identifier for the cart item. This is the primary key.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cart_item_id")
    private Long id;

    /**
     * The cart to which this item belongs.
     * This creates a many-to-one relationship, as a cart can have many items.
     * It's ignored during JSON serialization to prevent circular dependencies.
     */
    @ManyToOne
    @JoinColumn(name = "cart_id", nullable = false)
    @JsonIgnore
    private Cart cart;

    /**
     * The specific product variant being added to the cart.
     * This links the cart item to a purchasable SKU, which holds the current price and stock info.
     */
    @ManyToOne
    @JoinColumn(name = "variant_id", nullable = false)
    private ProductVariant variant;

    /**
     * The number of units of the product variant in the cart.
     */
    @Column(nullable = false)
    private int quantity;

    // --- CODE QUALITY IMPROVEMENT ---
    // The price fields (mrpPrice, sellingPrice, subtotal) have been removed from CartItem.
    // REASON: The Cart is a temporary state. The current, authoritative price should always
    // be retrieved from the associated `ProductVariant` entity to avoid data inconsistency.
    // A historical price snapshot is correctly stored in the `OrderItem` entity upon purchase.
}