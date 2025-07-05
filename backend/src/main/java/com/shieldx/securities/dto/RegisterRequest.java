package com.shieldx.securities.dto;

import lombok.Data;

@Data
public class RegisterRequest {
    private String firstName;
    private String lastName;
    private String username;
    private String email;
    private String mobile;
    private String address;
    private String password;
}
