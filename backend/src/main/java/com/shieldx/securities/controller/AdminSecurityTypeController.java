package com.shieldx.securities.controller;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shieldx.securities.dto.SecurityTypeRequest;
import com.shieldx.securities.dto.SecurityTypeResponse;
import com.shieldx.securities.service.SecurityTypeService;

@RestController
@RequestMapping("/api/admin/security-types")
@PreAuthorize("hasRole('ADMIN')")
public class AdminSecurityTypeController {

	@Autowired
	private SecurityTypeService securityTypeService;

	@GetMapping
	public ResponseEntity<List<SecurityTypeResponse>> getAllSecurityTypes() {
		return ResponseEntity.ok(securityTypeService.getAllSecurityTypess());
	}

	@PostMapping
	public ResponseEntity<SecurityTypeResponse> addSecurityType(@RequestBody SecurityTypeRequest request) {
		return ResponseEntity.ok(securityTypeService.addSecurityType(request));
	}

	@PutMapping("/{id}")
	public ResponseEntity<SecurityTypeResponse> updateSecurityType(@PathVariable("id") Integer stId,
			@RequestBody SecurityTypeRequest request) {
		return ResponseEntity.ok(securityTypeService.updateSecurityType(stId, request));
	}



}