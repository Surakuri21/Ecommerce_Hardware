package com.Surakuri.Controller;

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
}