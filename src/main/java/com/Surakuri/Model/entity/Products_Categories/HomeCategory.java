package com.Surakuri.Model.entity.Products_Categories;

// NOTE: Consider moving this to a package like 'com.Surakuri.Model.entity.Homepage' for better organization.
import com.Surakuri.Domain.HomeCategorySection;
import jakarta.persistence.*;
import lombok.*;

/**
 * Represents a category feature to be displayed on the homepage.
 * This entity links a visual element (like a banner or card with an image)
 * to an actual product category, allowing users to navigate from the homepage
 * directly to a category page.
 */
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(exclude = "category") // Exclude relationships to prevent infinite loops
@Table(name = "home_categories") // Matches your DB table
public class HomeCategory {

    /**
     * The unique identifier for the homepage category entry. This is the primary key.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Matches AUTO_INCREMENT
    private Long id;

    /**
     * The display name for the homepage feature (e.g., "Shop Power Tools", "New Arrivals").
     * This text will appear on the banner or card.
     */
    @Column(nullable = false)
    private String name;

    /**
     * The URL or path to the image used for this homepage feature.
     */
    @Column(nullable = false)
    private String image;

    // --- FIX START ---
    // Old Code: private String categoryId;
    // New Code: Link to the actual Category Entity
    /**
     * The actual product category that this homepage feature links to.
     * This creates a direct relationship, so clicking this feature on the homepage
     * will navigate the user to the corresponding category page.
     */
    @OneToOne
    @JoinColumn(name = "category_id") // Maps to the BIGINT column in MySQL
    private Category category;
    // --- FIX END ---

    /**
     * Defines which section of the homepage this feature belongs to.
     * This allows for organizing content into different areas like 'MAIN_CAROUSEL',
     * 'FEATURED_BANNERS', etc.
     */
    @Enumerated(EnumType.STRING)
    private HomeCategorySection section;
}