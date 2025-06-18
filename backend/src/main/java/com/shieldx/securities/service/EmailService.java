package com.shieldx.securities.service;

import org.springframework.beans.factory.annotation.Autowired;
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
    
    
    
}
