package com.ogos.controller;

import com.ogos.dto.ApiResponse;
import com.ogos.dto.CategoryRequest;
import com.ogos.dto.CategoryResponse;
import com.ogos.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
@Tag(name = "Categories", description = "Category management — write operations require ADMIN role")
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping
    @Operation(summary = "Get all categories")
    public ResponseEntity<ApiResponse<List<CategoryResponse>>> getAllCategories() {
        return ResponseEntity.ok(ApiResponse.success("Categories fetched successfully",
                categoryService.getAllCategories()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get category by ID")
    public ResponseEntity<ApiResponse<CategoryResponse>> getCategory(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success("Category fetched successfully",
                categoryService.getCategory(id)));
    }

    @PostMapping
    @Operation(summary = "Create category (ADMIN only)", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<ApiResponse<CategoryResponse>> createCategory(
            @Valid @RequestBody CategoryRequest request) {
        CategoryResponse response = categoryService.createCategory(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Category created successfully", response));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update category (ADMIN only)", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<ApiResponse<CategoryResponse>> updateCategory(
            @PathVariable Long id,
            @Valid @RequestBody CategoryRequest request) {
        return ResponseEntity.ok(ApiResponse.success("Category updated successfully",
                categoryService.updateCategory(id, request)));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete category (ADMIN only)", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<ApiResponse<Void>> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.ok(ApiResponse.success("Category deleted successfully"));
    }
}
