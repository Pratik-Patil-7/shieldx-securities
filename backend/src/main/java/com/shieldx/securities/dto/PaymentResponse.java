package com.shieldx.securities.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class PaymentResponse {
    private int paymentId;
    private int bookingId;
    private int userId;
    private Double amount;
    private String status;
    private LocalDateTime paymentDate;

    public PaymentResponse(int paymentId, int bookingId, int userId, Double amount, 
                          String status, LocalDateTime paymentDate) {
        this.paymentId = paymentId;
        this.bookingId = bookingId;
        this.userId = userId;
        this.amount = amount;
        this.status = status;
        this.paymentDate = paymentDate;
    }
}