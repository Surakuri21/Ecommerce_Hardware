package com.Surakuri.Model.entity.Other_Business_Entities;

import com.Surakuri.Model.entity.User_Cart.User;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

/**
 * Represents a verification token used for account activation or password resets.
 *
 * <p>This entity stores a One-Time Password (OTP) and links it to a specific {@link User} or {@link Seller}.
 * It includes an expiry date to ensure that tokens are only valid for a limited time, enhancing security.</p>
 */
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(exclude = {"user", "seller"}) // Exclude relationships
@Table(name = "verification_tokens")
public class Verification {

    /**
     * The unique identifier for the verification token. This is the primary key.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The One-Time Password (OTP) code, typically a 6-digit string.
     */
    @Column(nullable = false)
    private String otp;

    /**
     * The email associated with the verification attempt.
     * This can serve as a backup or quick reference.
     */
    private String email;

    /**
     * The timestamp when this token becomes invalid.
     * This is a crucial security feature to prevent the use of old tokens.
     */
    @Column(name = "expiry_date", nullable = false)
    private LocalDateTime expiryDate;

    // --- RELATIONSHIPS ---

    /**
     * The user account associated with this verification token.
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    /**
     * The seller account associated with this verification token.
     * This allows the same token system to be used for both users and sellers.
     */
    @OneToOne
    @JoinColumn(name = "seller_id")
    private Seller seller;

    // --- CONSTRUCTORS & HELPERS ---

    /**
     * Checks if the verification token has expired by comparing the
     * {@code expiryDate} with the current time.
     *
     * @return {@code true} if the token is expired, {@code false} otherwise.
     */
    public boolean isExpired() {
        return LocalDateTime.now().isAfter(this.expiryDate);
    }

    /**
     * A convenience constructor to create a new verification token for a user.
     * It automatically sets the expiry date to 15 minutes from the time of creation.
     *
     * @param otp  The OTP code to be stored.
     * @param user The user to whom this token belongs.
     */
    public Verification(String otp, User user) {
        this.otp = otp;
        this.user = user;
        this.expiryDate = LocalDateTime.now().plusMinutes(15); // Standard OTP lifespan
    }
}