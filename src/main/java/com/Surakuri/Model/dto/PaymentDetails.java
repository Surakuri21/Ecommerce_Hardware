package com.Surakuri.Model.dto;

import com.Surakuri.Domain.PaymentMethod;
import com.Surakuri.Domain.PaymentStatus;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal; // Always use this for money
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable // This allows it to sit inside PaymentOrder or Seller tables
public class PaymentDetails {

    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod; // e.g., GCASH, COD

    @Enumerated(EnumType.STRING)
    private PaymentStatus status = PaymentStatus.PENDING;

    // GENERALIZATION:
    // Don't call it "razorpayPaymentId" or "stripeId".
    // Call it "paymentId" or "referenceId".
    // This stores the GCash Ref No., Bank Transfer No., or Gateway ID.
    private String paymentId;

    // GENERALIZATION:
    // Use BigDecimal. Double loses cents (e.g., 100.10 becomes 100.0999999).
    private BigDecimal amount;

    // Optional: Track when the payment actually cleared (not just when order was made)
    private LocalDateTime paymentDate;
}


/* Generalizing PaymentDetails (The "Universal Receipt")
We rename specific fields to be generic.
Whether the money comes from a GCash Reference Number or a Credit Card Transaction ID,
 we store it in paymentId.    */