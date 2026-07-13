package com.ogos.service;

import com.ogos.dto.ProductRequest;
import com.ogos.dto.ProductResponse;
import com.ogos.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProductService {

    ProductResponse addProduct(ProductRequest request, User vendor);

    Page<ProductResponse> getAllProducts(Pageable pageable);

    ProductResponse getProduct(Long id);

    ProductResponse updateProduct(Long id, ProductRequest request, User vendor);

    void deleteProduct(Long id, User user);

    Page<ProductResponse> searchProducts(String keyword, Pageable pageable);

    Page<ProductResponse> getProductsByCategory(Long categoryId, Pageable pageable);

    List<ProductResponse> getVendorProducts(User vendor);
}