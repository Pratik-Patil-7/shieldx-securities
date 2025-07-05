package com.shieldx.securities.dto;

import lombok.Data;

@Data
public class InitiatePaymentRequest {
	private Integer bookingId;
	private String amount;
}