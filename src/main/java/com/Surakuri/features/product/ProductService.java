package com.Surakuri.features.product;

import com.Surakuri.features.product.DTO.CreateProductRequest;
import com.Surakuri.features.product.DTO.VariantRequest;
import com.Surakuri.features.seller.Seller;
import com.Surakuri.features.seller.SellerRepository;
import com.Surakuri.shared.exception.ResourceNotFoundException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private SellerRepository sellerRepository;
    @Autowired
    private ProductVariantRepository variantRepository; // Inject the repository here
    @Autowired
    private ObjectMapper objectMapper;

    @Transactional
    public Product createProduct(CreateProductRequest req, Long sellerId) {

        Seller seller = sellerRepository.findById(sellerId)
                .orElseThrow(() -> new RuntimeException("Seller not found"));

        Category category = categoryRepository.findByName(req.getCategoryName());
        if (category == null) {
            category = new Category();
            category.setName(req.getCategoryName());
            categoryRepository.save(category);
        }

        Product product = new Product();
        product.setSeller(seller);
        product.setCategory(category);
        product.setName(req.getName());
        product.setBrand(req.getBrand());
        product.setDescription(req.getDescription());
        product.setImageUrl(req.getImageUrl());
        product.setCreatedAt(LocalDateTime.now());
        product.setActive(true);

        Product savedProduct = productRepository.save(product);

        List<ProductVariant> variantList = savedProduct.getVariants();

        for (VariantRequest vReq : req.getVariants()) {
            ProductVariant variant = new ProductVariant();
            variant.setProduct(savedProduct);
            variant.setSku(vReq.getSku());
            variant.setPrice(vReq.getPrice());
            variant.setStockQuantity(vReq.getQuantity());
            variant.setMinStockLevel(10);

            Map<String, String> attributes = vReq.getAttributes();
            try {
                variant.setSpecifications(objectMapper.writeValueAsString(attributes));
            } catch (JsonProcessingException e) {
                throw new RuntimeException("Error processing variant attributes", e);
            }

            String generatedName = attributes.values().stream().collect(Collectors.joining(" / "));
            variant.setVariantName(generatedName);

            if (attributes.containsKey("Weight")) {
                try {
                    String weightStr = attributes.get("Weight").replaceAll("[^\\d.]", "");
                    variant.setWeightKg(new BigDecimal(weightStr));
                } catch (NumberFormatException e) {
                    variant.setWeightKg(BigDecimal.ZERO);
                }
            } else {
                variant.setWeightKg(BigDecimal.ZERO);
            }

            variantList.add(variant);
        }

        if (!variantList.isEmpty()) {
            savedProduct.setPrice(variantList.get(0).getPrice());
            savedProduct.setWeightKg(variantList.get(0).getWeightKg());
        }

        return productRepository.save(savedProduct);
    }

    public Page<Product> findAllProducts(Pageable pageable) {
        return productRepository.findAll(pageable);
    }

    /**
     * Advanced search for products with multiple filters.
     */
    public Page<Product> searchProducts(String name, String brand, String category, BigDecimal minPrice, BigDecimal maxPrice, Pageable pageable) {
        Specification<Product> spec = Specification.where(ProductSpecification.isActive());

        if (name != null && !name.isEmpty()) {
            spec = spec.and(ProductSpecification.hasName(name));
        }
        if (brand != null && !brand.isEmpty()) {
            spec = spec.and(ProductSpecification.hasBrand(brand));
        }
        if (category != null && !category.isEmpty()) {
            spec = spec.and(ProductSpecification.hasCategory(category));
        }
        if (minPrice != null || maxPrice != null) {
            spec = spec.and(ProductSpecification.priceBetween(minPrice, maxPrice));
        }

        return productRepository.findAll(spec, pageable);
    }

    // New method to expose variant retrieval
    public ProductVariant getProductVariantById(Long id) {
        return variantRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product Variant not found"));
    }
    
    // New method to save variant (needed for stock updates)
    public ProductVariant saveProductVariant(ProductVariant variant) {
        return variantRepository.save(variant);
    }
}