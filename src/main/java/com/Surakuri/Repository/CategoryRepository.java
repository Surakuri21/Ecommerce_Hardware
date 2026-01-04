package com.Surakuri.Repository;

import com.Surakuri.Model.entity.Products_Categories.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    // 1. Find Main Categories (Root Level)
    // Useful for the main navbar (e.g., "Construction", "Electrical")
    // These are categories that have NO parent.
    List<Category> findByParentCategoryIsNull();

    // 2. Find Sub-Categories by Parent ID
    // Useful when clicking "Construction" to see "Cement", "Steel", etc.
    List<Category> findByParentCategoryId(Long parentId);

    // 3. Find by Name
    Category findByName(String name);
}