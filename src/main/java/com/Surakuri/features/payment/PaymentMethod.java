package com.Surakuri.features.payment;

public enum PaymentMethod {
    COD,            // Cash on Delivery (Most common in PH)
    GCASH,          // E-Wallet
    PAYMAYA,        // E-Wallet (Maya)
    CREDIT_CARD,    // Visa/Mastercard
    BANK_TRANSFER   // BPI/BDO (For large hardware orders)
}