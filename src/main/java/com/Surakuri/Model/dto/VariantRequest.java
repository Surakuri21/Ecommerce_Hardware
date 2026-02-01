package com.Surakuri.Model.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.math.BigDecimal;
import java.util.Map;
import java.util.HashMap;

/**
 * A Data Transfer Object (DTO) representing a request to create or update a product variant.
 *
 * <p>This DTO uses a flexible attribute map to handle the wide variety of specifications
 * found in hardware products. Instead of fixed fields like 'size' or 'color', it uses a
 * {@code Map<String, String>} to capture any and all variant-defining characteristics.</p>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class VariantRequest {

    /**
     * The unique Stock Keeping Unit (SKU) for this variant.
     * Example: "CEM-40KG"
     */
    private String sku;

    /**
     * The selling price of this specific variant.
     */
    private BigDecimal price;

    /**
     * The initial stock quantity for this variant.
     */
    private int quantity;

    /**
     * A map of key-value pairs representing the unique attributes of this variant.
     * This allows for maximum flexibility in defining product specifications.
     *
     * <p><b>Examples:</b></p>
     * <ul>
     *   <li>For Cement: {@code {"Weight": "40kg", "Type": "Portland Type 1"}}</li>
     *   <li>For a Pipe: {@code {"Diameter": "1/2 inch", "Length": "10 feet"}}</li>
     *   <li>For Paint: {@code {"Color": "Red", "Finish": "Gloss", "Volume": "1 Liter"}}</li>
     * </ul>
     */
    private Map<String, String> attributes = new HashMap<>();
}