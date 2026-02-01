package com.Surakuri.Model.dto;

import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable // CRITICAL: This tells Spring to merge these fields into the 'sellers' table
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BusinessDetails {

    // --- YOUR VISUAL & CONTACT FIELDS ---

    private String businessName;   // Fixed typo (was 'businesName')

    private String businessEmail;

    private String businessMobile;

    private String businessAddress; // Text representation (e.g., for Invoice Header)

    private String logo;   // URL to image

    private String banner; // URL to image

    // --- MERGED PHILIPPINE LEGAL FIELDS ---
    // These build trust with buyers in the Philippines

    private String businessPermitNumber; // Mayor's Permit No.

    private String dtiRegNumber;         // DTI or SEC Registration No.
}