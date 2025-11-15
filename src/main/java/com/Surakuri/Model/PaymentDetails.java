package com.Surakuri.Model;

import com.Surakuri.Domain.PaymentMethod;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PaymentDetails {


    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;  // GCASH, MAYA, CARD, etc.

    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;  // PENDING, SUCCESS, FAILED...

    private String transactionId;         // from GCash or Maya API
    private Double amount;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;


    private PaymentStatus status;
}
