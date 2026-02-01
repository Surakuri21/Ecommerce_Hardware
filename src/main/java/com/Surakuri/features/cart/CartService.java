package com.Surakuri.features.cart;

import com.Surakuri.shared.exception.ProductOutOfStockException;
import com.Surakuri.shared.exception.ResourceNotFoundException;
import com.Surakuri.features.cart.DTO.AddItemRequest;
import com.Surakuri.features.cart.DTO.CartItemResponse;
import com.Surakuri.features.cart.DTO.CartResponse;
import com.Surakuri.features.product.ProductVariant;
import com.Surakuri.features.user.User;
import com.Surakuri.features.product.ProductVariantRepository;
import com.Surakuri.features.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CartService {

    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private CartItemRepository cartItemRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ProductVariantRepository variantRepository;

    @Transactional
    public CartResponse addItemToCart(Long userId, AddItemRequest req) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Cart cart = cartRepository.findByUserId(userId)
                .orElseGet(() -> {
                    Cart newCart = new Cart();
                    newCart.setUser(user);
                    return cartRepository.save(newCart);
                });

        ProductVariant variant = variantRepository.findById(req.getVariantId())
                .orElseThrow(() -> new ResourceNotFoundException("Product Variant not found"));

        if (variant.getStockQuantity() < req.getQuantity()) {
            throw new ProductOutOfStockException("Insufficient stock for " + variant.getVariantName());
        }

        Optional<CartItem> existingItemOpt = cartItemRepository.findByCartIdAndVariantId(cart.getId(), variant.getId());

        if (existingItemOpt.isPresent()) {
            CartItem item = existingItemOpt.get();
            item.setQuantity(item.getQuantity() + req.getQuantity());
            cartItemRepository.save(item);
        } else {
            CartItem newItem = new CartItem();
            newItem.setCart(cart);
            newItem.setVariant(variant);
            newItem.setQuantity(req.getQuantity());
            cartItemRepository.save(newItem);
        }

        Cart updatedCart = cartRepository.findByUserIdWithItems(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found after update"));

        return mapToResponse(updatedCart);
    }

    public CartResponse findUserCart(Long userId) {
        Cart cart = cartRepository.findByUserIdWithItems(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found for user ID: " + userId));
        return mapToResponse(cart);
    }

    private CartResponse mapToResponse(Cart cart) {
        CartResponse res = new CartResponse();
        res.setCartId(cart.getId());

        if (cart.getUser() != null) {
            res.setCustomerName(cart.getUser().getFirstName() + " " + cart.getUser().getLastName());
        }

        List<CartItemResponse> itemDTOs = new ArrayList<>();
        BigDecimal totalAmount = BigDecimal.ZERO;
        int totalItems = 0;

        if (cart.getCartItems() != null) {
            for (CartItem item : cart.getCartItems()) {
                ProductVariant variant = item.getVariant();
                if (variant == null) continue;

                BigDecimal subtotal = variant.getPrice().multiply(new BigDecimal(item.getQuantity()));

                CartItemResponse itemRes = new CartItemResponse();
                itemRes.setCartItemId(item.getId());
                itemRes.setPrice(variant.getPrice());
                itemRes.setQuantity(item.getQuantity());
                itemRes.setSubtotal(subtotal);
                itemRes.setVariantName(variant.getVariantName());

                if (variant.getProduct() != null) {
                    itemRes.setProductName(variant.getProduct().getName());
                    itemRes.setImageUrl(variant.getProduct().getImageUrl());
                }

                itemDTOs.add(itemRes);
                totalAmount = totalAmount.add(subtotal);
                totalItems += item.getQuantity();
            }
        }

        res.setItems(itemDTOs);
        res.setTotalSellingPrice(totalAmount);
        res.setTotalItems(totalItems);

        return res;
    }
}