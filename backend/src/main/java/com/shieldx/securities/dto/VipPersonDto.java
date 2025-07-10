package com.shieldx.securities.dto;

import java.time.LocalDate;

import lombok.Data;

@Data
public class VipPersonDto {
    private Integer vipId;
    private String name;
    private String email;
    private String mobile;
    private String gender;
    private LocalDate dateOfBirth;
    private String address;
    private String profession;
    private String reasonForSecurity;
    private Integer userId; // To associate with the creating user
}