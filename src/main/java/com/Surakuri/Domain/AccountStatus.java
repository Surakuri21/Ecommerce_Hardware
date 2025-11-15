package com.Surakuri.Domain;

public enum AccountStatus {

    PENDING_VERIFICATION,   //  Account is created but not yet verified
    ACTIVE,                 //  Account is active and in good standing
    SUSPENDED,               // Account is temporarily suspended, possibly due to violations
    DEACTIVATED,            //  Account is permanently banned due to severe violations
    BANNED,                 //  Account id permanently closed , possibly at user request






}
