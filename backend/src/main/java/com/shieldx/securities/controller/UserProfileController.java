package com.shieldx.securities.controller;

import java.util.Collections;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shieldx.securities.dto.UpdatePasswordDTO;
import com.shieldx.securities.dto.UserProfileDTO;
import com.shieldx.securities.service.UserProfileService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/users/profile")
public class UserProfileController {

	private final UserProfileService userProfileService;

	public UserProfileController(UserProfileService userProfileService) {
		this.userProfileService = userProfileService;
	}

	@GetMapping
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<UserProfileDTO> getProfile(Authentication authentication) {
		Integer userId = Integer.parseInt(authentication.getName());
		return ResponseEntity.ok(userProfileService.getUserProfileByUserId(userId));
	}

	@PutMapping
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<Map<String, String>> updateProfile(@RequestBody UserProfileDTO dto,
			Authentication authentication) {
		Integer userId = Integer.parseInt(authentication.getName());
		String result = userProfileService.updateUserProfileByUserId(userId, dto);
		return ResponseEntity.ok(Collections.singletonMap("message", result));
	}

	@PutMapping("/password")
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<?> updatePassword(@Valid @RequestBody UpdatePasswordDTO dto, BindingResult bindingResult,
			Authentication authentication) {
		System.out.println(dto.getNewPassword()+" "+dto.getCurrentPassword());

		try {
			Integer userId = Integer.parseInt(authentication.getName());
			System.out.println("User ID: " + userId);
			String result = userProfileService.updatePasswordByUserId(userId, dto);
			return ResponseEntity.ok(Collections.singletonMap("message", result));
		} catch (IllegalArgumentException ex) {
			return ResponseEntity.badRequest().body(Collections.singletonMap("message", ex.getMessage()));
		}
	}
}