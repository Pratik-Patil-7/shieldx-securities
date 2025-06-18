package com.shieldx.securities.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.shieldx.securities.dto.UpdatePasswordDTO;
import com.shieldx.securities.dto.UserProfileDTO;
import com.shieldx.securities.model.Login;
import com.shieldx.securities.model.User;
import com.shieldx.securities.repository.LoginRepository;
import com.shieldx.securities.repository.UserRepository;

@Service
public class UserProfileService {

	@Autowired
	private UserRepository userRepo;
	@Autowired
	private LoginRepository loginRepo;
	@Autowired
	private PasswordEncoder passwordEncoder;

	public UserProfileDTO getUserProfile(String username) {
		User user = userRepo.findByUsername(username)
				.orElseThrow(() -> new UsernameNotFoundException("User not found"));

		UserProfileDTO dto = new UserProfileDTO();
		dto.setFirstName(user.getFirstName());
		dto.setLastName(user.getLastName());
		dto.setEmail(user.getEmail());
		dto.setMobile(user.getMobile());
		dto.setAddress(user.getAddress());
		return dto;
	}

	public String updateUserProfile(String username, UserProfileDTO dto) {
		User user = userRepo.findByUsername(username)
				.orElseThrow(() -> new UsernameNotFoundException("User not found"));

		user.setFirstName(dto.getFirstName());
		user.setLastName(dto.getLastName());
		user.setMobile(dto.getMobile());
		user.setAddress(dto.getAddress());
		userRepo.save(user);
		return "Profile updated successfully.";
	}

	public String updatePassword(String username, UpdatePasswordDTO dto) {
		Login login = loginRepo.findByUsername(username)
				.orElseThrow(() -> new UsernameNotFoundException("Login not found"));

		if (!passwordEncoder.matches(dto.getOldPassword(), login.getPassword())) {
			throw new IllegalArgumentException("Old password is incorrect");
		}

		login.setPassword(passwordEncoder.encode(dto.getNewPassword()));
		loginRepo.save(login);
		return "Password updated successfully.";
	}
}
