package com.shieldx.securities.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shieldx.securities.dto.LoginDTO;
import com.shieldx.securities.dto.UserDTO;
import com.shieldx.securities.model.Login;
import com.shieldx.securities.service.LoginService;
import com.shieldx.securities.service.UserService;

@RestController
@RequestMapping("/api/users")
public class UserController {
	
	@Autowired
	private UserService userService;
	@Autowired
	private LoginService loginService;
	
	@PostMapping("/register")
	public String register(@RequestBody UserDTO userDTO) {
		return userService.registerUser(userDTO);
	}
	@PostMapping("/login")
	public ResponseEntity<String> login(@RequestBody LoginDTO logindto) {
		 
		int remingAttempt = loginService.login(logindto);
		if(remingAttempt==0) {
			return new ResponseEntity<String>("Login Secussfully",HttpStatus.OK);
		}else if(remingAttempt<=5){
			return new ResponseEntity<String>("Login Attemept remning "+(6-remingAttempt),HttpStatus.OK);
		}else if(remingAttempt==401){
			return new ResponseEntity<String>("wrong username and Password ",HttpStatus.OK);
		}else
			return null;
	}
}
