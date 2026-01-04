package com.Surakuri.Model.entity.Other_Business_Entities;

import com.Surakuri.Model.entity.Products_Categories.Product;
import com.Surakuri.Model.entity.User_Cart.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

/**
 * Represents a user's wishlist, which is a collection of products they have saved for later.
 *
 * <p>Each user has a single wishlist. This entity manages the many-to-many relationship
 * between a wishlist and the products it contains.</p>
 */
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(exclude = {"user", "products"}) // Exclude relationships
@Table(name = "wishlists")
public class Wishlist {

    /**
     * The unique identifier for the wishlist. This is the primary key.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "wishlist_id")
    private Long id;

    /**
     * The user who owns this wishlist.
     * This establishes a one-to-one relationship where each user has exactly one wishlist.
     * It is ignored during JSON serialization to prevent circular dependencies.
     */
    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    @JsonIgnore
    private User user;

    /**
     * The set of products that the user has added to their wishlist.
     * A {@code @ManyToMany} relationship is used here, managed by a join table
     * named "wishlist_products", which links wishlists to products.
     */
    @ManyToMany
    @JoinTable(
            name = "wishlist_products",
            joinColumns = @JoinColumn(name = "wishlist_id"),
            inverseJoinColumns = @JoinColumn(name = "product_id")
    )
    private Set<Product> products = new HashSet<>();
}