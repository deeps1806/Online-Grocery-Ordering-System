package com.ogos.service.impl;

import com.ogos.dto.ProductResponse;
import com.ogos.dto.VendorDashboardResponse;
import com.ogos.entity.OrderStatus;
import com.ogos.entity.User;
import com.ogos.mapper.EntityMapper;
import com.ogos.repository.OrderRepository;
import com.ogos.repository.ProductRepository;
import com.ogos.service.VendorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VendorServiceImpl implements VendorService {

    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;

    @Override
    public List<ProductResponse> getVendorProducts(User vendor) {
        return productRepository.findByVendor(vendor).stream()
                .map(EntityMapper::toProductResponse)
                .collect(Collectors.toList());
    }

    @Override
    public VendorDashboardResponse getDashboard(User vendor) {
        long totalProducts = productRepository.countByVendor(vendor);
        long totalOrders  = orderRepository.countOrdersByVendor(vendor);
        long completedOrders = orderRepository.countOrdersByVendorAndStatus(vendor, OrderStatus.DELIVERED);
        long pendingOrders   = orderRepository.countOrdersByVendorAndStatus(vendor, OrderStatus.PENDING);
        Double revenue = orderRepository.calculateRevenueByVendor(vendor, OrderStatus.DELIVERED);

        return VendorDashboardResponse.builder()
                .totalProducts(totalProducts)
                .totalOrders(totalOrders)
                .completedOrders(completedOrders)
                .pendingOrders(pendingOrders)
                .revenue(revenue != null ? revenue : 0.0)
                .build();
    }
}
