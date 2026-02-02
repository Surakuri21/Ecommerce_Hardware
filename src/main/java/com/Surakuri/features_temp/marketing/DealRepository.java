package com.Surakuri.features.marketing;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DealRepository extends JpaRepository<Deal, Long> {

    // Fetch all active deals for the homepage
    List<Deal> findAll();
}