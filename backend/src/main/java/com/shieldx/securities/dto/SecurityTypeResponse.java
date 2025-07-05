package com.shieldx.securities.dto;

import lombok.Data;

@Data
public class SecurityTypeResponse {
    private int stId;
    private String levelName;
    private String description;
    private String isArmed;
    private String pricePerDay;

    public SecurityTypeResponse(int stId, String levelName, String description, String isArmed, String pricePerDay) {
        this.stId = stId;
        this.levelName = levelName;
        this.description = description;
        this.isArmed = isArmed;
        this.pricePerDay = pricePerDay;
    }
}