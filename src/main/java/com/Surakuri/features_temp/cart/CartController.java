package com.Surakuri.features.cart;

import com.Surakuri.features.cart.DTO.AddItemRequest;
import com.Surakuri.features.cart.DTO.CartResponse;
import com.Surakuri.features.user.User;
import com.Surakuri.features.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @Autowired
    private UserService userService;

    /**
     * Adds an item to the currently authenticated user's shopping cart.
     * The user is identified via their JWT token.
     */
    @PutMapping("/add")
    public ResponseEntity<CartResponse> addItemToCart(@RequestBody AddItemRequest req) {
        // Securely get the user ID from the JWT token.
        User user = userService.findUserProfileByJwt();
        CartResponse updatedCart = cartService.addItemToCart(user.getId(), req);
        return ResponseEntity.ok(updatedCart);
    }

    /**
     * Retrieves the shopping cart for the currently authenticated user.
     * The user is identified via their JWT token.
     */
    @GetMapping
    public ResponseEntity<CartResponse> findUserCart() {
        // Securely get the user ID from the JWT token.
        User user = userService.findUserProfileByJwt();
        CartResponse cart = cartService.findUserCart(user.getId());
        return ResponseEntity.ok(cart);
    }
}