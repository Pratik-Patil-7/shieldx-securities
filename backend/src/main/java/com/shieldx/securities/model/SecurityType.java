package com.shieldx.securities.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "security_type")
@Data
public class SecurityType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int stId;

    private String levelName;
    private String description;
    private String isArmed;
    private double pricePerDay;
}
