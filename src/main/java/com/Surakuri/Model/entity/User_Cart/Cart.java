package com.Surakuri.Model.entity.User_Cart;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Represents a user's shopping cart.
 * It acts as a container for a collection of CartItems that the user intends to purchase.
 * This entity's primary role is to link a User to their selected items.
 */
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(exclude = {"user", "cartItems"}) // Prevents infinite loops in relationships
@Table(name = "carts")
public class Cart {

    /**
     * The unique identifier for the cart. This is the primary key.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cart_id")
    private Long id;

    /**
     * The user who owns this cart. This establishes a strict one-to-one relationship
     * where each user has exactly one cart.
     * The `user` field in the `Cart` entity is the owning side of the relationship.
     */
    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    @JsonIgnore
    private User user;

    /**
     * The set of items currently in the cart.
     * `mappedBy = "cart"` indicates that the `CartItem` entity manages this relationship.
     * `cascade = CascadeType.ALL` ensures that cart items are saved/deleted along with the cart.
     * `orphanRemoval = true` ensures that if a CartItem is removed from this set, it's deleted from the database.
     */
    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<CartItem> cartItems = new HashSet<>();

    // --- CODE QUALITY IMPROVEMENT ---
    // The fields for totals (totalSellingPrice, totalItem, totalMrpPrice, discount, couponCode) have been removed.
    // REASON: These are calculated values. Storing them in the database can lead to data inconsistency
    // if the cart items change and the totals are not perfectly synchronized.
    // BEST PRACTICE: Calculate these totals dynamically in a service layer or a CartDTO
    // whenever the cart information is requested. This ensures the totals always reflect the true state of the cartItems.
}