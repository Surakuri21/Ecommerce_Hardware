package com.Surakuri.Model;


import com.Surakuri.Domain.PaymentMethod;
import com.Surakuri.Domain.PaymentOrderStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class PaymentOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Long amount;

    private PaymentOrderStatus status = PaymentOrderStatus.PENDING;

    private PaymentMethod paymentMethod;

    @OneToOne(cascade = CascadeType.ALL)
    private PaymentDetails paymentDetails;

    @ManyToOne
    private User user;

    @OneToMany
    private Set<Order> orders = new HashSet<>();

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;



}
