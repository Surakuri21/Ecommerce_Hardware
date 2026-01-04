package com.Surakuri.Repository;

import com.Surakuri.Domain.HomeCategorySection;
import com.Surakuri.Model.entity.Products_Categories.HomeCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HomeCategoryRepository extends JpaRepository<HomeCategory, Long> {

    // ==========================================
    // 1. HOMEPAGE LAYOUT QUERIES
    // ==========================================

    // Fetch categories for a specific section.
    // Usage:
    // repo.findBySection(HomeCategorySection.SLIDER) -> Returns images for the top carousel.
    // repo.findBySection(HomeCategorySection.GRID)   -> Returns items for the "Shop by Category" box.
    List<HomeCategory> findBySection(HomeCategorySection section);

}