package com.ogos.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VendorDashboardResponse {

    private Long totalProducts;
    private Long totalOrders;
    private Long completedOrders;
    private Long pendingOrders;
    private Double revenue;
}
