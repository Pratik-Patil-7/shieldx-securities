package com.shieldx.securities.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shieldx.securities.dto.UpdatePasswordDTO;
import com.shieldx.securities.dto.UserProfileDTO;
import com.shieldx.securities.service.UserProfileService;

@RestController
@RequestMapping("/api/profile")
public class UserProfileController {

	@Autowired
    private UserProfileService userProfileService;

    @GetMapping("/{username}")
    public ResponseEntity<UserProfileDTO> getProfile(@PathVariable String username) {
        return ResponseEntity.ok(userProfileService.getUserProfile(username));
    }

    @PutMapping("/{username}")
    public ResponseEntity<String> updateProfile(@PathVariable String username,
                                                @RequestBody UserProfileDTO dto) {
        String result = userProfileService.updateUserProfile(username, dto);
        return ResponseEntity.ok(result);
    }

    @PutMapping("/{username}/password")
    public ResponseEntity<String> updatePassword(@PathVariable String username,
                                                 @RequestBody UpdatePasswordDTO dto) {
        try {
            String result = userProfileService.updatePassword(username, dto);
            return ResponseEntity.ok(result);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }
}
