package com.ogos.service;

import com.ogos.dto.OrderRequest;
import com.ogos.dto.OrderResponse;
import com.ogos.entity.User;

import java.util.List;

public interface OrderService {

    OrderResponse placeOrder(User user, OrderRequest request);

    List<OrderResponse> getUserOrders(User user);

    OrderResponse getUserOrder(User user, Long orderId);

    void cancelOrder(User user, Long orderId);

    List<OrderResponse> getVendorOrders(User vendor);

    OrderResponse confirmOrder(User vendor, Long orderId);

    OrderResponse shipOrder(User vendor, Long orderId);

    OrderResponse deliverOrder(User vendor, Long orderId);

    List<OrderResponse> getAllOrders();
}
