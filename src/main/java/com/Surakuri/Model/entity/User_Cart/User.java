package com.Surakuri.Model.entity.User_Cart;

import com.Surakuri.Domain.User_Role;
import com.Surakuri.Model.entity.Other_Business_Entities.Address;
import com.Surakuri.Model.entity.Payment_Orders.Order;
import com.Surakuri.Model.entity.Payment_Orders.PaymentOrder;
import com.Surakuri.Model.entity.Products_Categories.Coupon;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.List;
import java.util.ArrayList;

/**
 * Represents a user in the e-commerce system. This is a core entity that acts as a central point
 * for relationships with authentication, personal information, shopping carts, orders, and addresses.
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"cart", "addresses", "orders", "paymentOrders", "usedCoupons"})
@EqualsAndHashCode(exclude = {"cart", "addresses", "orders", "paymentOrders", "usedCoupons"})
@Table(name = "users")
public class User {

    /**
     * The unique identifier for the user. This is the primary key.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    /**
     * The user's unique login identifier.
     */
    @Column(nullable = false, unique = true)
    private String username;

    /**
     * The user's securely hashed password. Should never store plain text.
     */
    @Column(name = "password_hash", nullable = false)
    private String password;

    /**
     * The user's first name.
     */
    @Column(name = "first_name", nullable = false)
    private String firstName;

    /**
     * The user's last name.
     */
    @Column(name = "last_name", nullable = false)
    private String lastName;

    /**
     * The user's unique email address, used for communication, notifications, and potentially login.
     */
    @Column(name = "email", nullable = false, unique = true)
    private String email;

    /**
     * The user's mobile phone number.
     */
    @Column(name = "phone")
    private String mobile;

    /**
     * The role assigned to the user (e.g., ROLE_CUSTOMER, ROLE_ADMIN), which dictates their permissions.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private User_Role role = User_Role.ROLE_CUSTOMER;

    /**
     * A flag to indicate if the user account is active. Inactive users cannot log in.
     */
    @Column(name = "is_active")
    private boolean isActive = true;

    /**
     * Timestamp indicating when the user account was created.
     */
    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    // --- RELATIONSHIPS (ALL MUST BE IGNORED IN TOSTRING/HASHCODE) ---

    /**
     * One-to-one relationship with the Cart. Each user has exactly one shopping cart.
     * `mappedBy = "user"` indicates that the `Cart` entity owns this relationship.
     * `cascade = CascadeType.ALL` means that if a User is deleted, their Cart is also deleted.
     */
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonIgnore
    private Cart cart;

    /**
     * One-to-many relationship with Address. A user can have multiple shipping/billing addresses.
     * `mappedBy = "user"` indicates that the `Address` entity owns this relationship.
     */
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Address> addresses = new ArrayList<>();

    /**
     * One-to-many relationship with Order. A user can have multiple orders.
     * This provides access to the user's order history.
     */
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<Order> orders = new HashSet<>();

    /**
     * One-to-many relationship with PaymentOrder. A user can have multiple payment transactions.
     * This links the user to their payment history.
     */
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<PaymentOrder> paymentOrders = new HashSet<>();

    /**
     * Many-to-many relationship with Coupon. This tracks which coupons a user has used.
     * A `user_coupons` join table is created to manage this relationship.
     */
    @ManyToMany
    @JoinTable(
            name = "user_coupons",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "coupon_id")
    )
    @JsonIgnore
    private Set<Coupon> usedCoupons = new HashSet<>();

    /**
     * A utility method to get the user's full name.
     * @return A string combining the first and last name.
     */
    public String getFullName() {
        return this.firstName + " " + this.lastName;
    }
}