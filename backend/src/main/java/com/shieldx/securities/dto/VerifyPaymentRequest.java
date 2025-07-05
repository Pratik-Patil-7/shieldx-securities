package com.shieldx.securities.dto;

import lombok.Data;

@Data
public class VerifyPaymentRequest {
    private Integer paymentId;
    private String razorpayPaymentId;
    private String razorpayOrderId;
    private String razorpaySignature;
}
