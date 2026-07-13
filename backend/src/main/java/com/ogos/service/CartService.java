package com.ogos.service;

import com.ogos.dto.CartItemRequest;
import com.ogos.dto.CartResponse;
import com.ogos.entity.User;

public interface CartService {

    CartResponse getCart(User user);

    CartResponse addItem(User user, CartItemRequest request);

    CartResponse updateItem(User user, Long itemId, CartItemRequest request);

    void removeItem(User user, Long itemId);

    void clearCart(User user);
}
