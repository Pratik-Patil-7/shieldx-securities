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

	public Login login(LoginDTO loginDTO) {
		if (loginDTO.getUsername() != null && !loginDTO.getUsername().isEmpty()) {
			Optional<Login> validLogin = loginRepository.findByUsername(loginDTO.getUsername());

			if (validLogin.isPresent()
					&& passwordEncoder.matches(loginDTO.getPassword(), validLogin.get().getPassword())) {
				return validLogin.get();
			}

		} else if (loginDTO.getEmail() != null && !loginDTO.getEmail().isEmpty()) {
			Login validLogin = loginRepository.findByEmail(loginDTO.getEmail());

			if (validLogin != null && passwordEncoder.matches(loginDTO.getPassword(), validLogin.getPassword())) {
				return validLogin;
			}
		}

		return null;
	}

}
