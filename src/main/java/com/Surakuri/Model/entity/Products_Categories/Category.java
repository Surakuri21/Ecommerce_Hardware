package com.Surakuri.Model.entity.Products_Categories;

// NOTE: Consider standardizing this package to 'com.Surakuri.Model.entity.Product_Inventory' to group all product-related entities.
import com.Surakuri.Model.entity.Products_Categories.Product;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Represents a category for organizing products.
 * This entity supports a hierarchical (tree) structure, allowing categories to have a parent and multiple sub-categories.
 * Example: "Tools" (parent) -> "Power Tools" (child) -> "Drills" (grandchild).
 */
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
// FIX: Exclude all relationships from equals and hashCode to prevent infinite loops and performance issues.
@EqualsAndHashCode(exclude = {"subCategories", "parentCategory", "products"})
@Table(name = "categories")
public class Category {

    /**
     * The unique identifier for the category. This is the primary key.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private Long id;

    /**
     * The name of the category (e.g., "Power Tools"). Must be unique.
     */
    @Column(nullable = false, unique = true)
    private String name;

    /**
     * A detailed description of the category and the types of products it contains.
     */
    @Column(columnDefinition = "TEXT")
    private String description;

    /**
     * The parent category in the hierarchy. A null value indicates this is a top-level category.
     * `FetchType.LAZY` is used for performance optimization, preventing the entire category tree from being loaded at once.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    @JsonIgnore // Stop Parent Loop
    private Category parentCategory;

    /**
     * The set of child categories that belong to this category.
     * `mappedBy` indicates that the `parentCategory` field in the child `Category` entity owns this relationship.
     */
    @OneToMany(mappedBy = "parentCategory", cascade = CascadeType.ALL)
    @JsonIgnore // Stop Child Loop
    private Set<Category> subCategories = new HashSet<>();

    /**
     * The list of products that belong directly to this category.
     * This is the inverse side of the many-to-one relationship in the Product entity.
     * It's ignored during JSON serialization to prevent a circular dependency with Product.
     */
    @OneToMany(mappedBy = "category")
    @JsonIgnore
    private List<Product> products = new ArrayList<>();

}