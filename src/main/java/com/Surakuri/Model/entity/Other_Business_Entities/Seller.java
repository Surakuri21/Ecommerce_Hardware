package com.Surakuri.Model.entity.Other_Business_Entities;

import com.Surakuri.Domain.AccountStatus;
import com.Surakuri.Model.dto.BankDetails;
import com.Surakuri.Model.dto.BusinessDetails;
import com.Surakuri.Model.entity.User_Cart.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

/**
 * Represents the business profile of a user whose role is ROLE_SELLER.
 *
 * <p>This entity holds information specific to a seller's business operations,
 * such as their store name, business details, and bank information. It is linked
 * via a One-to-One relationship to the core {@link User} entity, which manages
 * authentication and basic personal details.</p>
 */
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(exclude = {"pickupAddress", "user"})
@Table(name = "sellers")
public class Seller {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "seller_id")
    private Long id;

    /**
     * The core user account that owns this seller profile. This contains the login credentials.
     */
    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private User user;

    /**
     * The display name of the seller or their store.
     */
    @Column(name = "seller_name", nullable = false)
    private String sellerName;

    /**
     * An embedded object containing the seller's official business details.
     */
    @Embedded
    private BusinessDetails businessDetails = new BusinessDetails();

    /**
     * An embedded object containing the seller's bank account details for payouts.
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
     * The current status of the seller's account (e.g., PENDING_VERIFICATION, ACTIVE, SUSPENDED).
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "account_status")
    private AccountStatus accountStatus = AccountStatus.PENDING_VERIFICATION;
}