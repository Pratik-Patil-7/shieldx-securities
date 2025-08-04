package com.shieldx.securities.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

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
    
    public void sendPaymentRequestEmail(String to, Integer bookingId, double amount) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(to);
            helper.setSubject("Booking Approved - Complete Your Payment");
            String paymentUrl = "http://localhost:4200/payments/initiate?bookingId=" + bookingId;
            String qrCodeUrl = "https://api.qrserver.com/v1/create-qr-code/?size=150x150&data=" + paymentUrl;
            String htmlContent = "<h3>Your booking (ID: " + bookingId + ") has been approved!</h3>" +
                    "<p>Please complete the payment of ₹" + amount + " to proceed.</p>" +
                    "<p>Scan the QR code below or click <a href='" + paymentUrl + "'>here</a> to pay:</p>" +
                    "<img src='" + qrCodeUrl + "' alt='Payment QR Code' />";
            helper.setText(htmlContent, true);
            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send email: " + e.getMessage());
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
