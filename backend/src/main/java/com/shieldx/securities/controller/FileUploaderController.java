package com.shieldx.securities.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.shieldx.securities.service.FileStorageService;

@RestController
@RequestMapping("/api/upload")
public class FileUploaderController {

    @Autowired
    private FileStorageService fileStorageService;

    @PostMapping("/resume")
    public ResponseEntity<String> uploadResume(@AuthenticationPrincipal Integer userId, 
                                              @RequestParam("resume") MultipartFile resume) {
        String resumeUrl = fileStorageService.uploadResume(userId, resume);
        return ResponseEntity.ok("Resume uploaded successfully: " + resumeUrl);
    }

    @PostMapping("/photo")
    public ResponseEntity<String> uploadPhoto(@AuthenticationPrincipal Integer userId, 
                                             @RequestParam("photo") MultipartFile photo) {
        String photoUrl = fileStorageService.uploadPhoto(userId, photo);
        return ResponseEntity.ok("Photo uploaded successfully: " + photoUrl);
    }
}