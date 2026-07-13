package com.ogos.controller;

import com.ogos.dto.ApiResponse;
import com.ogos.dto.CartItemRequest;
import com.ogos.dto.CartResponse;
import com.ogos.entity.User;
import com.ogos.service.CartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
@Tag(name = "Cart", description = "Cart operations — CUSTOMER only")
@SecurityRequirement(name = "bearerAuth")
public class CartController {

    private final CartService cartService;

    @GetMapping
    @Operation(summary = "Get current user's cart")
    public ResponseEntity<ApiResponse<CartResponse>> getCart(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(ApiResponse.success("Cart fetched successfully",
                cartService.getCart(user)));
    }

    @PostMapping("/items")
    @Operation(summary = "Add item to cart (merges quantity if product already in cart)")
    public ResponseEntity<ApiResponse<CartResponse>> addItem(
            @AuthenticationPrincipal User user,
            @Valid @RequestBody CartItemRequest request) {
        return ResponseEntity.ok(ApiResponse.success("Item added to cart",
                cartService.addItem(user, request)));
    }

    @PutMapping("/items/{id}")
    @Operation(summary = "Update item quantity in cart")
    public ResponseEntity<ApiResponse<CartResponse>> updateItem(
            @AuthenticationPrincipal User user,
            @PathVariable Long id,
            @Valid @RequestBody CartItemRequest request) {
        return ResponseEntity.ok(ApiResponse.success("Cart item updated",
                cartService.updateItem(user, id, request)));
    }

    @DeleteMapping("/items/{id}")
    @Operation(summary = "Remove item from cart")
    public ResponseEntity<ApiResponse<Void>> removeItem(
            @AuthenticationPrincipal User user,
            @PathVariable Long id) {
        cartService.removeItem(user, id);
        return ResponseEntity.ok(ApiResponse.success("Item removed from cart"));
    }

    @DeleteMapping("/clear")
    @Operation(summary = "Clear all items from cart")
    public ResponseEntity<ApiResponse<Void>> clearCart(@AuthenticationPrincipal User user) {
        cartService.clearCart(user);
        return ResponseEntity.ok(ApiResponse.success("Cart cleared"));
    }
}
