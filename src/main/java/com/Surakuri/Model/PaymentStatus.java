package com.Surakuri.Model;


import lombok.Data;


public enum PaymentStatus {


    PENDING,
    SUCCESS,
    PROCESSING,
    COMPLETED,
    FAILED,
    REFUNDED
}
