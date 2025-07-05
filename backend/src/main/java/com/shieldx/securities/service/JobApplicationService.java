package com.shieldx.securities.service;

import com.shieldx.securities.dto.JobApplicationRequest;
import com.shieldx.securities.dto.JobApplicationResponse;
import com.shieldx.securities.model.JobApplication;
import com.shieldx.securities.model.User;
import com.shieldx.securities.repository.JobApplicationRepository;
import com.shieldx.securities.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class JobApplicationService {

	@Autowired
	private JobApplicationRepository jobApplicationRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private FileStorageService fileStorageService;

	public JobApplicationResponse applyForJob(Integer userId, JobApplicationRequest request) {
		User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

		JobApplication application = new JobApplication();
		application.setName(request.getName());
		application.setEmail(request.getEmail());
		application.setMobile(request.getMobile());
		application.setDob(request.getDob());
		application.setGender(request.getGender());
		application.setAddress(request.getAddress());
		application.setQualification(request.getQualification());
		application.setExperience(request.getExperience());
		application.setStatus("pending");
		jobApplicationRepository.save(application);

		return mapToJobApplicationResponse(application);
	}

	public String[] uploadResumeAndPhoto(Integer userId, MultipartFile resume, MultipartFile photo) {
		if (!resume.getContentType().equals("application/pdf")) {
			throw new RuntimeException("Resume must be a PDF file");
		}
		if (!photo.getContentType().equals("image/jpeg") && !photo.getContentType().equals("image/png")) {
			throw new RuntimeException("Photo must be JPEG or PNG");
		}

		String resumeFileName = "resume_" + userId + "_" + System.currentTimeMillis();
		String photoFileName = "photo_" + userId + "_" + System.currentTimeMillis();
		String resumeUrl = fileStorageService.uploadFile(resume, resumeFileName);
		String photoUrl = fileStorageService.uploadFile(photo, photoFileName);

		JobApplication application = jobApplicationRepository.findByUserId(userId)
				.orElseThrow(() -> new RuntimeException("Application not found"));
		application.setResumeUrl(resumeUrl);
		application.setPhotoUrl(photoUrl);
		jobApplicationRepository.save(application);

		return new String[] { resumeUrl, photoUrl };
	}

	public List<JobApplicationResponse> getUserApplications(Integer userId) {
		return jobApplicationRepository.findByUserId(userId).stream().map(this::mapToJobApplicationResponse)
				.collect(Collectors.toList());
	}

	private JobApplicationResponse mapToJobApplicationResponse(JobApplication application) {
		return new JobApplicationResponse(application.getApplicationId(), application.getName(), application.getEmail(),
				application.getMobile(), application.getDob(), application.getGender(), application.getAddress(),
				application.getQualification(), application.getExperience(), application.getResumeUrl(),
				application.getPhotoUrl(), application.getStatus());
	}
}