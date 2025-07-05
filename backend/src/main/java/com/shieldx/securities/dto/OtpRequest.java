package com.shieldx.securities.dto;

import lombok.Data;

@Data
public class OtpRequest {
    private String email;
    private String otpCode;
}