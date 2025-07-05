package com.shieldx.securities.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shieldx.securities.dto.BouncerRequest;
import com.shieldx.securities.dto.JobApplicationResponse;
import com.shieldx.securities.service.SecurityStaffService;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminSecurityStaffController {

    @Autowired
    private SecurityStaffService securityStaffService;

    @GetMapping("/applications")
    public ResponseEntity<List<JobApplicationResponse>> listAllApplications() {
        return ResponseEntity.ok(securityStaffService.getAllApplications());
    }

    @PutMapping("/applications/{id}/approve")
    public ResponseEntity<String> approveApplication(@PathVariable("id") Integer applicationId) {
        securityStaffService.approveApplication(applicationId);
        return ResponseEntity.ok("Application approved successfully");
    }

    @PutMapping("/applications/{id}/reject")
    public ResponseEntity<String> rejectApplication(@PathVariable("id") Integer applicationId) {
        securityStaffService.rejectApplication(applicationId);
        return ResponseEntity.ok("Application rejected successfully");
    }

    @PostMapping("/bouncers")
    public ResponseEntity<String> addBouncer(@RequestBody BouncerRequest request) {
        securityStaffService.addBouncer(request);
        return ResponseEntity.ok("Bouncer added successfully");
    }
}