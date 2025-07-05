package com.shieldx.securities.dto;

import lombok.Data;

@Data
public class SecurityTypeRequest {
    private String levelName;
    private String description;
    private String isArmed;
    private String pricePerDay;
}