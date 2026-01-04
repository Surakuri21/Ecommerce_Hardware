package com.Surakuri.Model.entity.Other_Business_Entities;

import com.Surakuri.Model.entity.Products_Categories.Product;
import com.Surakuri.Model.entity.User_Cart.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

/**
 * Represents a customer review for a specific {@link Product}.
 *
 * <p>This entity stores the feedback provided by a user, including a text comment,
 * a numerical rating, and a reference to the product being reviewed.</p>
 */
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(exclude = {"product", "user"}) // Exclude relationships to prevent recursion
@Table(name = "reviews")
public class Review {

    /**
     * The unique identifier for the review. This is the primary key.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The text content of the review written by the user.
     * (e.g., "This G.I. Pipe is very durable!").
     */
    private String reviewText;

    /**
     * The numerical rating given by the user, typically on a scale of 1.0 to 5.0.
     */
    private double rating;

    /**
     * The product that this review is for.
     * This relationship is ignored during JSON serialization to prevent circular dependencies
     * where a Product might have a list of Reviews, each linking back to the Product.
     */
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    /**
     * The user who wrote the review.
     */
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    /**
     * The timestamp when the review was submitted.
     */
    private LocalDateTime createdAt = LocalDateTime.now();
}