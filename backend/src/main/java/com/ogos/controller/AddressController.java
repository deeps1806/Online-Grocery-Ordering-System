package com.ogos.controller;

import com.ogos.dto.AddressRequest;
import com.ogos.dto.AddressResponse;
import com.ogos.dto.ApiResponse;
import com.ogos.entity.User;
import com.ogos.service.AddressService;
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
@RequestMapping("/api/addresses")
@RequiredArgsConstructor
@Tag(name = "Addresses", description = "Customer address management")
@SecurityRequirement(name = "bearerAuth")
public class AddressController {

    private final AddressService addressService;

    @GetMapping
    @Operation(summary = "Get all addresses of the current user")
    public ResponseEntity<ApiResponse<List<AddressResponse>>> getAddresses(
            @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(ApiResponse.success("Addresses fetched successfully",
                addressService.getUserAddresses(user)));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get address by ID")
    public ResponseEntity<ApiResponse<AddressResponse>> getAddress(
            @AuthenticationPrincipal User user,
            @PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success("Address fetched successfully",
                addressService.getAddress(user, id)));
    }

    @PostMapping
    @Operation(summary = "Create a new address")
    public ResponseEntity<ApiResponse<AddressResponse>> createAddress(
            @AuthenticationPrincipal User user,
            @Valid @RequestBody AddressRequest request) {
        AddressResponse response = addressService.createAddress(user, request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Address created successfully", response));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an address")
    public ResponseEntity<ApiResponse<AddressResponse>> updateAddress(
            @AuthenticationPrincipal User user,
            @PathVariable Long id,
            @Valid @RequestBody AddressRequest request) {
        return ResponseEntity.ok(ApiResponse.success("Address updated successfully",
                addressService.updateAddress(user, id, request)));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete an address")
    public ResponseEntity<ApiResponse<Void>> deleteAddress(
            @AuthenticationPrincipal User user,
            @PathVariable Long id) {
        addressService.deleteAddress(user, id);
        return ResponseEntity.ok(ApiResponse.success("Address deleted successfully"));
    }
}
