package com.shieldx.securities.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "bouncer")
@Data
public class Bouncer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int bouncerId;

    private String name;
    private String gender;
    private String age;
    private String email;
    private String mobile;
    private String address;
    private String qualification;
    private String experience;
    private String resumeUrl;
    private String photoUrl;
    private String isArmed;
    private String status = "pending";
}
