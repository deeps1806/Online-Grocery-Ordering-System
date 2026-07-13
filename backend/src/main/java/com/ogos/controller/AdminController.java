package com.ogos.controller;

import com.ogos.dto.ApiResponse;
import com.ogos.dto.OrderResponse;
import com.ogos.dto.UserResponse;
import com.ogos.service.AdminService;
import com.ogos.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@Tag(name = "Admin", description = "Admin-only: user management and all orders")
@SecurityRequirement(name = "bearerAuth")
public class AdminController {

    private final AdminService adminService;
    private final OrderService orderService;

    @GetMapping("/users")
    @Operation(summary = "Get all users")
    public ResponseEntity<ApiResponse<List<UserResponse>>> getAllUsers() {
        return ResponseEntity.ok(ApiResponse.success("Users fetched successfully",
                adminService.getAllUsers()));
    }

    @GetMapping("/users/{id}")
    @Operation(summary = "Get user by ID")
    public ResponseEntity<ApiResponse<UserResponse>> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success("User fetched successfully",
                adminService.getUserById(id)));
    }

    @PutMapping("/users/{id}/status")
    @Operation(summary = "Block or activate a user (status: ACTIVE or BLOCKED)")
    public ResponseEntity<ApiResponse<UserResponse>> updateUserStatus(
            @PathVariable Long id,
            @RequestParam String status) {
        return ResponseEntity.ok(ApiResponse.success("User status updated",
                adminService.updateUserStatus(id, status)));
    }

    @DeleteMapping("/users/{id}")
    @Operation(summary = "Delete a user")
    public ResponseEntity<ApiResponse<Void>> deleteUser(@PathVariable Long id) {
        adminService.deleteUser(id);
        return ResponseEntity.ok(ApiResponse.success("User deleted successfully"));
    }

    @GetMapping("/orders")
    @Operation(summary = "Get all orders in the system")
    public ResponseEntity<ApiResponse<List<OrderResponse>>> getAllOrders() {
        return ResponseEntity.ok(ApiResponse.success("All orders fetched",
                orderService.getAllOrders()));
    }
}
