package com.Surakuri.Model.entity.Products_Categories;

// NOTE: Consider moving this to a package like 'com.Surakuri.Model.entity.Marketing' for better organization.
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Represents a promotional deal or sale event, such as "20% Off Summer Sale".
 * This entity is designed to be linked to a homepage section (`HomeCategory`) to display
 * promotional banners or tags. It defines the discount and its duration.
 */
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(exclude = "category") // Exclude relationships to prevent infinite loops
@Table(name = "deals")
public class Deal {

    /**
     * The unique identifier for the deal. This is the primary key.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "deal_id")
    private Long id;

    /**
     * The title of the deal, which can be displayed on the frontend (e.g., "Summer Sale", "Clearance").
     */
    @Column(nullable = false)
    private String title;

    /**
     * The discount percentage for this deal (e.g., 20.00 for 20% off).
     * Using BigDecimal is a best practice for any value related to pricing or discounts to avoid precision errors.
     */
    @Column(name = "discount_percentage", nullable = false)
    private BigDecimal discountPercentage;

    /**
     * The date from which the deal is active.
     */
    @Column(name = "valid_from")
    private LocalDate validFrom;

    /**
     * The date until which the deal is active (inclusive).
     */
    @Column(name = "valid_until")
    private LocalDate validUntil;

    /**
     * A flag to manually enable or disable the deal, regardless of the active dates.
     */
    @Column(name = "is_active")
    private boolean isActive = true;

    /**
     * The homepage section to which this deal is attached.
     * This creates a one-to-one link, allowing a homepage category like "Summer Sale"
     * to be associated with a specific deal banner or tag.
     */
    @OneToOne
    @JoinColumn(name = "home_category_id")
    private HomeCategory category;
}