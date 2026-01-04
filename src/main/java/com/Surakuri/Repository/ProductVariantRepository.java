package com.Surakuri.Repository;

import com.Surakuri.Model.entity.Products_Categories.ProductVariant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductVariantRepository extends JpaRepository<ProductVariant, Long> {

    // ==========================================
    // 1. STOREFRONT DISPLAY
    // ==========================================

    // Find all size/weight options for a specific Product
    // Usage: When User clicks "Republic Cement", show list: ["40kg", "1kg"]
    List<ProductVariant> findByProductId(Long productId);


    // ==========================================
    // 2. INVENTORY & ADMIN MANAGEMENT
    // ==========================================

    // Find specific item by unique SKU code
    // Usage: Barcode scanner or Admin search
    Optional<ProductVariant> findBySku(String sku);

    // Check if SKU exists (To prevent duplicates when adding new stock)
    boolean existsBySku(String sku);
}