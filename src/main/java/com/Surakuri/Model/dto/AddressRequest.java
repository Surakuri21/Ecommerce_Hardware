package com.Surakuri.Model.dto;

import lombok.Data;

/**
 * A Data Transfer Object for creating or updating a user's address.
 */
@Data
public class AddressRequest {

    private String contactPersonName;
    private String contactMobile;
    private String addressLabel; // e.g., "Home", "Office"
    private String region;
    private String province;
    private String city;
    private String barangay;
    private String street;
    private String postalCode;
    private String additionalNotes;
    private boolean isDefault = false;
}