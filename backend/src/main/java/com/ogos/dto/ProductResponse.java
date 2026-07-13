package com.ogos.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductResponse {

    private Long id;
    private String name;
    private String description;
    private Double price;
    private Integer stock;
    private String category;
    private Long categoryId;
    private String imageUrl;
    private Long vendorId;
    private String vendorName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
