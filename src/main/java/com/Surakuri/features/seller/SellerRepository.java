package com.Surakuri.features.seller;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SellerRepository extends JpaRepository<Seller, Long> {

    /**
     * Finds a seller profile associated with a specific user ID.
     * This is the primary way to get seller details from a logged-in user.
     * @param userId The ID of the user.
     * @return An Optional containing the Seller profile.
     */
    Optional<Seller> findByUserId(Long userId);

    /**
     * For Admin to see all pending applications.
     * @param status The account status to filter by.
     * @return A list of sellers with the given status.
     */
    List<Seller> findByAccountStatus(AccountStatus status);
}