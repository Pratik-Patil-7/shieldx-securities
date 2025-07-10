package com.shieldx.securities.model;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "vip_person")
@Data
public class VipPerson {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer vipId;

    private String name;
    private String email;
    private String mobile;
    private String gender;
    private LocalDate dateOfBirth;
    private String address;
    private String profession;
    private String reasonForSecurity;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user; // Link to the user who created this VIP
}