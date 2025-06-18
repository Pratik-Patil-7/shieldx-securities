package com.shieldx.securities.scheduler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.shieldx.securities.service.BirthDayService;

@Component
public class BirthdayScheduler {

    @Autowired
    private BirthDayService birthdayService;

    // Run every day at 9 AM
    @Scheduled(cron = "0 0 9 * * ?")
    public void checkAndSend() {
        birthdayService.sendBirthdayWishes();
    }
}
