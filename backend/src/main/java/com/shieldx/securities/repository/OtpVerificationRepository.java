package com.shieldx.securities.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.shieldx.securities.model.OtpVerification;

public interface OtpVerificationRepository extends JpaRepository<OtpVerification, Integer> {
    Optional<OtpVerification> findByUserEmailAndOtpCode(String email, String otpCode);
}