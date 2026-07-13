package com.ogos.controller;

import com.ogos.dto.ApiResponse;
import com.ogos.dto.OrderRequest;
import com.ogos.dto.OrderResponse;
import com.ogos.entity.User;
import com.ogos.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
@Tag(name = "Orders", description = "Customer order operations")
@SecurityRequirement(name = "bearerAuth")
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    @Operation(summary = "Place a new order from cart (CUSTOMER only)")
    public ResponseEntity<ApiResponse<OrderResponse>> placeOrder(
            @AuthenticationPrincipal User user,
            @Valid @RequestBody OrderRequest request) {
        OrderResponse response = orderService.placeOrder(user, request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Order placed successfully", response));
    }

    @GetMapping
    @Operation(summary = "Get all orders of current user")
    public ResponseEntity<ApiResponse<List<OrderResponse>>> getOrders(
            @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(ApiResponse.success("Orders fetched successfully",
                orderService.getUserOrders(user)));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get specific order by ID")
    public ResponseEntity<ApiResponse<OrderResponse>> getOrder(
            @AuthenticationPrincipal User user,
            @PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success("Order fetched successfully",
                orderService.getUserOrder(user, id)));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Cancel an order (only PENDING or CONFIRMED)")
    public ResponseEntity<ApiResponse<Void>> cancelOrder(
            @AuthenticationPrincipal User user,
            @PathVariable Long id) {
        orderService.cancelOrder(user, id);
        return ResponseEntity.ok(ApiResponse.success("Order cancelled successfully"));
    }
}
