package com.Surakuri.Model.dto;

import jakarta.persistence.Embeddable;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable // Required so it can be embedded in the Seller entity
public class BankDetails {

    // GENERALIZATION:
    // Instead of 'ifsCode' (India specific), just ask for the Bank Name.
    // In PH, knowing "BDO" or "BPI" is usually enough for manual transfers.
    private String bankName;

    // Fixed typo: acountHolderName -> accountHolderName
    private String accountHolderName;

    private String accountNumber;

    // Optional: Useful for international transfers, but safe to keep null for local
    private String swiftCode;
}

/*  Generalizing BankDetails (Philippine Context)
In the Philippines, we identify banks by Name (BDO, BPI) and sometimes Branch    */