package com.Surakuri.Controller;

import com.Surakuri.Model.dto.CheckoutRequest;
import com.Surakuri.Model.dto.OrderResponse;
import com.Surakuri.Model.entity.Payment_Orders.Order;
import com.Surakuri.Model.entity.User_Cart.User;
import com.Surakuri.Service.OrderService;
import com.Surakuri.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller for handling all order-related operations.
 */
@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserService userService;

    /**
     * Creates an order from the user's current shopping cart.
     * This is a protected endpoint, and the user is identified via their JWT token.
     *
     * @param req The request body containing checkout details like addressId and paymentMethod.
     * @return A response DTO with the details of the newly created order.
     */
    @PostMapping("/checkout")
    public ResponseEntity<OrderResponse> checkout(@RequestBody CheckoutRequest req) {
        User user = userService.findUserProfileByJwt();
        OrderResponse orderResponse = orderService.checkout(user.getId(), req);
        return ResponseEntity.ok(orderResponse);
    }

    /**
     * Retrieves the order history for the currently authenticated user.
     * The user is identified via their JWT token.
     *
     * @return A list of the user's orders, sorted by most recent first.
     */
    @GetMapping("/user")
    public ResponseEntity<List<Order>> getUserOrders() {
        List<Order> orders = orderService.findUserOrders();
        return ResponseEntity.ok(orders);
    }
}