package com.Surakuri.Controller;

import com.Surakuri.Model.dto.SignupRequest;
import com.Surakuri.Model.entity.Other_Business_Entities.Seller;
import com.Surakuri.Model.entity.User_Cart.User;
import com.Surakuri.Service.AuthService;
import com.Surakuri.Service.SellerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private SellerService sellerService;

    @Autowired
    private AuthService authService;

    @PutMapping("/sellers/{sellerId}/approve")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Seller> approveSeller(@PathVariable Long sellerId) {
        Seller updatedSeller = sellerService.approveSeller(sellerId);
        return ResponseEntity.ok(updatedSeller);
    }

    /**
     * Creates a new user with the ADMIN role.
     * This endpoint is protected and only accessible by existing admins.
     */
    @PostMapping("/users/create-admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<User> createAdminUser(@RequestBody SignupRequest req) {
        User newAdmin = authService.createAdmin(req);
        return new ResponseEntity<>(newAdmin, HttpStatus.CREATED);
    }
}