package com.Surakuri.Controller;

import com.Surakuri.Model.dto.CreateProductRequest;
import com.Surakuri.Model.entity.Other_Business_Entities.Seller;
import com.Surakuri.Model.entity.Products_Categories.Product;
import com.Surakuri.Service.ProductService;
import com.Surakuri.Service.SellerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private SellerService sellerService;

    /**
     * Endpoint to create a new product. This is protected and only accessible by authenticated sellers.
     * The seller is identified via their JWT token.
     * URL: POST http://localhost:2121/api/products/create
     */
    @PostMapping("/create")
    @PreAuthorize("hasRole('SELLER')")
    public ResponseEntity<Product> createProduct(@RequestBody CreateProductRequest req) {
        Seller seller = sellerService.findSellerProfileByJwt();
        Product product = productService.createProduct(req, seller.getId());
        return new ResponseEntity<>(product, HttpStatus.CREATED);
    }

    /**
     * Public endpoint to search and filter products.
     * Supports filtering by name, brand, category, and price range.
     * Also supports pagination and sorting via standard Pageable parameters.
     *
     * Example: GET /api/products?name=Drill&category=Power Tools&minPrice=1000&sort=price,asc
     */
    @GetMapping
    public ResponseEntity<Page<Product>> searchProducts(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String brand,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            Pageable pageable
    ) {
        Page<Product> products = productService.searchProducts(name, brand, category, minPrice, maxPrice, pageable);
        return ResponseEntity.ok(products);
    }
}