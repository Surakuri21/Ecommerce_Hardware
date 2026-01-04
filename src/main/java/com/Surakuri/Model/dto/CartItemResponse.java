package com.Surakuri.Model.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class CartItemResponse {
    private Long cartItemId;
    private String productName;  // "Makita Grinder"
    private String variantName;  // "Model 9556HN"
    private BigDecimal price;
    private int quantity;
    private BigDecimal subtotal;
    private String imageUrl;     // Nice to have for the frontend
}