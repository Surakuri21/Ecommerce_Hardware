package com.Surakuri.Model.dto;

import lombok.Data;

@Data
public class SellerRegisterRequest {
    // Login Info
    private String email;
    private String password;
    private String sellerName; // Display Name (e.g., "Cebu Builders")
    private String mobile;

    // Business Info (Embedded)
    private String businessName; // Legal Name
    private String businessAddress;
    private String tinNumber; // Tax ID

    // Address Info (For Pickup)
    private String street;
    private String city;
    private String province;
    private String region;
    private String zipCode;
}