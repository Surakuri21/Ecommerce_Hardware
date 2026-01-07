package com.Surakuri.Domain;

public enum PaymentOrderStatus {
    PENDING,    // Order placed, waiting for seller action
    CONFIRMED,  // Seller has accepted the order
    SHIPPED,    // Seller has handed over to courier
    DELIVERED,  // Customer has received the item
    CANCELLED,  // Order was cancelled
    SUCCESS,    // (Legacy) Payment successful
    FAILED      // Payment failed
}