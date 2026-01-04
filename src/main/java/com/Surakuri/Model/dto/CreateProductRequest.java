package com.Surakuri.Model.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

@Data
public class CreateProductRequest {
    // General Info
    private String name;        // "Phelps Dodge THHN Wire"
    private String brand;       // "Phelps Dodge"
    private String description; // "Heat resistant up to 90C"
    private String imageUrl;
    private String categoryName; // "Electrical" (We will look up the ID)

    // The Variations (Sizes/types)
    private List<VariantRequest> variants;
}

