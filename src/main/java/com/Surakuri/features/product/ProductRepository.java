package com.Surakuri.features.product;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {

    // ==========================================
    // 1. BASIC SEARCH (Optimized for E-commerce)
    // ==========================================

    // Find by Brand (e.g., "Phelps Dodge")
    List<Product> findByBrand(String brand);

    // Search by Name (e.g., "Pipe") - Case Insensitive
    List<Product> findByNameContainingIgnoreCase(String keyword);

    // Find active products (Hide deleted/out-of-stock items from the main page)
    Page<Product> findByIsActiveTrue(Pageable pageable);

    // ==========================================
    // 2. ADVANCED FILTERING (The "Hardware Store" Logic)
    // ==========================================

    // Find by Category ID (e.g., User clicks "Plumbing" button)
    Page<Product> findByCategoryId(Long categoryId, Pageable pageable);

    // Find by Price Range (e.g., Budget filtering)
    List<Product> findByPriceBetween(BigDecimal minPrice, BigDecimal maxPrice);

    // Find by SKU (Vital for Admin/Inventory lookup)
    Optional<Product> findBySku(String sku);

    // ==========================================
    // 3. CUSTOM SEARCH QUERY (The "Smart" Search)
    // ==========================================

    // This searches Name OR Brand OR Description for the keyword.
    @Query("SELECT p FROM Product p WHERE " +
            "p.isActive = true AND " +
            "(LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(p.brand) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(p.description) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    Page<Product> searchProducts(@Param("keyword") String keyword, Pageable pageable);
}