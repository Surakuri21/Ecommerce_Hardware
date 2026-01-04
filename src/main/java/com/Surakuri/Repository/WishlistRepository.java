package com.Surakuri.Repository;

import com.Surakuri.Model.entity.Other_Business_Entities.Wishlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WishlistRepository extends JpaRepository<Wishlist, Long> {

    // Find the wishlist belonging to a specific user
    // Usage: wishlistRepo.findByUserId(currentUser.getId())
    Optional<Wishlist> findByUserId(Long userId);

    // Helper: Check if a specific product is in a user's wishlist
    // (Useful for showing the "Heart" icon as filled or empty on the product page)
    boolean existsByUserIdAndProductsId(Long userId, Long productId);
}