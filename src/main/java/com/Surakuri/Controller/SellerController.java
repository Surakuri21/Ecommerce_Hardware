package com.Surakuri.Controller;

import com.Surakuri.Domain.PaymentOrderStatus;
import com.Surakuri.Model.dto.SellerRegisterRequest;
import com.Surakuri.Model.entity.Payment_Orders.Order;
import com.Surakuri.Service.SellerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sellers")
public class SellerController {

    @Autowired
    private SellerService sellerService;

    /**
     * Public endpoint for a new seller to register.
     * The account will be created with a PENDING_VERIFICATION status.
     */
    @PostMapping("/register")
    public ResponseEntity<String> registerSeller(@RequestBody SellerRegisterRequest req) {
        sellerService.registerSeller(req);
        String message = "Seller Registration Successful! Please wait for Admin approval.";
        return new ResponseEntity<>(message, HttpStatus.CREATED);
    }

    /**
     * Retrieves the order history for the currently authenticated seller.
     * This endpoint is protected and only accessible by users with the ROLE_SELLER.
     *
     * @return A list of orders that contain products sold by the seller.
     */
    @GetMapping("/my-orders")
    @PreAuthorize("hasRole('SELLER')")
    public ResponseEntity<List<Order>> getSellerOrders() {
        List<Order> orders = sellerService.findSellerOrders();
        return ResponseEntity.ok(orders);
    }

    /**
     * Updates the status of an order.
     * Only the seller who owns the products in the order can update its status.
     * URL: PATCH http://localhost:2121/api/sellers/orders/{orderId}/status?status=SHIPPED
     */
    @PatchMapping("/orders/{orderId}/status")
    @PreAuthorize("hasRole('SELLER')")
    public ResponseEntity<Order> updateOrderStatus(
            @PathVariable Long orderId,
            @RequestParam PaymentOrderStatus status
    ) {
        Order updatedOrder = sellerService.updateOrderStatus(orderId, status);
        return ResponseEntity.ok(updatedOrder);
    }
}