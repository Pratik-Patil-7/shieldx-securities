package com.shieldx.securities.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shieldx.securities.model.SecurityType;
import com.shieldx.securities.service.SecurityTypeService;

@RestController
@RequestMapping("/api/security")
@CrossOrigin(origins = "*")
public class SecurityTypeController {

	private final SecurityTypeService securityTypeService;

	public SecurityTypeController(SecurityTypeService securityTypeService) {
		this.securityTypeService = securityTypeService;
	}

	@GetMapping("/types")
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<List<SecurityType>> getAllSecurityTypes(Authentication authentication) {
		Integer userId = Integer.parseInt(authentication.getName());
		System.out.println("Fetching security types for user ID: " + userId);
		return ResponseEntity.ok(securityTypeService.getAllSecurityTypes());
	}

	@GetMapping("/levels")
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<List<String>> getAllSecurityLevels(Authentication authentication) {
		Integer userId = Integer.parseInt(authentication.getName());
		System.out.println("Fetching security levels for user ID: " + userId);
		return ResponseEntity.ok(securityTypeService.getSecurityLevel());
	}

	@GetMapping("/pricing")
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<List<SecurityType>> getAllPricing(Authentication authentication) {
		Integer userId = Integer.parseInt(authentication.getName());
		System.out.println("Fetching security pricing for user ID: " + userId);
		return ResponseEntity.ok(securityTypeService.getAllPricing());
	}
}