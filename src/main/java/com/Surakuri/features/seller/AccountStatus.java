package com.Surakuri.features.seller;

public enum AccountStatus {
    PENDING_VERIFICATION,   // User registered but hasn't clicked the email link yet
    ACTIVE,                 // Good standing, can buy/sell
    SUSPENDED,              // Temporary lock (e.g., suspicious activity)
    DEACTIVATED,            // User closed their own account (Soft Delete)
    BANNED                  // Admin permanently removed them (Violation)
}