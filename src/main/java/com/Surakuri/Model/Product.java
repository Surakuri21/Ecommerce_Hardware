package com.Surakuri.Model;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String title;

    private String description;

    private int mrpPrice;

    private int sellingPrice;

    private int discountPercent;

    private int quantity;

    @ManyToOne
    private Category category;

    @ElementCollection
    private List<String> imgages = new ArrayList<>();

    private int numRatings;

    private String color;

    @ManyToOne
    private Seller seller;

    private LocalDateTime createdAt;

    private String size;

    @OneToMany()
    private List<Review> reviews = new ArrayList<>();




}
