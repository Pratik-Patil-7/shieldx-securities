package com.shieldx.securities.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.shieldx.securities.model.VipPerson;
import com.shieldx.securities.repository.VIPpersonRepository;

@Service
public class BirthDayService {

    @Autowired
    private VIPpersonRepository vipRepo;

    @Autowired
    private JavaMailSender mailSender;

    public void sendBirthdayWishes() {
        LocalDate today = LocalDate.now();
        List<VipPerson> birthdayVIPs = vipRepo.findByDateOfBirth(today);

        for (VipPerson vip : birthdayVIPs) {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(vip.getEmail());
            message.setSubject("🎉 Happy Birthday, " + vip.getName() + "!");
            message.setText("Dear " + vip.getName() + ",\n\nWishing you a very Happy Birthday! 🎂\nStay safe and enjoy your day.\n\n– Team ShieldX");

            mailSender.send(message);
        }
    }
}
