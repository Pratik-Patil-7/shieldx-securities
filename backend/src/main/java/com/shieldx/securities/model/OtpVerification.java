package com.shieldx.securities.model;

import java.time.LocalTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "otp_verification")
@Data
public class OtpVerification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int otpId;

    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;

    private String otpCode;
    private LocalTime expiryTime;
    private String verified = "no";
}
