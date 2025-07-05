package com.shieldx.securities.controller;

import com.shieldx.securities.dto.JobApplicationRequest;
import com.shieldx.securities.dto.JobApplicationResponse;
import com.shieldx.securities.service.JobApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/jobs")
public class JobApplicationController {

	@Autowired
	private JobApplicationService jobApplicationService;

	@PostMapping("/apply")
	public ResponseEntity<JobApplicationResponse> applyForJob(@AuthenticationPrincipal Integer userId,
			@RequestBody JobApplicationRequest request) {
		return ResponseEntity.ok(jobApplicationService.applyForJob(userId, request));
	}

	@PostMapping("/upload-resume")
	public ResponseEntity<String> uploadResumeAndPhoto(@AuthenticationPrincipal Integer userId,
			@RequestParam("resume") MultipartFile resume, @RequestParam("photo") MultipartFile photo) {
		String[] urls = jobApplicationService.uploadResumeAndPhoto(userId, resume, photo);
		return ResponseEntity.ok("Files uploaded successfully: Resume - " + urls[0] + ", Photo - " + urls[1]);
	}

	@GetMapping("/my-applications")
	public ResponseEntity<List<JobApplicationResponse>> getMyApplications(@AuthenticationPrincipal Integer userId) {
		return ResponseEntity.ok(jobApplicationService.getUserApplications(userId));
	}
}