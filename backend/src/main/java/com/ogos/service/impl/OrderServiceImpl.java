package com.ogos.service.impl;

import com.ogos.dto.OrderRequest;
import com.ogos.dto.OrderResponse;
import com.ogos.entity.*;
import com.ogos.exception.InsufficientStockException;
import com.ogos.exception.ResourceNotFoundException;
import com.ogos.mapper.EntityMapper;
import com.ogos.repository.*;
import com.ogos.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final CartRepository cartRepository;
    private final AddressRepository addressRepository;
    private final ProductRepository productRepository;

    @Override
    @Transactional
    public OrderResponse placeOrder(User user, OrderRequest request) {

        // Validate address belongs to user
        Address address = addressRepository.findById(request.getAddressId())
                .orElseThrow(() -> new ResourceNotFoundException("Address", "id", request.getAddressId()));

        if (!address.getUser().getId().equals(user.getId())) {
            throw new ResourceNotFoundException("Address", "id", request.getAddressId());
        }

        // Get user's cart
        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new IllegalArgumentException("Cart is empty. Add items before placing an order."));

        if (cart.getItems() == null || cart.getItems().isEmpty()) {
            throw new IllegalArgumentException("Cart is empty. Add items before placing an order.");
        }

        // Validate stock for all items
        for (CartItem cartItem : cart.getItems()) {
            Product product = cartItem.getProduct();
            if (product.getQuantity() < cartItem.getQuantity()) {
                throw new InsufficientStockException(
                        product.getProductName(),
                        product.getQuantity(),
                        cartItem.getQuantity()
                );
            }
        }

        // Build order
        Order order = Order.builder()
                .user(user)
                .shippingAddress(address)
                .status(OrderStatus.PENDING)
                .totalAmount(cart.getTotalPrice())
                .items(new ArrayList<>())
                .build();

        Order savedOrder = orderRepository.save(order);

        // Copy cart items → order items and reduce stock
        for (CartItem cartItem : cart.getItems()) {
            Product product = cartItem.getProduct();

            OrderItem orderItem = OrderItem.builder()
                    .order(savedOrder)
                    .product(product)
                    .quantity(cartItem.getQuantity())
                    .price(cartItem.getPrice())
                    .totalPrice(cartItem.getPrice() * cartItem.getQuantity())
                    .build();

            savedOrder.getItems().add(orderItem);

            // Reduce stock
            product.setQuantity(product.getQuantity() - cartItem.getQuantity());
            productRepository.save(product);
        }

        // Clear cart
        cart.getItems().clear();
        cart.setTotalPrice(0.0);
        cartRepository.save(cart);

        Order finalOrder = orderRepository.save(savedOrder);
        return EntityMapper.toOrderResponse(finalOrder);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderResponse> getUserOrders(User user) {
        return orderRepository.findByUserOrderByCreatedAtDesc(user).stream()
                .map(EntityMapper::toOrderResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public OrderResponse getUserOrder(User user, Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order", "id", orderId));

        if (!order.getUser().getId().equals(user.getId())) {
            throw new ResourceNotFoundException("Order", "id", orderId);
        }

        return EntityMapper.toOrderResponse(order);
    }

    @Override
    @Transactional
    public void cancelOrder(User user, Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order", "id", orderId));

        if (!order.getUser().getId().equals(user.getId())) {
            throw new ResourceNotFoundException("Order", "id", orderId);
        }

        if (order.getStatus() == OrderStatus.SHIPPED || order.getStatus() == OrderStatus.DELIVERED) {
            throw new IllegalArgumentException("Cannot cancel an order that is already " + order.getStatus().name().toLowerCase());
        }

        // Restore stock
        for (OrderItem item : order.getItems()) {
            Product product = item.getProduct();
            product.setQuantity(product.getQuantity() + item.getQuantity());
            productRepository.save(product);
        }

        order.setStatus(OrderStatus.CANCELLED);
        orderRepository.save(order);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderResponse> getVendorOrders(User vendor) {
        return orderRepository.findOrdersByVendor(vendor).stream()
                .map(EntityMapper::toOrderResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public OrderResponse confirmOrder(User vendor, Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order", "id", orderId));

        if (order.getStatus() != OrderStatus.PENDING) {
            throw new IllegalArgumentException("Order can only be confirmed when PENDING");
        }

        order.setStatus(OrderStatus.CONFIRMED);
        return EntityMapper.toOrderResponse(orderRepository.save(order));
    }

    @Override
    @Transactional
    public OrderResponse shipOrder(User vendor, Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order", "id", orderId));

        if (order.getStatus() != OrderStatus.CONFIRMED) {
            throw new IllegalArgumentException("Order can only be shipped when CONFIRMED");
        }

        order.setStatus(OrderStatus.SHIPPED);
        return EntityMapper.toOrderResponse(orderRepository.save(order));
    }

    @Override
    @Transactional
    public OrderResponse deliverOrder(User vendor, Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order", "id", orderId));

        if (order.getStatus() != OrderStatus.SHIPPED) {
            throw new IllegalArgumentException("Order can only be delivered when SHIPPED");
        }

        order.setStatus(OrderStatus.DELIVERED);
        return EntityMapper.toOrderResponse(orderRepository.save(order));
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderResponse> getAllOrders() {
        return orderRepository.findAll().stream()
                .map(EntityMapper::toOrderResponse)
                .collect(Collectors.toList());
    }
}
