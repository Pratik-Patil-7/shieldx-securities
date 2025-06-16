package com.shieldx.securities.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.shieldx.securities.dto.LoginDTO;
import com.shieldx.securities.model.Login;
import com.shieldx.securities.repository.LoginRepository;

@Service
public class LoginService {

	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private LoginRepository loginRepository;

	public Login login(LoginDTO logindto) {
		
		if(logindto.getUsername()!=null && !logindto.getUsername().isEmpty()) {
			
			Login validLogin = loginRepository.findByUsername(logindto.getUsername());
			if(validLogin!=null && passwordEncoder.matches(logindto.getPassword(), validLogin.getPassword())) {
				return validLogin;
			}
		}else if(logindto.getEmail()!=null && !logindto.getEmail().isEmpty()){
			Login validLogin = loginRepository.findByEmail(logindto.getEmail());
			if(validLogin!=null && passwordEncoder.matches(logindto.getPassword(), validLogin.getPassword())) {
				return validLogin;
			}
		}
		return null;
	}
}
