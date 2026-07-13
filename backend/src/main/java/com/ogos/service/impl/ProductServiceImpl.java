package com.ogos.service.impl;

import com.ogos.dto.ProductRequest;
import com.ogos.dto.ProductResponse;
import com.ogos.entity.Category;
import com.ogos.entity.Product;
import com.ogos.entity.User;
import com.ogos.exception.ResourceNotFoundException;
import com.ogos.mapper.EntityMapper;
import com.ogos.repository.CategoryRepository;
import com.ogos.repository.ProductRepository;
import com.ogos.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    @Override
    @Transactional
    public ProductResponse addProduct(ProductRequest request, User vendor) {

        Product product = Product.builder()
                .productName(request.getName())
                .description(request.getDescription())
                .price(request.getPrice())
                .quantity(request.getStock())
                .imageUrl(request.getImageUrl() != null ? request.getImageUrl() : "")
                .vendor(vendor)
                .build();

        if (request.getCategoryId() != null) {
            Category category = categoryRepository.findById(request.getCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("Category", "id", request.getCategoryId()));
            product.setCategory(category);
        }

        Product saved = productRepository.save(product);
        return EntityMapper.toProductResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProductResponse> getAllProducts(Pageable pageable) {
        return productRepository.findAll(pageable)
                .map(EntityMapper::toProductResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public ProductResponse getProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", id));
        return EntityMapper.toProductResponse(product);
    }

    @Override
    @Transactional
    public ProductResponse updateProduct(Long id, ProductRequest request, User vendor) {

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", id));

        product.setProductName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setQuantity(request.getStock());

        if (request.getImageUrl() != null) {
            product.setImageUrl(request.getImageUrl());
        }

        if (request.getCategoryId() != null) {
            Category category = categoryRepository.findById(request.getCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("Category", "id", request.getCategoryId()));
            product.setCategory(category);
        }

        Product saved = productRepository.save(product);
        return EntityMapper.toProductResponse(saved);
    }

    @Override
    @Transactional
    public void deleteProduct(Long id, User user) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", id));
        productRepository.delete(product);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProductResponse> searchProducts(String keyword, Pageable pageable) {
        return productRepository.searchByKeyword(keyword, pageable)
                .map(EntityMapper::toProductResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProductResponse> getProductsByCategory(Long categoryId, Pageable pageable) {
        return productRepository.findByCategoryId(categoryId, pageable)
                .map(EntityMapper::toProductResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductResponse> getVendorProducts(User vendor) {
        return productRepository.findByVendor(vendor).stream()
                .map(EntityMapper::toProductResponse)
                .collect(Collectors.toList());
    }
}
