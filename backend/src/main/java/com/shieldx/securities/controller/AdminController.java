package com.shieldx.securities.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shieldx.securities.dto.DashboardStatsResponse;
import com.shieldx.securities.dto.UserResponse;
import com.shieldx.securities.service.AdminService;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @GetMapping("/dashboard")
    public ResponseEntity<DashboardStatsResponse> getDashboardStats() {
        return ResponseEntity.ok(adminService.getDashboardStats());
    }

    @GetMapping("/users")
    public ResponseEntity<List<UserResponse>> listAllUsers() {
        return ResponseEntity.ok(adminService.getAllUsers());
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<UserResponse> getUserDetails(@PathVariable("userId") Integer userId) {
        return ResponseEntity.ok(adminService.getUserDetails(userId));
    }

    @DeleteMapping("/users/{userId}")
    public ResponseEntity<String> deactivateUser(@PathVariable("userId") Integer userId) {
        adminService.deactivateUser(userId);
        return ResponseEntity.ok("User deactivated successfully");
    }
}