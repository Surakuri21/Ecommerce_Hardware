package com.Surakuri.Model.dto;

import com.Surakuri.Domain.OrderStatus;
import com.Surakuri.Domain.PaymentOrderStatus;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class OrderResponse {
    private Long orderId;
    private String orderReference; // "ORD-2025-XYZ"
    private String status;         // "PENDING"
    private BigDecimal totalAmount;
    private String paymentMethod;
    private String customerName;
    private String shippingAddress;
    private LocalDateTime orderDate;
}