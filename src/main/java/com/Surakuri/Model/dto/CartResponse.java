package com.Surakuri.Model.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

@Data
public class CartResponse {
    private Long cartId;
    private BigDecimal totalSellingPrice;
    private int totalItems;
    private String customerName;
    private List<CartItemResponse> items; // The list of safe items
}