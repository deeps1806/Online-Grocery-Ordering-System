package com.ogos.controller;

import com.ogos.dto.ApiResponse;
import com.ogos.dto.ProductRequest;
import com.ogos.dto.ProductResponse;
import com.ogos.entity.User;
import com.ogos.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
@Tag(name = "Products", description = "Product CRUD with search, pagination, and category filter")
public class ProductController {

    private final ProductService productService;

    @PostMapping
    @Operation(summary = "Add a product (VENDOR / ADMIN only)", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<ApiResponse<ProductResponse>> addProduct(
            @Valid @RequestBody ProductRequest request,
            @AuthenticationPrincipal User currentUser) {
        ProductResponse response = productService.addProduct(request, currentUser);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Product created successfully", response));
    }

    @GetMapping
    @Operation(summary = "Get all products with pagination")
    public ResponseEntity<ApiResponse<Page<ProductResponse>>> getAllProducts(
            @RequestParam(defaultValue = "0")  int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sort) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sort));
        return ResponseEntity.ok(ApiResponse.success("Products fetched successfully",
                productService.getAllProducts(pageable)));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get product by ID")
    public ResponseEntity<ApiResponse<ProductResponse>> getProduct(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success("Product fetched successfully",
                productService.getProduct(id)));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a product (VENDOR / ADMIN only)", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<ApiResponse<ProductResponse>> updateProduct(
            @PathVariable Long id,
            @Valid @RequestBody ProductRequest request,
            @AuthenticationPrincipal User currentUser) {
        ProductResponse response = productService.updateProduct(id, request, currentUser);
        return ResponseEntity.ok(ApiResponse.success("Product updated successfully", response));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a product (VENDOR / ADMIN only)", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<ApiResponse<Void>> deleteProduct(
            @PathVariable Long id,
            @AuthenticationPrincipal User currentUser) {
        productService.deleteProduct(id, currentUser);
        return ResponseEntity.ok(ApiResponse.success("Product deleted successfully"));
    }

    @GetMapping("/search")
    @Operation(summary = "Search products by keyword")
    public ResponseEntity<ApiResponse<Page<ProductResponse>>> searchProducts(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0")  int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(ApiResponse.success("Search results",
                productService.searchProducts(keyword, pageable)));
    }

    @GetMapping("/category/{categoryId}")
    @Operation(summary = "Get products by category")
    public ResponseEntity<ApiResponse<Page<ProductResponse>>> getByCategory(
            @PathVariable Long categoryId,
            @RequestParam(defaultValue = "0")  int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(ApiResponse.success("Products by category",
                productService.getProductsByCategory(categoryId, pageable)));
    }

    @GetMapping("/sort")
    @Operation(summary = "Get products sorted by field and direction")
    public ResponseEntity<ApiResponse<Page<ProductResponse>>> getSorted(
            @RequestParam(defaultValue = "price")      String field,
            @RequestParam(defaultValue = "asc")        String direction,
            @RequestParam(defaultValue = "0")          int page,
            @RequestParam(defaultValue = "10")         int size) {
        Sort sort = direction.equalsIgnoreCase("desc")
                ? Sort.by(field).descending()
                : Sort.by(field).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        return ResponseEntity.ok(ApiResponse.success("Sorted products",
                productService.getAllProducts(pageable)));
    }
}