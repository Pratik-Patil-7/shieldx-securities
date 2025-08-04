package com.shieldx.securities.dto;

import java.time.LocalDate;

import lombok.Data;

@Data
public class JobApplicationResponse {
	private int applicationId;
	private String name;
	private String email;
	private String mobile;
	private Integer userId;
	private LocalDate dob;
	private String gender;
	private String address;
	private String qualification;
	private String experience;
	private String resumeUrl;
	private String photoUrl;
	private String status;

	public JobApplicationResponse(int applicationId, String name, String email, String mobile, LocalDate dob,
			String gender, String address, String qualification, String experience, String resumeUrl, String photoUrl,
			String status) {
		this.applicationId = applicationId;
		this.name = name;
		this.email = email;
		this.mobile = mobile;
		this.dob = dob;
		this.gender = gender;
		this.address = address;
		this.qualification = qualification;
		this.experience = experience;
		this.resumeUrl = resumeUrl;
		this.photoUrl = photoUrl;
		this.status = status;
	}

	public JobApplicationResponse(int applicationId, LocalDate dob, Integer userId, String address, String email,
			String experience, String gender, String mobile, String name, String photoUrl, String qualification,
			String resumeUrl, String status) {
		this.applicationId = applicationId;
		this.dob = dob;
		this.userId = userId;
		this.address = address;
		this.email = email;
		this.experience = experience;
		this.gender = gender;
		this.mobile = mobile;
		this.name = name;
		this.photoUrl = photoUrl;
		this.qualification = qualification;
		this.resumeUrl = resumeUrl;
		this.status = status;
	}
}