package com.Surakuri.Model.dto;

import com.Surakuri.Domain.PaymentMethod;
import lombok.Data;

@Data
public class CheckoutRequest {
    private Long addressId;       // Where to ship?
    private PaymentMethod paymentMethod; // GCASH, COD, etc.
}