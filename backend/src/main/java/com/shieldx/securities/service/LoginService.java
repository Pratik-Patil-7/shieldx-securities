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

	private PasswordEncoder passwordEncoder;	
	private LoginRepository loginRepository;
	@Autowired
	public LoginService(LoginRepository loginRepository,PasswordEncoder passwordEncoder) {
		this.loginRepository = loginRepository;
		this.passwordEncoder = passwordEncoder;
	}
	public Login login(LoginDTO loginDTO) {
		if (loginDTO.getUsername() != null && !loginDTO.getUsername().isEmpty()) {
			Optional<Login> validLogin = loginRepository.findByUsername(loginDTO.getUsername());
			if(validLogin.isPresent()) {	
				Login login = validLogin.get();
				if(login.getStatus().equalsIgnoreCase("active")) {
					if(Integer.valueOf(login.getFailedAttempts())<5) {
						if ( passwordEncoder.matches(loginDTO.getPassword(), validLogin.get().getPassword())) {
							login.setFailedAttempts("0");
							return loginRepository.save(login);
						}else {
							int faildAttempt = Integer.valueOf(login.getFailedAttempts())+1;
							login.setFailedAttempts(String.valueOf(faildAttempt));
							return loginRepository.save(login);
						}
					}else {
						login.setStatus("Disable");
						return loginRepository.save(login);
					}
				}
			}
		} else if (loginDTO.getEmail() != null && !loginDTO.getEmail().isEmpty()) {
			Login validLogin = loginRepository.findByEmail(loginDTO.getEmail());
			if(validLogin != null && validLogin.getStatus().equalsIgnoreCase("Active")) {
				if(Integer.valueOf(validLogin.getFailedAttempts())<5) {	
					if ( passwordEncoder.matches(loginDTO.getPassword(), validLogin.getPassword())) {
						validLogin.setFailedAttempts("0");
						return loginRepository.save(validLogin);
					}else {
						int faildAttempt = Integer.valueOf(validLogin.getFailedAttempts())+1;
						validLogin.setFailedAttempts(String.valueOf(faildAttempt));
						return loginRepository.save(validLogin);
					}
				}else {
					validLogin.setStatus("Disable");
					return loginRepository.save(validLogin);
				}
			}	
		}
		return null;
	}
}
