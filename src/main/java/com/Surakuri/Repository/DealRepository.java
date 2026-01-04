package com.Surakuri.Repository;

import com.Surakuri.Model.entity.Products_Categories.Deal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DealRepository extends JpaRepository<Deal, Long> {

    // Fetch all active deals for the homepage
    List<Deal> findAll();
}