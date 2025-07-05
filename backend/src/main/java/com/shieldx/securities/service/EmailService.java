package com.shieldx.securities.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendThankYouForRegistration(String toEmail, String username) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("Welcome to ShieldX Securities!");
        message.setText("Hello " + username + ",\n\nThank you for registering with ShieldX Securities. We’re glad to have you onboard!\n\nStay secure,\nTeam ShieldX");
        mailSender.send(message);
    }
    

    public void sendOtpEmail(String email, String otpCode) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(email);
            message.setSubject("ShieldX OTP Verification");
            message.setText("Your OTP code is: " + otpCode + "\nThis code is valid for 10 minutes.");
            mailSender.send(message);
        } catch (MailException e) {
            throw new RuntimeException("Failed to send OTP email: " + e.getMessage());
        }
    }

    public void sendResetPasswordEmail(String email, String token) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(email);
            message.setSubject("ShieldX Password Reset");
            message.setText("Use this token to reset your password: " + token + 
                            "\nLink: http://your-app/reset-password?token=" + token + 
                            "\nThis link is valid for 1 hour.");
            mailSender.send(message);
        } catch (MailException e) {
            throw new RuntimeException("Failed to send password reset email: " + e.getMessage());
        }
    }
    
    
    
}
