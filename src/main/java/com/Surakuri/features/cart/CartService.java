package com.Surakuri.features.cart;

import com.Surakuri.shared.exception.ProductOutOfStockException;
import com.Surakuri.shared.exception.ResourceNotFoundException;
import com.Surakuri.features.cart.DTO.AddItemRequest;
import com.Surakuri.features.cart.DTO.CartItemResponse;
import com.Surakuri.features.cart.DTO.CartResponse;
import com.Surakuri.features.product.ProductVariant;
import com.Surakuri.features.product.ProductService; // Import ProductService
import com.Surakuri.features.user.User;
import com.Surakuri.features.user.UserService; // Import UserService
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
    private UserService userService; // Use UserService instead of Repository
    @Autowired
    private ProductService productService; // Use ProductService instead of Repository

    @Transactional
    public CartResponse addItemToCart(Long userId, AddItemRequest req) {
        User user = userService.findById(userId); // Use UserService

        // 1. Get the cart (eagerly fetch items if possible, but we will refresh later)
        Cart cart = cartRepository.findByUserIdWithItems(userId)
                .orElseGet(() -> {
                    Cart newCart = new Cart();
                    newCart.setUser(user);
                    return cartRepository.save(newCart);
                });

        // 2. Get the variant
        ProductVariant variant = productService.getProductVariantById(req.getVariantId());

        if (variant.getStockQuantity() < req.getQuantity()) {
            throw new ProductOutOfStockException("Insufficient stock for " + variant.getVariantName());
        }

        // 3. Check if item exists using the repository (Direct DB check)
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
            
            // CRITICAL FIX: Save the item AND add it to the cart's collection in memory
            // This ensures that if we map the cart to response immediately, it shows the new item.
            cartItemRepository.save(newItem);
            cart.getCartItems().add(newItem); 
        }

        // 4. Force a refresh from the database to ensure we have the full object graph (Product, Variant, etc.)
        // We must flush changes first so the query sees the new item.
        cartItemRepository.flush(); 
        
        Cart updatedCart = cartRepository.findByUserIdWithItems(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found after update"));

        return mapToResponse(updatedCart);
    }

    public CartResponse findUserCart(Long userId) {
        Cart cart = cartRepository.findByUserIdWithItems(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found for user ID: " + userId));
        return mapToResponse(cart);
    }

    // New method for OrderService to get the Cart entity
    public Cart getCartEntityByUserId(Long userId) {
        return cartRepository.findByUserIdWithItems(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found"));
    }

    // New method for OrderService to clear the cart
    // FIX: Use JPA orphanRemoval instead of manual delete to avoid locking issues
    @Transactional
    public void clearCart(Cart cart) {
        cart.getCartItems().clear();
        cartRepository.save(cart);
    }

    // New method to create a cart (for AuthService)
    @Transactional
    public void createCartForUser(User user) {
        Cart cart = new Cart();
        cart.setUser(user);
        cartRepository.save(cart);
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