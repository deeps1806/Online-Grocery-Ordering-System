package com.ogos.service.impl;

import com.ogos.dto.CartItemRequest;
import com.ogos.dto.CartResponse;
import com.ogos.entity.Cart;
import com.ogos.entity.CartItem;
import com.ogos.entity.Product;
import com.ogos.entity.User;
import com.ogos.exception.ResourceNotFoundException;
import com.ogos.mapper.EntityMapper;
import com.ogos.repository.CartItemRepository;
import com.ogos.repository.CartRepository;
import com.ogos.repository.ProductRepository;
import com.ogos.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;

    @Override
    @Transactional(readOnly = true)
    public CartResponse getCart(User user) {
        Cart cart = getOrCreateCart(user);
        return EntityMapper.toCartResponse(cart);
    }

    @Override
    @Transactional
    public CartResponse addItem(User user, CartItemRequest request) {
        Cart cart = getOrCreateCart(user);

        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", request.getProductId()));

        // Check if item already exists in cart
        Optional<CartItem> existingItem = cartItemRepository.findByCartIdAndProductId(cart.getId(), product.getId());

        if (existingItem.isPresent()) {
            // Increase quantity
            CartItem item = existingItem.get();
            item.setQuantity(item.getQuantity() + request.getQuantity());
            cartItemRepository.save(item);
        } else {
            CartItem newItem = CartItem.builder()
                    .cart(cart)
                    .product(product)
                    .quantity(request.getQuantity())
                    .price(product.getPrice())
                    .build();
            cart.getItems().add(newItem);
        }

        cart.recalculateTotal();
        Cart savedCart = cartRepository.save(cart);
        return EntityMapper.toCartResponse(savedCart);
    }

    @Override
    @Transactional
    public CartResponse updateItem(User user, Long itemId, CartItemRequest request) {
        Cart cart = getOrCreateCart(user);

        CartItem item = cartItemRepository.findById(itemId)
                .orElseThrow(() -> new ResourceNotFoundException("CartItem", "id", itemId));

        if (!item.getCart().getId().equals(cart.getId())) {
            throw new ResourceNotFoundException("CartItem", "id", itemId);
        }

        item.setQuantity(request.getQuantity());
        cartItemRepository.save(item);

        cart.recalculateTotal();
        Cart savedCart = cartRepository.save(cart);
        return EntityMapper.toCartResponse(savedCart);
    }

    @Override
    @Transactional
    public void removeItem(User user, Long itemId) {
        Cart cart = getOrCreateCart(user);

        CartItem item = cartItemRepository.findById(itemId)
                .orElseThrow(() -> new ResourceNotFoundException("CartItem", "id", itemId));

        if (!item.getCart().getId().equals(cart.getId())) {
            throw new ResourceNotFoundException("CartItem", "id", itemId);
        }

        cart.getItems().remove(item);
        cartItemRepository.delete(item);

        cart.recalculateTotal();
        cartRepository.save(cart);
    }

    @Override
    @Transactional
    public void clearCart(User user) {
        Cart cart = getOrCreateCart(user);
        cart.getItems().clear();
        cart.setTotalPrice(0.0);
        cartRepository.save(cart);
    }

    private Cart getOrCreateCart(User user) {
        return cartRepository.findByUser(user)
                .orElseGet(() -> {
                    Cart newCart = Cart.builder()
                            .user(user)
                            .totalPrice(0.0)
                            .build();
                    return cartRepository.save(newCart);
                });
    }
}
