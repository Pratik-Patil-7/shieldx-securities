package com.shieldx.securities.dto;

import java.time.LocalDate;

import lombok.Data;

@Data
public class JobApplicationRequest {
    private String name;
    private String email;
    private String mobile;
    private LocalDate dob;
//    private int userId;
    private String gender;
    private String address;
    private String qualification;
    private String experience;
}
