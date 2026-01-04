package com.Surakuri.Repository;

import com.Surakuri.Model.entity.Products_Categories.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    // ==========================================
    // 1. BASIC SEARCH (Optimized for E-commerce)
    // ==========================================

    // Find by Brand (e.g., "Phelps Dodge")
    // We return a List here, but you could also use Page<Product> if you have many items per brand
    List<Product> findByBrand(String brand);

    // Search by Name (e.g., "Pipe") - Case Insensitive
    List<Product> findByNameContainingIgnoreCase(String keyword);

    // Find active products (Hide deleted/out-of-stock items from the main page)
    // Updated to return a Page (Standard for "Shop All" pages)
    Page<Product> findByIsActiveTrue(Pageable pageable);

    // ==========================================
    // 2. ADVANCED FILTERING (The "Hardware Store" Logic)
    // ==========================================

    // Find by Category ID (e.g., User clicks "Plumbing" button)
    Page<Product> findByCategoryId(Long categoryId, Pageable pageable);

    // Find by Price Range (e.g., Budget filtering)
    // Note: We use BigDecimal because your Entity now uses BigDecimal
    List<Product> findByPriceBetween(BigDecimal minPrice, BigDecimal maxPrice);

    // Find by SKU (Vital for Admin/Inventory lookup)
    // Returns Optional because a typo in SKU might mean no product is found
    Optional<Product> findBySku(String sku);

    // ==========================================
    // 3. CUSTOM SEARCH QUERY (The "Smart" Search)
    // ==========================================

    // This searches Name OR Brand OR Description for the keyword.
    // Example: User types "Eagle". This finds "Eagle Cement" (Brand) AND "Eagle Pliers" (Name).
    @Query("SELECT p FROM Product p WHERE " +
            "p.isActive = true AND " +
            "(LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(p.brand) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(p.description) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    Page<Product> searchProducts(@Param("keyword") String keyword, Pageable pageable);
}