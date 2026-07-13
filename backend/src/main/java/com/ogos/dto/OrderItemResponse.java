package com.ogos.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderItemResponse {

    private Long id;
    private Long productId;
    private String productName;
    private String imageUrl;
    private Integer quantity;
    private Double price;
    private Double totalPrice;
}
