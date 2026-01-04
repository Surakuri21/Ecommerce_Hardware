package com.Surakuri.Model.dto;


import com.Surakuri.Model.entity.Products_Categories.HomeCategory;
import com.Surakuri.Model.entity.Products_Categories.Deal;
import lombok.Data;

import java.util.List;

@Data
public class HomePage {

    private List<HomeCategory> home;

    private List<Deal> dealsPromotion;

    private List<HomeCategory> toolsEquipment;

    private List<HomeCategory> buildingConstruction;

    private List<HomeCategory> electricalLightning;

    private List<HomeCategory> plumbingPipes;

    private List<HomeCategory> gardenOutdoor;

    private List<HomeCategory> specialCategories;

















}
