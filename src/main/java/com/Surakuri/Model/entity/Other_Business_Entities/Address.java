package com.Surakuri.Model.entity.Other_Business_Entities;

import com.Surakuri.Model.entity.User_Cart.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
// ⚠️ CRITICAL FIX: Exclude 'user'
@EqualsAndHashCode(exclude = {"user"})
@Table(name = "customer_addresses")
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "address_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = true)
    @JsonIgnore // Stop the loop back to User
    private User user;

    /**
     * The full name of the person who will receive the delivery at this address.
     */
    @Column(name = "contact_person_name", nullable = false)
    private String contactPersonName;

    /**
     * The mobile number of the contact person.
     */
    @Column(name = "contact_mobile", nullable = false)
    private String contactMobile;

    /**
     * A user-defined label for the address (e.g., "Home", "Office", "Warehouse").
     */
    @Column(name = "address_label")
    private String addressLabel;

    /**
     * The administrative region of the address.
     */
    @Column(nullable = false)
    private String region;

    /**
     * The province of the address.
     */
    @Column(nullable = false)
    private String province;

    /**
     * The city or municipality of the address.
     */
    @Column(nullable = false)
    private String city;

    /**
     * The specific barangay (village or district) of the address.
     */
    @Column(nullable = false)
    private String barangay;

    /**
     * The street name, building number, and unit number.
     */
    @Column(nullable = false)
    private String street;

    /**
     * The postal or ZIP code for the address.
     */
    @Column(name = "postal_code", nullable = false)
    private String postalCode;

    @Column(name = "additional_notes")
    private String additionalNotes;

    @Column(name = "is_default")
    private boolean isDefault = false;
}