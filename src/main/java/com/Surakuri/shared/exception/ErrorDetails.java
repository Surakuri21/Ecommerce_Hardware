package com.Surakuri.shared.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ErrorDetails {
    private LocalDateTime timestamp;
    private String message;
    private String details; // e.g., the API URL that failed
    private String status;  // e.g., "NOT_FOUND" or "BAD_REQUEST"
}