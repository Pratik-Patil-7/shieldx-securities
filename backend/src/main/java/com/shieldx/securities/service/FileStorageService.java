package com.shieldx.securities.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileStorageService {
	public String uploadFile(MultipartFile file, String fileName) {
		// Implement file upload logic (e.g., AWS S3 or Cloudinary)
		return "https://your-storage-bucket/" + fileName;
	}

	public String uploadResume(Integer userId, MultipartFile resume) {
		if (!resume.getContentType().equals("application/pdf")) {
			throw new RuntimeException("Resume must be a PDF file");
		}
		String fileName = "resume_" + userId + "_" + System.currentTimeMillis() + ".pdf";
		return uploadFile(resume, fileName);
	}

	public String uploadPhoto(Integer userId, MultipartFile photo) {
		if (!photo.getContentType().equals("image/jpeg") && !photo.getContentType().equals("image/png")) {
			throw new RuntimeException("Photo must be JPEG or PNG");
		}
		String fileName = "photo_" + userId + "_" + System.currentTimeMillis()
				+ (photo.getContentType().equals("image/jpeg") ? ".jpg" : ".png");
		return uploadFile(photo, fileName);
	}
}