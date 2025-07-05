package com.shieldx.securities.dto;

import lombok.Data;

@Data
public class AuthResponse {
    private String token;
    private Integer userId;
    private String email;

    public AuthResponse(String token, Integer userId, String email) {
        this.token = token;
        this.userId = userId;
        this.email = email;
    }
}