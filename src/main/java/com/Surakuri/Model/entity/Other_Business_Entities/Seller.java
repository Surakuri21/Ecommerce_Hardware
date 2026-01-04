package com.Surakuri.Model.entity.Other_Business_Entities;

import com.Surakuri.Domain.AccountStatus;
import com.Surakuri.Domain.User_Role;
import com.Surakuri.Model.dto.BankDetails;
import com.Surakuri.Model.dto.BusinessDetails;
import jakarta.persistence.*;
import lombok.*;

/**
 * Represents a seller or vendor in the multi-vendor e-commerce platform.
 *
 * <p>This entity stores all information related to a seller, including their personal contact details,
 * business information, banking details for payouts, and account status. It serves as the central
 * point for managing sellers and their products.</p>
 */
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(exclude = {"pickupAddress"})
@Table(name = "sellers")
public class Seller {

    /**
     * The unique identifier for the seller. This is the primary key.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "seller_id")
    private Long id;

    /**
     * The display name of the seller or their store.
     */
    @Column(name = "seller_name", nullable = false)
    private String sellerName;

    /**
     * The seller's primary contact mobile number.
     */
    @Column(name = "mobile")
    private String mobile;

    /**
     * The seller's login and contact email address. Must be unique.
     */
    @Column(unique = true, nullable = false)
    private String email;

    /**
     * The hashed password for the seller's account.
     */
    @Column(nullable = false)
    private String password;

    /**
     * An embedded object containing the seller's official business details,
     * such as business name, address, and registration numbers.
     */
    @Embedded
    private BusinessDetails businessDetails = new BusinessDetails();

    /**
     * An embedded object containing the seller's bank account details,
     * required for processing payouts.
     */
    @Embedded
    private BankDetails bankDetails = new BankDetails();

    /**
     * The seller's designated default address for order pickups.
     */
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "pickup_address_id")
    private Address pickupAddress;

    /**
     * The seller's Taxpayer Identification Number (TIN).
     */
    @Column(name = "tin_number")
    private String TIN;

    /**
     * The user role, which is always {@link User_Role#ROLE_SELLER} for this entity.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private User_Role role = User_Role.ROLE_SELLER;

    /**
     * A flag indicating whether the seller's email address has been verified.
     */
    @Column(name = "is_email_verified")
    private boolean isEmailVerified = false;

    /**
     * The current status of the seller's account (e.g., PENDING_VERIFICATION, ACTIVE, SUSPENDED).
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "account_status")
    private AccountStatus accountStatus = AccountStatus.PENDING_VERIFICATION;
}