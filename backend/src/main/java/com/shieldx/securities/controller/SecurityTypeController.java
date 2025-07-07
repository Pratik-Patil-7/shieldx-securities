package com.shieldx.securities.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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

	@Autowired
	private SecurityTypeService securityTypeService;

	@GetMapping("/types")
	public ResponseEntity<List<String>> getAllSecurityTypes() {
		return ResponseEntity.ok(securityTypeService.getSecurityType());
	}

	@GetMapping("/levels")
	public ResponseEntity<List<String>> getAllSecurityLevels() {
		return ResponseEntity.ok(securityTypeService.getSecurityLevel());
	}

	@GetMapping("/pricing")
	public ResponseEntity<List<SecurityType>> getAllPricing() {
		return ResponseEntity.ok(securityTypeService.getAllPricing());
	}
	
	
}
