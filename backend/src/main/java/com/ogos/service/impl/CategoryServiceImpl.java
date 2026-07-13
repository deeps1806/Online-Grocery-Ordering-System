package com.ogos.service.impl;

import com.ogos.dto.CategoryRequest;
import com.ogos.dto.CategoryResponse;
import com.ogos.entity.Category;
import com.ogos.exception.ResourceNotFoundException;
import com.ogos.mapper.EntityMapper;
import com.ogos.repository.CategoryRepository;
import com.ogos.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    public List<CategoryResponse> getAllCategories() {
        return categoryRepository.findAll().stream()
                .map(EntityMapper::toCategoryResponse)
                .collect(Collectors.toList());
    }

    @Override
    public CategoryResponse getCategory(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", id));
        return EntityMapper.toCategoryResponse(category);
    }

    @Override
    @Transactional
    public CategoryResponse createCategory(CategoryRequest request) {
        Category category = Category.builder()
                .name(request.getName())
                .description(request.getDescription())
                .imageUrl(request.getImageUrl())
                .build();

        Category saved = categoryRepository.save(category);
        return EntityMapper.toCategoryResponse(saved);
    }

    @Override
    @Transactional
    public CategoryResponse updateCategory(Long id, CategoryRequest request) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", id));

        category.setName(request.getName());
        category.setDescription(request.getDescription());
        if (request.getImageUrl() != null) {
            category.setImageUrl(request.getImageUrl());
        }

        Category saved = categoryRepository.save(category);
        return EntityMapper.toCategoryResponse(saved);
    }

    @Override
    @Transactional
    public void deleteCategory(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", id));
        categoryRepository.delete(category);
    }
}
