package com.Surakuri.features.order.DTO;

import com.Surakuri.features.payment.PaymentMethod;
import lombok.Data;

@Data
public class CheckoutRequest {
    private Long addressId;       // Where to ship?
    private PaymentMethod paymentMethod; // GCASH, COD, etc.
}