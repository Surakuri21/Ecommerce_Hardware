package com.Surakuri.Repository;

import com.Surakuri.Model.entity.Other_Business_Entities.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {

    // ==========================================
    // 1. USER LOOKUP (For Checkout & Profile)
    // ==========================================

    // Find the address belonging to a specific user
    // Usage: addressRepository.findByUserId(currentUser.getId());
    // Returns Optional because a new user might not have set an address yet.
    Optional<Address> findByUserId(Long userId);


    // ==========================================
    // 2. LOGISTICS & ADMIN (Philippine Context)
    // ==========================================

    // Find all addresses in a specific City (e.g., "Show all deliveries in Davao City")
    // Useful for grouping shipments to save gas/logistics costs.
    List<Address> findByCity(String city);

    // Find all addresses in a Region (e.g., "NCR", "Region VII")
    // Useful for calculating shipping fees (e.g., NCR = ₱50, Visayas = ₱150)
    List<Address> findByRegion(String region);


    // ==========================================
    // 3. MAINTENANCE
    // ==========================================

    // Delete address by User ID (e.g., if a user deletes their account)
    void deleteByUserId(Long userId);
}