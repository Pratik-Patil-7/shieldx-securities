package com.shieldx.securities.dto;

import lombok.Data;

@Data
public class BouncerRequest {
    private String name;
    private String gender;
    private String age;
    private String email;
    private String mobile;
    private String address;
    private String qualification;
    private String experience;
    private String isArmed;
}