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

import jakarta.transaction.Transactional;

@Service
@Transactional
public class UserProfileService {

	private final UserRepository userRepo;
	private final LoginRepository loginRepo;
	private final PasswordEncoder passwordEncoder;

	@Autowired
	public UserProfileService(UserRepository userRepo, LoginRepository loginRepo, PasswordEncoder passwordEncoder) {
		this.userRepo = userRepo;
		this.loginRepo = loginRepo;
		this.passwordEncoder = passwordEncoder;
	}

	public UserProfileDTO getUserProfileByUserId(Integer userId) {
		User user = userRepo.findById(userId)
				.orElseThrow(() -> new UsernameNotFoundException("User not found with ID: " + userId));

		return mapToProfileDTO(user);
	}

	public String updateUserProfileByUserId(Integer userId, UserProfileDTO dto) {
		User user = userRepo.findById(userId)
				.orElseThrow(() -> new UsernameNotFoundException("User not found with ID: " + userId));

		updateUserFromDTO(user, dto);
		userRepo.save(user);
		return "Profile updated successfully.";
	}

	public String updatePasswordByUserId(Integer userId, UpdatePasswordDTO dto) {
		validatePasswordDTO(dto);

		Login login = loginRepo.findByUserUserId(userId) // Changed to use relationship
				.orElseThrow(() -> new UsernameNotFoundException("Login not found for user ID: " + userId));

		verifyCurrentPassword(dto.getCurrentPassword(), login.getPassword());

		updateLoginPassword(login, dto.getNewPassword());
		return "Password updated successfully";
	}

	// Helper methods
	private UserProfileDTO mapToProfileDTO(User user) {
		UserProfileDTO dto = new UserProfileDTO();
		dto.setFirstName(user.getFirstName());
		dto.setLastName(user.getLastName());
		dto.setEmail(user.getEmail());
		dto.setMobile(user.getMobile());
		dto.setAddress(user.getAddress());
		return dto;
	}

	private void updateUserFromDTO(User user, UserProfileDTO dto) {
		user.setFirstName(dto.getFirstName());
		user.setLastName(dto.getLastName());
		user.setMobile(dto.getMobile());
		user.setAddress(dto.getAddress());
	}

	private void validatePasswordDTO(UpdatePasswordDTO dto) {
		if (dto.getCurrentPassword() == null || dto.getCurrentPassword().trim().isEmpty()) {
			throw new IllegalArgumentException("Current password cannot be empty");
		}
		if (dto.getNewPassword() == null || dto.getNewPassword().trim().isEmpty()) {
			throw new IllegalArgumentException("New password cannot be empty");
		}
	}

	private void verifyCurrentPassword(String rawPassword, String encodedPassword) {
		if (!passwordEncoder.matches(rawPassword, encodedPassword)) {
			throw new IllegalArgumentException("Current password is incorrect");
		}
	}

	private void updateLoginPassword(Login login, String newPassword) {
		login.setPassword(passwordEncoder.encode(newPassword));
		loginRepo.save(login);
	}
}