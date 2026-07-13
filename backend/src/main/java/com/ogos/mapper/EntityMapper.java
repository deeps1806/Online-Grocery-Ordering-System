package com.ogos.mapper;

import com.ogos.dto.*;
import com.ogos.entity.*;

import java.util.stream.Collectors;

public class EntityMapper {

    private EntityMapper() {}

    // ---- User ----
    public static UserResponse toUserResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .name(user.getFirstName() + (user.getLastName() != null && !user.getLastName().isEmpty() ? " " + user.getLastName() : ""))
                .email(user.getEmail())
                .phone(user.getPhone())
                .role(user.getRole().name())
                .status(user.getStatus() != null ? user.getStatus().name() : null)
                .build();
    }

    // ---- Product ----
    public static ProductResponse toProductResponse(Product product) {
        ProductResponse.ProductResponseBuilder builder = ProductResponse.builder()
                .id(product.getId())
                .name(product.getProductName())
                .description(product.getDescription())
                .price(product.getPrice())
                .stock(product.getQuantity())
                .imageUrl(product.getImageUrl())
                .createdAt(product.getCreatedAt())
                .updatedAt(product.getUpdatedAt());

        if (product.getCategory() != null) {
            builder.category(product.getCategory().getName());
            builder.categoryId(product.getCategory().getId());
        }

        if (product.getVendor() != null) {
            builder.vendorId(product.getVendor().getId());
            builder.vendorName(product.getVendor().getFirstName());
        }

        return builder.build();
    }

    // ---- Category ----
    public static CategoryResponse toCategoryResponse(Category category) {
        return CategoryResponse.builder()
                .id(category.getId())
                .name(category.getName())
                .description(category.getDescription())
                .imageUrl(category.getImageUrl())
                .createdAt(category.getCreatedAt())
                .updatedAt(category.getUpdatedAt())
                .build();
    }

    // ---- Cart ----
    public static CartResponse toCartResponse(Cart cart) {
        return CartResponse.builder()
                .id(cart.getId())
                .items(cart.getItems().stream()
                        .map(EntityMapper::toCartItemResponse)
                        .collect(Collectors.toList()))
                .totalPrice(cart.getTotalPrice())
                .build();
    }

    public static CartItemResponse toCartItemResponse(CartItem item) {
        return CartItemResponse.builder()
                .id(item.getId())
                .productId(item.getProduct().getId())
                .productName(item.getProduct().getProductName())
                .imageUrl(item.getProduct().getImageUrl())
                .quantity(item.getQuantity())
                .price(item.getPrice())
                .totalPrice(item.getPrice() * item.getQuantity())
                .build();
    }

    // ---- Address ----
    public static AddressResponse toAddressResponse(Address address) {
        return AddressResponse.builder()
                .id(address.getId())
                .street(address.getStreet())
                .city(address.getCity())
                .state(address.getState())
                .zipCode(address.getZipCode())
                .country(address.getCountry())
                .isDefault(address.getIsDefault())
                .build();
    }

    // ---- Order ----
    public static OrderResponse toOrderResponse(Order order) {
        return OrderResponse.builder()
                .id(order.getId())
                .status(order.getStatus().name())
                .totalAmount(order.getTotalAmount())
                .shippingAddress(order.getShippingAddress() != null ? toAddressResponse(order.getShippingAddress()) : null)
                .items(order.getItems().stream()
                        .map(EntityMapper::toOrderItemResponse)
                        .collect(Collectors.toList()))
                .createdAt(order.getCreatedAt())
                .updatedAt(order.getUpdatedAt())
                .build();
    }

    public static OrderItemResponse toOrderItemResponse(OrderItem item) {
        return OrderItemResponse.builder()
                .id(item.getId())
                .productId(item.getProduct().getId())
                .productName(item.getProduct().getProductName())
                .imageUrl(item.getProduct().getImageUrl())
                .quantity(item.getQuantity())
                .price(item.getPrice())
                .totalPrice(item.getTotalPrice())
                .build();
    }
}
