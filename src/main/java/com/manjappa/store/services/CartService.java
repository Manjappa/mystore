package com.manjappa.store.services;

import com.manjappa.store.dtos.CartDto;
import com.manjappa.store.dtos.CartItemDto;
import com.manjappa.store.entities.Cart;
import com.manjappa.store.entities.CartItem;
import com.manjappa.store.entities.Product;
import com.manjappa.store.exceptions.CartNotFoundException;
import com.manjappa.store.exceptions.ProductNotFoundException;
import com.manjappa.store.mapper.CartMapper;
import com.manjappa.store.repositories.CartRepository;
import com.manjappa.store.repositories.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.UUID;

@Service
@AllArgsConstructor
public class CartService {
    private final ProductRepository productRepository;
    private CartRepository cartRepository;
    private CartMapper cartMapper;

    public CartDto createCart() {
        Cart cart = new Cart();
        cartRepository.save(cart);
        return cartMapper.toCartDto(cart);
    }

    public CartItemDto addToCart(UUID cartId, Long productId) {
        Cart cart = cartRepository.getCartWithItems(cartId).orElse(null);
        if (cart == null) {
           throw new CartNotFoundException();
        }
        Product product = productRepository.findById(productId).orElse(null);
        if (product == null) {
            throw new ProductNotFoundException();
        }
        CartItem cartItem = cart.addItem(product);
        cartRepository.save(cart);
        return cartMapper.toCartItemDto(cartItem);
    }

    public CartDto getCart(UUID cartId) {
        Cart cart = cartRepository.getCartWithItems(cartId).orElse(null); //Avoid Eager loading
        if (cart == null) {
            throw new CartNotFoundException();
        }
        return cartMapper.toCartDto(cart);
    }

    public CartItemDto updateItem(UUID cartId, Long productId, Integer quantity) {
        Cart cart = cartRepository.getCartWithItems(cartId).orElse(null);
        if (cart == null) {
            throw new CartNotFoundException();
        }
        var cartItem =  cart.getItem(productId);
        if (cartItem == null) {
            throw new ProductNotFoundException();
        }
        return cartMapper.toCartItemDto(cartItem);
    }

    public void removeItem(UUID cartId, Long productId) {
        Cart cart = cartRepository.getCartWithItems(cartId).orElse(null);
        if (cart == null) {
            throw new CartNotFoundException();
        }
        var cartItem = cart.getItem(productId);
        if (cartItem == null) {
            throw new ProductNotFoundException();
        }
        cartRepository.delete(cart);
    }

    public void clearCart(UUID cartId) {
        Cart cart = cartRepository.getCartWithItems(cartId).orElse(null);
        if (cart == null) {
            throw new CartNotFoundException();
        }
        cart.clear();
        cartRepository.save(cart);

    }
}
