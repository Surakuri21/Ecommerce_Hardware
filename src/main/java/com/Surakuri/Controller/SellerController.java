package com.Surakuri.Controller;

import com.Surakuri.Model.dto.AuthResponse; // Correct import for the new DTO
import com.Surakuri.Model.dto.SellerRegisterRequest;
import com.Surakuri.Model.entity.Other_Business_Entities.Seller;
import com.Surakuri.Service.SellerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/sellers")
public class SellerController {

    @Autowired
    private SellerService sellerService;

    // URL: http://localhost:2121/sellers/register
    @PostMapping("/register")
    public ResponseEntity<String> registerSeller(@RequestBody SellerRegisterRequest req) {
        sellerService.registerSeller(req);
        String message = "Seller Registration Successful! Please wait for Admin approval.";
        return new ResponseEntity<>(message, HttpStatus.CREATED);
    }
}