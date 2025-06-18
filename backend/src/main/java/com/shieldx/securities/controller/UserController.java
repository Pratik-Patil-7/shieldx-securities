package com.shieldx.securities.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shieldx.securities.dto.LoginDTO;
import com.shieldx.securities.dto.UserDTO;
import com.shieldx.securities.model.Login;
import com.shieldx.securities.service.EmailService;
import com.shieldx.securities.service.LoginService;
import com.shieldx.securities.service.UserService;

@RestController
@RequestMapping("/api/users")
public class UserController {

	@Autowired
	private UserService userService;

	@Autowired
	private LoginService loginService;
	
	@Autowired
	private EmailService emailService;
	
	@PostMapping("/register")
	public String register(@RequestBody UserDTO userDTO) {
	    emailService.sendThankYouForRegistration(userDTO.getEmail(), userDTO.getUsername());
		return userService.registerUser(userDTO);
	}
	@PostMapping("/login")
	public Login login(@RequestBody LoginDTO logindto) {
		return  loginService.login(logindto);
	}
}
