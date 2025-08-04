package com.shieldx.securities.dto;

import lombok.Data;

@Data
public class VerifyPaymentRequest {
	private Integer bookingId;
    private String paymentId;
    private Integer bouncerId;
}
