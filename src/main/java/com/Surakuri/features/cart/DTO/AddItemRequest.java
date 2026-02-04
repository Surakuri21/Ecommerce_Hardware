package com.Surakuri.features.cart.DTO;

import lombok.Data;

@Data
public class  AddItemRequest {
    private Long variantId; // The specific SKU (e.g., "Red Wire ID")
    private int quantity;   // How many?
}