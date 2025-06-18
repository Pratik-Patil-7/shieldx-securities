package com.shieldx.securities;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ShieldxSecuritiesApplication {

	public static void main(String[] args) {
		SpringApplication.run(ShieldxSecuritiesApplication.class, args);
	}

}
