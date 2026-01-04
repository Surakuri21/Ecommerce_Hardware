package com.Surakuri.Repository;

import com.Surakuri.Model.entity.User_Cart.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    // ==========================================
    // 1. THE "ADD TO CART" CHECKER
    // ==========================================

    // When a user clicks "Add to Cart", we first check:
    // "Does this specific Cart already contain this specific Variant (Size/SKU)?"
    // If YES -> We just increase quantity (+1).
    // If NO  -> We create a new CartItem row.
    Optional<CartItem> findByCartIdAndVariantId(Long cartId, Long variantId);


    // ==========================================
    // 2. CLEANUP (After Checkout)
    // ==========================================

    // Deletes all items in a specific cart.
    // Usage: Call this immediately after an Order is successfully placed (PAID).
    @Modifying // Tells Spring this modifies the DB (DELETE), not just reads.
    @Query("DELETE FROM CartItem ci WHERE ci.cart.id = :cartId")
    void deleteAllByCartId(Long cartId);
}