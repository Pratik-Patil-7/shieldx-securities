package com.shieldx.securities.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	   @Bean
	    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
	        http
	            .csrf(csrf -> csrf.disable()) // Disable CSRF
	            .authorizeHttpRequests(auth -> auth
	                .anyRequest().permitAll() // Allow all endpoints without authentication
	            )
	            .httpBasic(httpBasic -> httpBasic.disable()) // Disable HTTP Basic
	            .formLogin(form -> form.disable()); // Disable Form login

	        return http.build();
	    }

}
