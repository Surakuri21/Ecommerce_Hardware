package com.Surakuri.Repository;

import com.Surakuri.Model.entity.Payment_Orders.PaymentOrder;
import com.Surakuri.Domain.PaymentOrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentOrderRepository extends JpaRepository<PaymentOrder, Long> {

    // This works for GCash, PayMaya, or Card.
    // It looks inside the embedded 'paymentDetails' for the generic 'paymentId'.
    Optional<PaymentOrder> findByPaymentDetails_PaymentId(String paymentId);
}
