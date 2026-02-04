package com.Surakuri.features.seller.DTO;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class SellerDashboardDTO {
    private BigDecimal totalRevenue;
    private long totalOrders;
    private long pendingOrders;
    private long completedOrders;
}