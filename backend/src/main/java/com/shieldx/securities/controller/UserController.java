package com.shieldx.securities.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.shieldx.securities.dto.ChangePasswordRequest;
import com.shieldx.securities.dto.UpdateProfileRequest;
import com.shieldx.securities.dto.UserResponse;
import com.shieldx.securities.service.UserService;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/me")
    public ResponseEntity<UserResponse> getProfile(@AuthenticationPrincipal Integer userId) {
        return ResponseEntity.ok(userService.getProfile(userId));
    }

    @PutMapping("/me")
    public ResponseEntity<UserResponse> updateProfile(@AuthenticationPrincipal Integer userId, 
                                                    @RequestBody UpdateProfileRequest request) {
        return ResponseEntity.ok(userService.updateProfile(userId, request));
    }

    @PutMapping("/change-password")
    public ResponseEntity<String> changePassword(@AuthenticationPrincipal Integer userId, 
                                                @RequestBody ChangePasswordRequest request) {
        userService.changePassword(userId, request);
        return ResponseEntity.ok("Password changed successfully");
    }

    @PostMapping("/upload-avatar")
    public ResponseEntity<String> uploadAvatar(@AuthenticationPrincipal Integer userId, 
                                              @RequestParam("photo") MultipartFile file) {
        String photoUrl = userService.uploadAvatar(userId, file);
        return ResponseEntity.ok("Avatar uploaded successfully: " + photoUrl);
    }
}