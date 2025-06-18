package com.shieldx.securities.service;

import java.util.Optional;

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


public int login(LoginDTO logindto) {
		
		if(logindto.getUsername()!=null && !logindto.getUsername().isEmpty()) {
			Optional<Login> login = loginRepository.findByUsername(logindto.getUsername());
			if(login.isPresent() ) {
				Login validLogin = login.get();
				if(Integer.valueOf(validLogin.getFailedAttempts())<=5 && passwordEncoder.matches(logindto.getPassword(),validLogin.getPassword())) {
					validLogin.setFailedAttempts("0");
					 loginRepository.save(validLogin); 
					 return 0;
				}else {
					int temp = Integer.valueOf(validLogin.getFailedAttempts())+1;
					validLogin.setFailedAttempts(String.valueOf(temp));
					loginRepository.save(validLogin); 
					return Integer.valueOf(validLogin.getFailedAttempts());
				}
			}else
				return 401;
		}else if(logindto.getEmail()!=null && !logindto.getEmail().isEmpty()){
			Login validLogin = loginRepository.findByEmail(logindto.getEmail());
			if(validLogin!=null ) {
				if(Integer.valueOf(validLogin.getFailedAttempts())<=5 && passwordEncoder.matches(logindto.getPassword(),validLogin.getPassword())) {
					validLogin.setFailedAttempts("0");
					 loginRepository.save(validLogin); 
					 return 0;
				}else {
					int temp = Integer.valueOf(validLogin.getFailedAttempts())+1;
					validLogin.setFailedAttempts(String.valueOf(temp));
					loginRepository.save(validLogin); 
					return Integer.valueOf(validLogin.getFailedAttempts());
				}
			}else
				return 401;
		}
		return 401;
	}

}
