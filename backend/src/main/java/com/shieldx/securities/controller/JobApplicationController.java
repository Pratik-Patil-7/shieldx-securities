package com.shieldx.securities.controller;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.shieldx.securities.dto.JobApplicationRequest;
import com.shieldx.securities.dto.JobApplicationResponse;
import com.shieldx.securities.service.JobApplicationService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/jobs")
@CrossOrigin(origins = "*")
public class JobApplicationController {

	private final JobApplicationService jobApplicationService;

	public JobApplicationController(JobApplicationService jobApplicationService) {
		this.jobApplicationService = jobApplicationService;
	}

	@PostMapping("/apply")
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<Map<String, String>> applyForJob(@Valid @RequestBody JobApplicationRequest request,
			BindingResult bindingResult, Authentication authentication) {

		if (bindingResult.hasErrors()) {
			return ResponseEntity.badRequest().body(Collections.singletonMap("message",
					"Invalid application data: " + bindingResult.getFieldError().getDefaultMessage()));
		}

		Integer userId = Integer.parseInt(authentication.getName());
		System.out.println("Processing job application for user ID: " + userId); // Optional logging

		JobApplicationResponse response = jobApplicationService.applyForJob(userId, request);
		return ResponseEntity.ok(Collections.singletonMap("message",
				"Job application submitted successfully. Application ID: " + response.getApplicationId()));
	}

	@PostMapping("/upload-resume")
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<Map<String, String>> uploadResumeAndPhoto(@RequestParam("resume") MultipartFile resume,
			@RequestParam("photo") MultipartFile photo, Authentication authentication) {

		if (resume.isEmpty() || photo.isEmpty()) {
			return ResponseEntity.badRequest()
					.body(Collections.singletonMap("message", "Both resume and photo files are required"));
		}

		Integer userId = Integer.parseInt(authentication.getName());
		System.out.println("Processing file upload for user ID: " + userId); // Optional logging

		String[] urls = jobApplicationService.uploadResumeAndPhoto(userId, resume, photo);
		return ResponseEntity
				.ok(Map.of("message", "Files uploaded successfully", "resumeUrl", urls[0], "photoUrl", urls[1]));
	}

	@GetMapping("/my-applications")
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<List<JobApplicationResponse>> getMyApplications(Authentication authentication) {
		Integer userId = Integer.parseInt(authentication.getName());
		System.out.println("Fetching applications for user ID: " + userId); // Optional logging
		return ResponseEntity.ok(jobApplicationService.getUserApplications(userId));
	}

	@GetMapping("/pending")
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<List<JobApplicationResponse>> getPendingApplications(Authentication authentication) {
		Integer userId = Integer.parseInt(authentication.getName());
		System.out.println("Fetching pending applications for user ID: " + userId); // Optional logging
		List<JobApplicationResponse> pendingApplications = jobApplicationService.getPendingApplications(userId);
		return ResponseEntity.ok(pendingApplications);
	}

	@GetMapping("/{id}")
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<?> getApplicationById(@PathVariable Integer id, Authentication authentication) {
		Integer userId = Integer.parseInt(authentication.getName());
		JobApplicationResponse application = jobApplicationService.getApplicationById(id, userId);

		if (application == null) {
			return ResponseEntity.badRequest()
					.body(Collections.singletonMap("message", "Application not found or access denied."));
		}

		return ResponseEntity.ok(application);
	}

}
