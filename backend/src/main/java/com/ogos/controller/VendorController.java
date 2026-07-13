package com.ogos.controller;

import com.ogos.dto.ApiResponse;
import com.ogos.dto.OrderResponse;
import com.ogos.dto.ProductResponse;
import com.ogos.dto.VendorDashboardResponse;
import com.ogos.entity.User;
import com.ogos.service.OrderService;
import com.ogos.service.VendorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vendor")
@RequiredArgsConstructor
@Tag(name = "Vendor", description = "Vendor dashboard, products, and order management")
@SecurityRequirement(name = "bearerAuth")
public class VendorController {

    private final VendorService vendorService;
    private final OrderService orderService;

    @GetMapping("/products")
    @Operation(summary = "Get all products listed by the logged-in vendor")
    public ResponseEntity<ApiResponse<List<ProductResponse>>> getVendorProducts(
            @AuthenticationPrincipal User vendor) {
        return ResponseEntity.ok(ApiResponse.success("Vendor products fetched",
                vendorService.getVendorProducts(vendor)));
    }

    @GetMapping("/orders")
    @Operation(summary = "Get all orders containing vendor's products")
    public ResponseEntity<ApiResponse<List<OrderResponse>>> getVendorOrders(
            @AuthenticationPrincipal User vendor) {
        return ResponseEntity.ok(ApiResponse.success("Vendor orders fetched",
                orderService.getVendorOrders(vendor)));
    }

    @GetMapping("/dashboard")
    @Operation(summary = "Get vendor dashboard stats")
    public ResponseEntity<ApiResponse<VendorDashboardResponse>> getDashboard(
            @AuthenticationPrincipal User vendor) {
        return ResponseEntity.ok(ApiResponse.success("Dashboard data fetched",
                vendorService.getDashboard(vendor)));
    }

    @PutMapping("/orders/{id}/confirm")
    @Operation(summary = "Confirm a PENDING order")
    public ResponseEntity<ApiResponse<OrderResponse>> confirmOrder(
            @AuthenticationPrincipal User vendor,
            @PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success("Order confirmed",
                orderService.confirmOrder(vendor, id)));
    }

    @PutMapping("/orders/{id}/ship")
    @Operation(summary = "Mark a CONFIRMED order as SHIPPED")
    public ResponseEntity<ApiResponse<OrderResponse>> shipOrder(
            @AuthenticationPrincipal User vendor,
            @PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success("Order shipped",
                orderService.shipOrder(vendor, id)));
    }

    @PutMapping("/orders/{id}/deliver")
    @Operation(summary = "Mark a SHIPPED order as DELIVERED")
    public ResponseEntity<ApiResponse<OrderResponse>> deliverOrder(
            @AuthenticationPrincipal User vendor,
            @PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success("Order delivered",
                orderService.deliverOrder(vendor, id)));
    }
}
